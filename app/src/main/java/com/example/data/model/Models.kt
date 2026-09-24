package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class MenuItem(
  val id: String,
  val name: String,
  val teluguName: String = "",
  val category: MenuCategory,
  val price: Double,
  val description: String,
  val isVeg: Boolean,
  val isBestSeller: Boolean = false,
  val spiceLevel: Int = 2, // 1 to 3
  val rating: Double = 4.5,
  val reviewCount: Int = 120,
  val availableTimings: String = "All Day",
  val imageRes: Int = 0
)

enum class MenuCategory(val displayName: String, val iconName: String) {
  ALL("All Items", "Restaurant"),
  BIRYANI("Biryanis", "LocalFireDepartment"),
  STARTERS("Starters", "LocalDining"),
  TIFFINS("Morning Tiffins", "BakeryDining"),
  CURRIES("Curries & Gravies", "SoupKitchen"),
  BREADS("Tandoori & Breads", "LunchDining"),
  DESSERTS_DRINKS("Desserts & Drinks", "LocalBar")
}

@Entity(tableName = "cart_items")
data class CartItem(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val menuItemId: String,
  val name: String,
  val price: Double,
  val quantity: Int = 1,
  val isVeg: Boolean,
  val spicePreference: String = "Medium",
  val specialNote: String = ""
)

@Entity(tableName = "table_bookings")
data class TableBooking(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val bookingRef: String,
  val customerName: String,
  val customerPhone: String,
  val partySize: Int,
  val bookingDate: String,
  val timeSlot: String,
  val seatingArea: String,
  val specialRequest: String = "",
  val status: String = "Confirmed",
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderRecord(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val orderNumber: String,
  val orderType: String, // "Home Delivery", "Takeaway / Parcel", "Dine-in Order"
  val customerName: String,
  val phone: String,
  val addressOrTable: String,
  val itemsSummary: String,
  val subtotal: Double,
  val taxGst: Double,
  val deliveryFee: Double,
  val discount: Double,
  val grandTotal: Double,
  val paymentMethod: String,
  val status: String = "Preparing", // "Preparing", "Out for Delivery", "Ready for Pickup", "Delivered"
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "customer_reviews")
data class CustomerReview(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val reviewerName: String,
  val rating: Int,
  val comment: String,
  val favoriteDish: String,
  val dateFormatted: String,
  val isVerifiedDiner: Boolean = true
)

@Entity(tableName = "favorites")
data class FavoriteItem(
  @PrimaryKey val menuItemId: String,
  val addedAt: Long = System.currentTimeMillis()
)
