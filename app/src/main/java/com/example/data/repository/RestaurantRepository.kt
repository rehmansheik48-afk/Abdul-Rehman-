package com.example.data.repository

import com.example.R
import com.example.data.db.AppDatabase
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RestaurantRepository(private val database: AppDatabase) {

  val cartDao = database.cartDao()
  val bookingDao = database.tableBookingDao()
  val orderDao = database.orderDao()
  val reviewDao = database.reviewDao()
  val favoriteDao = database.favoriteDao()

  val allCartItems: Flow<List<CartItem>> = cartDao.getAllCartItems()
  val allBookings: Flow<List<TableBooking>> = bookingDao.getAllBookings()
  val allOrders: Flow<List<OrderRecord>> = orderDao.getAllOrders()
  val allReviews: Flow<List<CustomerReview>> = reviewDao.getAllReviews()
  val allFavorites: Flow<List<String>> = favoriteDao.getAllFavoriteIds()

  val restaurantInfo = RestaurantProfile(
    name = "Maturi Restaurant & Hotel",
    tagline = "Legendary Biryani & Authentic Andhra Flavors",
    address = "Revenue Ward 18, 19/186, H. Junction Road, Bandar Road, Ramanagaram, Nuzividu, Krishna District, Andhra Pradesh - 521201",
    shortAddress = "Ramanagaram, Nuzvid, AP 521201",
    plusCode = "QVP2+V76 Nuzividu, Andhra Pradesh",
    googleMapsUrl = "https://maps.app.goo.gl/tBf7pusyyvvvt9JX7?g_st=ac",
    latitude = 16.7867853,
    longitude = 80.8508248,
    phone = "+91 86562 32456",
    alternatePhone = "+91 94401 88990",
    rating = 4.3,
    totalReviewsCount = 528,
    breakfastTimings = "6:00 AM – 11:00 AM",
    lunchTimings = "12:00 PM – 4:00 PM",
    dinnerTimings = "5:30 PM – 10:30 PM",
    isOpen = true,
    statusText = "Open Now • Closes at 10:30 PM",
    features = listOf(
      "Authentic Hyderabadi & Andhra Dum Biryanis",
      "Air-Conditioned Family Dining Hall",
      "Fast Takeaway & Delivery Parcel Counter",
      "Morning Breakfast Tiffins from 6:00 AM",
      "Banquet & Catering for Weddings & Events"
    )
  )

  val menuList: List<MenuItem> = listOf(
    // BIRYANIS
    MenuItem(
      id = "biryani_01",
      name = "Maturi Special Chicken Dum Biryani",
      teluguName = "మాతూరి స్పెషల్ చికెన్ దమ్ బిర్యానీ",
      category = MenuCategory.BIRYANI,
      price = 260.0,
      description = "Our signature aromatic slow-cooked basmati rice with tender succulent chicken pieces, marinated with secret Andhra spices, served with Mirchi Ka Salan & Raita.",
      isVeg = false,
      isBestSeller = true,
      spiceLevel = 3,
      rating = 4.8,
      reviewCount = 310,
      imageRes = R.drawable.img_signature_biryani
    ),
    MenuItem(
      id = "biryani_02",
      name = "Andhra Gongura Chicken Biryani",
      teluguName = "గోంగూర చికెన్ బిర్యానీ",
      category = MenuCategory.BIRYANI,
      price = 280.0,
      description = "Tangy sorrel Gongura leaves paste blended with spicy chicken dum biryani. A true regional Andhra delicacy loved by locals.",
      isVeg = false,
      isBestSeller = true,
      spiceLevel = 3,
      rating = 4.7,
      reviewCount = 185,
      imageRes = R.drawable.img_signature_biryani
    ),
    MenuItem(
      id = "biryani_03",
      name = "Special Cashew Veg Dum Biryani",
      teluguName = "స్పెషల్ జీడిపప్పు వెజ్ బిర్యానీ",
      category = MenuCategory.BIRYANI,
      price = 230.0,
      description = "Royal vegetarian preparation loaded with roasted whole cashews, fresh paneer cubes, beans, carrots, and caramelized mint.",
      isVeg = true,
      isBestSeller = true,
      spiceLevel = 2,
      rating = 4.6,
      reviewCount = 142,
      imageRes = R.drawable.img_signature_biryani
    ),
    MenuItem(
      id = "biryani_04",
      name = "Mutton Dum Biryani (Royal Gosht)",
      teluguName = "మటన్ దమ్ బిర్యానీ",
      category = MenuCategory.BIRYANI,
      price = 380.0,
      description = "Tender baby goat meat marinated overnight in yogurt, cardamom, cinnamon and cooked on dum with long-grain Dehradun basmati rice.",
      isVeg = false,
      isBestSeller = true,
      spiceLevel = 2,
      rating = 4.9,
      reviewCount = 220,
      imageRes = R.drawable.img_signature_biryani
    ),
    MenuItem(
      id = "biryani_05",
      name = "Chicken Fry Piece Biryani",
      teluguName = "చికెన్ ఫ్రై పీస్ బిర్యానీ",
      category = MenuCategory.BIRYANI,
      price = 290.0,
      description = "Crispy spicy Andhra shallow-fried bone-in chicken placed generously over fragrant spiced biryani rice.",
      isVeg = false,
      isBestSeller = false,
      spiceLevel = 3,
      rating = 4.6,
      reviewCount = 95,
      imageRes = R.drawable.img_signature_biryani
    ),
    MenuItem(
      id = "biryani_06",
      name = "Coastal Prawns Dum Biryani",
      teluguName = "రొయ్యల దమ్ బిర్యానీ",
      category = MenuCategory.BIRYANI,
      price = 340.0,
      description = "Juicy coastal fresh prawns gently spiced with green chilies, coriander and layered with saffron-infused rice.",
      isVeg = false,
      isBestSeller = false,
      spiceLevel = 2,
      rating = 4.7,
      reviewCount = 88,
      imageRes = R.drawable.img_signature_biryani
    ),
    MenuItem(
      id = "biryani_07",
      name = "Egg Dum Biryani",
      teluguName = "ఎగ్ దమ్ బిర్యానీ",
      category = MenuCategory.BIRYANI,
      price = 190.0,
      description = "Two pan-roasted spiced boiled eggs served with aromatic dum biryani rice and thick salan gravy.",
      isVeg = false,
      isBestSeller = false,
      spiceLevel = 2,
      rating = 4.4,
      reviewCount = 64,
      imageRes = R.drawable.img_signature_biryani
    ),

    // STARTERS
    MenuItem(
      id = "starter_01",
      name = "Maturi Signature Cashew Chicken Fry",
      teluguName = "జీడిపప్పు చికెన్ వేపుడు",
      category = MenuCategory.STARTERS,
      price = 290.0,
      description = "Crunchy bone-free chicken chunks stir-fried with an abundance of roasted golden whole cashews, curry leaves and green chillies.",
      isVeg = false,
      isBestSeller = true,
      spiceLevel = 3,
      rating = 4.9,
      reviewCount = 290,
      imageRes = R.drawable.img_andhra_starters
    ),
    MenuItem(
      id = "starter_02",
      name = "Andhra Chicken 65",
      teluguName = "చికెన్ 65",
      category = MenuCategory.STARTERS,
      price = 250.0,
      description = "Crisp deep-fried chicken cubes tossed in house hot red garlic sauce, curry leaves and lemon.",
      isVeg = false,
      isBestSeller = true,
      spiceLevel = 3,
      rating = 4.7,
      reviewCount = 180,
      imageRes = R.drawable.img_andhra_starters
    ),
    MenuItem(
      id = "starter_03",
      name = "Kaju Paneer Roast",
      teluguName = "జీడిపప్పు పన్నీర్ రోస్ట్",
      category = MenuCategory.STARTERS,
      price = 240.0,
      description = "Fresh soft malai paneer cubes tossed with whole cashews, crushed black pepper and Andhra masala.",
      isVeg = true,
      isBestSeller = true,
      spiceLevel = 2,
      rating = 4.7,
      reviewCount = 150,
      imageRes = R.drawable.img_andhra_starters
    ),
    MenuItem(
      id = "starter_04",
      name = "Apollo Fish Fry",
      teluguName = "అపోలో ఫిష్ ఫ్రై",
      category = MenuCategory.STARTERS,
      price = 320.0,
      description = "Succulent boneless fish fillets spiced with mustard seeds, curry leaves, ginger and lemon zest.",
      isVeg = false,
      isBestSeller = false,
      spiceLevel = 2,
      rating = 4.6,
      reviewCount = 110,
      imageRes = R.drawable.img_andhra_starters
    ),
    MenuItem(
      id = "starter_05",
      name = "Crispy Pepper Corn Salt",
      teluguName = "క్రిస్పీ పెప్పర్ కార్న్",
      category = MenuCategory.STARTERS,
      price = 180.0,
      description = "Tender sweet corn tossed in a light tempura crust with freshly crushed black pepper and scallions.",
      isVeg = true,
      isBestSeller = false,
      spiceLevel = 1,
      rating = 4.5,
      reviewCount = 75,
      imageRes = R.drawable.img_andhra_starters
    ),

    // MORNING TIFFINS
    MenuItem(
      id = "tiffin_01",
      name = "Ghee Karam Dosa",
      teluguName = "నెయ్యి కారం దోశ",
      category = MenuCategory.TIFFINS,
      price = 80.0,
      description = "Super crispy golden crepe spread with Andhra red chilli garlic karam and drenched in pure melted country ghee.",
      isVeg = true,
      isBestSeller = true,
      spiceLevel = 2,
      rating = 4.8,
      reviewCount = 210,
      availableTimings = "6:00 AM – 11:00 AM",
      imageRes = R.drawable.img_restaurant_hero
    ),
    MenuItem(
      id = "tiffin_02",
      name = "Steaming Idli Sambar (4 Pcs)",
      teluguName = "ఇడ్లీ సాంబార్ (4 ముక్కలు)",
      category = MenuCategory.TIFFINS,
      price = 50.0,
      description = "Piping hot cloud-soft steamed idlis served with fragrant drumstick sambar and fresh peanut chutney.",
      isVeg = true,
      isBestSeller = false,
      spiceLevel = 1,
      rating = 4.7,
      reviewCount = 190,
      availableTimings = "6:00 AM – 11:00 AM",
      imageRes = R.drawable.img_restaurant_hero
    ),
    MenuItem(
      id = "tiffin_03",
      name = "Crispy Medu Vada (2 Pcs)",
      teluguName = "మెదు వడ",
      category = MenuCategory.TIFFINS,
      price = 60.0,
      description = "Golden crunchy urad dal fritters crispy on the outside, soft inside, served with hot sambar and coconut dip.",
      isVeg = true,
      isBestSeller = false,
      spiceLevel = 1,
      rating = 4.6,
      reviewCount = 120,
      availableTimings = "6:00 AM – 11:00 AM",
      imageRes = R.drawable.img_restaurant_hero
    ),
    MenuItem(
      id = "tiffin_04",
      name = "Poori Masala (3 Pcs)",
      teluguName = "పూరీ మసాలా",
      category = MenuCategory.TIFFINS,
      price = 70.0,
      description = "Hot fluffy golden wheat pooris accompanied by traditional spiced potato curry.",
      isVeg = true,
      isBestSeller = false,
      spiceLevel = 1,
      rating = 4.5,
      reviewCount = 95,
      availableTimings = "6:00 AM – 11:00 AM",
      imageRes = R.drawable.img_restaurant_hero
    ),
    MenuItem(
      id = "tiffin_05",
      name = "MLA Pesarattu with Upma",
      teluguName = "ఎంఎల్ఏ పెసరట్టు ఉప్మా",
      category = MenuCategory.TIFFINS,
      price = 95.0,
      description = "Nutritious whole green moong dal crepe stuffed with rich ghee upma, served with tangy ginger allam pachadi.",
      isVeg = true,
      isBestSeller = true,
      spiceLevel = 2,
      rating = 4.9,
      reviewCount = 175,
      availableTimings = "6:00 AM – 11:00 AM",
      imageRes = R.drawable.img_restaurant_hero
    ),

    // CURRIES & GRAVIES
    MenuItem(
      id = "curry_01",
      name = "Maturi Butter Chicken Masala",
      teluguName = "బటర్ చికెన్ మసాలా",
      category = MenuCategory.CURRIES,
      price = 260.0,
      description = "Charred tandoori chicken pieces simmered in a velvety smooth tomato cashew cream makhani gravy.",
      isVeg = false,
      isBestSeller = true,
      spiceLevel = 1,
      rating = 4.8,
      reviewCount = 160,
      imageRes = R.drawable.img_andhra_starters
    ),
    MenuItem(
      id = "curry_02",
      name = "Andhra Kaju Chicken Curry",
      teluguName = "ఆంధ్రా కాజు చికెన్ కూర",
      category = MenuCategory.CURRIES,
      price = 280.0,
      description = "Spicy country-style chicken curry cooked with caramelized onions, roasted cashews and aromatic garam masala.",
      isVeg = false,
      isBestSeller = true,
      spiceLevel = 3,
      rating = 4.7,
      reviewCount = 130,
      imageRes = R.drawable.img_andhra_starters
    ),
    MenuItem(
      id = "curry_03",
      name = "Paneer Butter Masala",
      teluguName = "పన్నీర్ బటర్ మసాలా",
      category = MenuCategory.CURRIES,
      price = 220.0,
      description = "Soft cottage cheese cubes cooked in rich mild spiced butter gravy with dried fenugreek leaves.",
      isVeg = true,
      isBestSeller = false,
      spiceLevel = 1,
      rating = 4.6,
      reviewCount = 110,
      imageRes = R.drawable.img_andhra_starters
    ),
    MenuItem(
      id = "curry_04",
      name = "Traditional Andhra Veg Thali",
      teluguName = "ఆంధ్రా వెజ్ భోజనం",
      category = MenuCategory.CURRIES,
      price = 160.0,
      description = "Steaming Sona Masoori rice served with Ghee, Pappu (Dal), Drumstick Sambar, Tomato Rasam, Palya Vepudu, Curd, and Papad.",
      isVeg = true,
      isBestSeller = true,
      spiceLevel = 2,
      rating = 4.8,
      reviewCount = 240,
      imageRes = R.drawable.img_restaurant_hero
    ),

    // BREADS
    MenuItem(
      id = "bread_01",
      name = "Butter Naan",
      teluguName = "బటర్ నాన్",
      category = MenuCategory.BREADS,
      price = 45.0,
      description = "Leavened flatbread baked in clay tandoor and brushed generously with butter.",
      isVeg = true,
      isBestSeller = false,
      spiceLevel = 0,
      rating = 4.6,
      reviewCount = 80,
      imageRes = R.drawable.img_restaurant_hero
    ),
    MenuItem(
      id = "bread_02",
      name = "Garlic Naan",
      teluguName = "గార్లిక్ నాన్",
      category = MenuCategory.BREADS,
      price = 55.0,
      description = "Clay-oven baked naan infused with minced roasted garlic and fresh coriander.",
      isVeg = true,
      isBestSeller = false,
      spiceLevel = 1,
      rating = 4.7,
      reviewCount = 95,
      imageRes = R.drawable.img_restaurant_hero
    ),
    MenuItem(
      id = "bread_03",
      name = "Rumali Roti (2 Pcs)",
      teluguName = "రుమాలి రోటీ",
      category = MenuCategory.BREADS,
      price = 50.0,
      description = "Ultra thin, soft handkerchief-style breads tossed on an inverted tawa.",
      isVeg = true,
      isBestSeller = false,
      spiceLevel = 0,
      rating = 4.5,
      reviewCount = 70,
      imageRes = R.drawable.img_restaurant_hero
    ),

    // DESSERTS & DRINKS
    MenuItem(
      id = "dessert_01",
      name = "Nuzvid Sweet Mango Lassi",
      teluguName = "నూజివీడు మామిడి లస్సీ",
      category = MenuCategory.DESSERTS_DRINKS,
      price = 70.0,
      description = "Thick, creamy chilled yogurt smoothie infused with sweet pulp from world-famous Nuzvid Banganapalli mangoes.",
      isVeg = true,
      isBestSeller = true,
      spiceLevel = 0,
      rating = 4.9,
      reviewCount = 190,
      imageRes = R.drawable.img_restaurant_hero
    ),
    MenuItem(
      id = "dessert_02",
      name = "Hot Gulab Jamun with Rabri (2 Pcs)",
      teluguName = "గులాబ్ జామూన్ రబ్రీ",
      category = MenuCategory.DESSERTS_DRINKS,
      price = 60.0,
      description = "Melt-in-the-mouth warm khoya dumplings served with rich condensed cardamom milk rabri.",
      isVeg = true,
      isBestSeller = false,
      spiceLevel = 0,
      rating = 4.7,
      reviewCount = 110,
      imageRes = R.drawable.img_restaurant_hero
    ),
    MenuItem(
      id = "dessert_03",
      name = "South Indian Filter Coffee",
      teluguName = "ఫిల్టర్ కాఫీ",
      category = MenuCategory.DESSERTS_DRINKS,
      price = 35.0,
      description = "Authentic freshly brewed chicory decoction frothed with boiling hot whole milk in traditional brass dabarah.",
      isVeg = true,
      isBestSeller = false,
      spiceLevel = 0,
      rating = 4.8,
      reviewCount = 135,
      imageRes = R.drawable.img_restaurant_hero
    )
  )

  suspend fun initializeDefaultReviewsIfEmpty() {
    val count = reviewDao.getReviewCount()
    if (count == 0) {
      val defaultReviews = listOf(
        CustomerReview(
          reviewerName = "Suresh Reddy",
          rating = 5,
          comment = "Hands down the best biryani in Nuzvid! The Maturi Special Chicken Dum Biryani has that authentic aroma and perfectly spiced chicken. Always stop here when traveling through Krishna district.",
          favoriteDish = "Maturi Special Chicken Dum Biryani",
          dateFormatted = "2 days ago"
        ),
        CustomerReview(
          reviewerName = "Lakshmi Narayana",
          rating = 5,
          comment = "We had a family dinner in their AC hall. The Cashew Chicken Fry starter was out of this world — loaded with whole roasted cashews. Excellent quick service and reasonable prices.",
          favoriteDish = "Maturi Signature Cashew Chicken Fry",
          dateFormatted = "1 week ago"
        ),
        CustomerReview(
          reviewerName = "Praveen Kumar",
          rating = 4,
          comment = "Morning tiffins are super fresh! The Ghee Karam Dosa with drumstick sambar is a must try. Very clean place right on H. Junction / Bandar Road.",
          favoriteDish = "Ghee Karam Dosa",
          dateFormatted = "2 weeks ago"
        ),
        CustomerReview(
          reviewerName = "Deepika Varma",
          rating = 5,
          comment = "Gongura Chicken Biryani is pure heaven. Authentic Andhra taste with the right punch of sour and spice. Highly recommend booking a table on weekends.",
          favoriteDish = "Andhra Gongura Chicken Biryani",
          dateFormatted = "3 weeks ago"
        )
      )
      reviewDao.insertAllReviews(defaultReviews)
    }
  }

  suspend fun addToCart(item: MenuItem, spicePreference: String = "Medium", note: String = "") {
    val existing = cartDao.getCartItemByMenuId(item.id)
    if (existing != null) {
      cartDao.updateQuantity(existing.id, existing.quantity + 1)
    } else {
      cartDao.insertOrUpdate(
        CartItem(
          menuItemId = item.id,
          name = item.name,
          price = item.price,
          quantity = 1,
          isVeg = item.isVeg,
          spicePreference = spicePreference,
          specialNote = note
        )
      )
    }
  }

  suspend fun decrementOrRemoveFromCart(menuItemId: String) {
    val existing = cartDao.getCartItemByMenuId(menuItemId) ?: return
    if (existing.quantity > 1) {
      cartDao.updateQuantity(existing.id, existing.quantity - 1)
    } else {
      cartDao.delete(existing)
    }
  }

  suspend fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
    if (newQuantity <= 0) {
      cartDao.deleteById(cartItemId)
    } else {
      cartDao.updateQuantity(cartItemId, newQuantity)
    }
  }

  suspend fun clearCart() {
    cartDao.clearCart()
  }

  suspend fun bookTable(booking: TableBooking): Long {
    return bookingDao.insertBooking(booking)
  }

  suspend fun cancelBooking(id: Long) {
    bookingDao.updateStatus(id, "Cancelled")
  }

  suspend fun placeOrder(order: OrderRecord): Long {
    val id = orderDao.insertOrder(order)
    cartDao.clearCart()
    return id
  }

  suspend fun addReview(review: CustomerReview): Long {
    return reviewDao.insertReview(review)
  }

  suspend fun toggleFavorite(menuItemId: String, isFav: Boolean) {
    if (isFav) {
      favoriteDao.removeFavorite(menuItemId)
    } else {
      favoriteDao.addFavorite(FavoriteItem(menuItemId))
    }
  }
}

data class RestaurantProfile(
  val name: String,
  val tagline: String,
  val address: String,
  val shortAddress: String,
  val plusCode: String,
  val googleMapsUrl: String,
  val latitude: Double,
  val longitude: Double,
  val phone: String,
  val alternatePhone: String,
  val rating: Double,
  val totalReviewsCount: Int,
  val breakfastTimings: String,
  val lunchTimings: String,
  val dinnerTimings: String,
  val isOpen: Boolean,
  val statusText: String,
  val features: List<String>
)
