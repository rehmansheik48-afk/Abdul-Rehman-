package com.example.data.db

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
  @Query("SELECT * FROM cart_items ORDER BY id ASC")
  fun getAllCartItems(): Flow<List<CartItem>>

  @Query("SELECT * FROM cart_items WHERE menuItemId = :menuItemId LIMIT 1")
  suspend fun getCartItemByMenuId(menuItemId: String): CartItem?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(cartItem: CartItem): Long

  @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
  suspend fun updateQuantity(id: Long, quantity: Int)

  @Delete
  suspend fun delete(cartItem: CartItem)

  @Query("DELETE FROM cart_items WHERE id = :id")
  suspend fun deleteById(id: Long)

  @Query("DELETE FROM cart_items WHERE menuItemId = :menuItemId")
  suspend fun deleteByMenuItemId(menuItemId: String)

  @Query("DELETE FROM cart_items")
  suspend fun clearCart()
}

@Dao
interface TableBookingDao {
  @Query("SELECT * FROM table_bookings ORDER BY id DESC")
  fun getAllBookings(): Flow<List<TableBooking>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBooking(booking: TableBooking): Long

  @Query("UPDATE table_bookings SET status = :status WHERE id = :id")
  suspend fun updateStatus(id: Long, status: String)

  @Query("DELETE FROM table_bookings WHERE id = :id")
  suspend fun deleteBooking(id: Long)
}

@Dao
interface OrderDao {
  @Query("SELECT * FROM orders ORDER BY createdAt DESC")
  fun getAllOrders(): Flow<List<OrderRecord>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrder(order: OrderRecord): Long

  @Query("UPDATE orders SET status = :status WHERE id = :id")
  suspend fun updateOrderStatus(id: Long, status: String)

  @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
  suspend fun getOrderById(id: Long): OrderRecord?
}

@Dao
interface ReviewDao {
  @Query("SELECT * FROM customer_reviews ORDER BY id DESC")
  fun getAllReviews(): Flow<List<CustomerReview>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReview(review: CustomerReview): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllReviews(reviews: List<CustomerReview>)

  @Query("SELECT COUNT(*) FROM customer_reviews")
  suspend fun getReviewCount(): Int
}

@Dao
interface FavoriteDao {
  @Query("SELECT menuItemId FROM favorites")
  fun getAllFavoriteIds(): Flow<List<String>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addFavorite(fav: FavoriteItem)

  @Query("DELETE FROM favorites WHERE menuItemId = :menuItemId")
  suspend fun removeFavorite(menuItemId: String)
}

@Database(
  entities = [CartItem::class, TableBooking::class, OrderRecord::class, CustomerReview::class, FavoriteItem::class],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun cartDao(): CartDao
  abstract fun tableBookingDao(): TableBookingDao
  abstract fun orderDao(): OrderDao
  abstract fun reviewDao(): ReviewDao
  abstract fun favoriteDao(): FavoriteDao
}
