package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.db.AppDatabase
import com.example.data.model.*
import com.example.data.repository.RestaurantProfile
import com.example.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {

  private val database = Room.databaseBuilder(
    application.applicationContext,
    AppDatabase::class.java,
    "maturi_restaurant.db"
  ).fallbackToDestructiveMigration().build()

  val repository = RestaurantRepository(database)

  val restaurantProfile: RestaurantProfile = repository.restaurantInfo
  val fullMenuList: List<MenuItem> = repository.menuList

  // Observables from Room
  val cartItems: StateFlow<List<CartItem>> = repository.allCartItems.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val tableBookings: StateFlow<List<TableBooking>> = repository.allBookings.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val orders: StateFlow<List<OrderRecord>> = repository.allOrders.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val customerReviews: StateFlow<List<CustomerReview>> = repository.allReviews.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val favoriteIds: StateFlow<List<String>> = repository.allFavorites.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // Filter & Search states
  private val _searchQuery = MutableStateFlow("")
  val searchQuery = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow(MenuCategory.ALL)
  val selectedCategory = _selectedCategory.asStateFlow()

  private val _vegOnlyFilter = MutableStateFlow(false)
  val vegOnlyFilter = _vegOnlyFilter.asStateFlow()

  private val _appliedCoupon = MutableStateFlow<String?>(null)
  val appliedCoupon = _appliedCoupon.asStateFlow()

  // Booking UI state
  private val _bookingSuccessDialog = MutableStateFlow<TableBooking?>(null)
  val bookingSuccessDialog = _bookingSuccessDialog.asStateFlow()

  // Order Success UI state
  private val _orderSuccessDialog = MutableStateFlow<OrderRecord?>(null)
  val orderSuccessDialog = _orderSuccessDialog.asStateFlow()

  init {
    viewModelScope.launch {
      repository.initializeDefaultReviewsIfEmpty()
    }
  }

  // Filtered menu
  val filteredMenu: StateFlow<List<MenuItem>> = combine(
    searchQuery,
    selectedCategory,
    vegOnlyFilter
  ) { query, category, vegOnly ->
    fullMenuList.filter { item ->
      val matchesQuery = query.isBlank() ||
        item.name.contains(query, ignoreCase = true) ||
        item.teluguName.contains(query, ignoreCase = true) ||
        item.description.contains(query, ignoreCase = true)

      val matchesCategory = (category == MenuCategory.ALL) || (item.category == category)
      val matchesVeg = !vegOnly || item.isVeg

      matchesQuery && matchesCategory && matchesVeg
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = fullMenuList
  )

  // Cart Calculations
  val cartSubtotal: StateFlow<Double> = cartItems.map { items ->
    items.sumOf { it.price * it.quantity }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

  val cartItemCount: StateFlow<Int> = cartItems.map { items ->
    items.sumOf { it.quantity }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

  fun onSearchQueryChanged(q: String) {
    _searchQuery.value = q
  }

  fun onCategorySelected(cat: MenuCategory) {
    _selectedCategory.value = cat
  }

  fun toggleVegFilter() {
    _vegOnlyFilter.value = !_vegOnlyFilter.value
  }

  fun addToCart(item: MenuItem, spice: String = "Medium", note: String = "") {
    viewModelScope.launch {
      repository.addToCart(item, spice, note)
    }
  }

  fun decrementCart(menuItemId: String) {
    viewModelScope.launch {
      repository.decrementOrRemoveFromCart(menuItemId)
    }
  }

  fun updateCartQuantity(cartItemId: Long, quantity: Int) {
    viewModelScope.launch {
      repository.updateCartQuantity(cartItemId, quantity)
    }
  }

  fun clearCart() {
    viewModelScope.launch {
      repository.clearCart()
      _appliedCoupon.value = null
    }
  }

  fun applyCoupon(code: String): Boolean {
    val clean = code.trim().uppercase()
    return if (clean == "MATURI50" || clean == "WELCOME100" || clean == "BIRYANI20") {
      _appliedCoupon.value = clean
      true
    } else {
      false
    }
  }

  fun removeCoupon() {
    _appliedCoupon.value = null
  }

  fun toggleFavorite(itemId: String) {
    viewModelScope.launch {
      val isFav = favoriteIds.value.contains(itemId)
      repository.toggleFavorite(itemId, isFav)
    }
  }

  fun submitTableBooking(
    name: String,
    phone: String,
    partySize: Int,
    dateStr: String,
    timeSlot: String,
    seatingArea: String,
    specialRequest: String
  ) {
    viewModelScope.launch {
      val randomDigits = (1000..9999).random()
      val ref = "MR-$randomDigits"
      val booking = TableBooking(
        bookingRef = ref,
        customerName = name.ifBlank { "Guest Diner" },
        customerPhone = phone.ifBlank { "9876543210" },
        partySize = partySize,
        bookingDate = dateStr,
        timeSlot = timeSlot,
        seatingArea = seatingArea,
        specialRequest = specialRequest,
        status = "Confirmed"
      )
      repository.bookTable(booking)
      _bookingSuccessDialog.value = booking
    }
  }

  fun dismissBookingDialog() {
    _bookingSuccessDialog.value = null
  }

  fun cancelBooking(id: Long) {
    viewModelScope.launch {
      repository.cancelBooking(id)
    }
  }

  fun placeOrder(
    orderType: String,
    customerName: String,
    phone: String,
    addressOrTable: String,
    paymentMethod: String
  ) {
    viewModelScope.launch {
      val items = cartItems.value
      if (items.isEmpty()) return@launch

      val subtotal = items.sumOf { it.price * it.quantity }
      val taxGst = subtotal * 0.05
      val deliveryFee = if (orderType == "Home Delivery") 40.0 else 0.0
      val discount = when (_appliedCoupon.value) {
        "MATURI50" -> 50.0
        "WELCOME100" -> 100.0
        "BIRYANI20" -> subtotal * 0.20
        else -> 0.0
      }.coerceAtMost(subtotal)
      val grandTotal = (subtotal + taxGst + deliveryFee - discount).coerceAtLeast(0.0)

      val summary = items.joinToString(", ") { "${it.quantity}x ${it.name}" }
      val orderNum = "ORD-" + System.currentTimeMillis().toString().takeLast(6)

      val order = OrderRecord(
        orderNumber = orderNum,
        orderType = orderType,
        customerName = customerName.ifBlank { "Valued Diner" },
        phone = phone.ifBlank { "9876543210" },
        addressOrTable = addressOrTable.ifBlank { if (orderType == "Home Delivery") "Nuzvid Town" else "Table / Counter" },
        itemsSummary = summary,
        subtotal = subtotal,
        taxGst = taxGst,
        deliveryFee = deliveryFee,
        discount = discount,
        grandTotal = grandTotal,
        paymentMethod = paymentMethod,
        status = "Preparing"
      )

      val id = repository.placeOrder(order)
      _appliedCoupon.value = null
      _orderSuccessDialog.value = order.copy(id = id)
    }
  }

  fun dismissOrderDialog() {
    _orderSuccessDialog.value = null
  }

  fun submitReview(name: String, rating: Int, comment: String, favDish: String) {
    viewModelScope.launch {
      val review = CustomerReview(
        reviewerName = name.ifBlank { "Foodie Explorer" },
        rating = rating.coerceIn(1, 5),
        comment = comment.ifBlank { "Delicious authentic food and great ambiance!" },
        favoriteDish = favDish.ifBlank { "Maturi Special Biryani" },
        dateFormatted = "Just now"
      )
      repository.addReview(review)
    }
  }

  // System Intents: Call phone, Open Google Maps, Share
  fun openRestaurantInMaps(context: Context) {
    val uri = Uri.parse(restaurantProfile.googleMapsUrl)
    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
    try {
      context.startActivity(mapIntent)
    } catch (e: Exception) {
      val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${restaurantProfile.latitude},${restaurantProfile.longitude}"))
      context.startActivity(webIntent)
    }
  }

  fun callRestaurant(context: Context) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
      data = Uri.parse("tel:${restaurantProfile.phone.replace(" ", "")}")
    }
    context.startActivity(intent)
  }

  fun shareRestaurant(context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(
        Intent.EXTRA_TEXT,
        "Check out ${restaurantProfile.name} in Nuzvid! Famous for authentic Dum Biryanis and Andhra starters.\nLocation: ${restaurantProfile.googleMapsUrl}\nPhone: ${restaurantProfile.phone}"
      )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share Maturi Restaurant"))
  }
}
