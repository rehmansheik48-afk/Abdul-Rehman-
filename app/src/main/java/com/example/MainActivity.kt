package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.about.AboutScreen
import com.example.ui.screens.cart.CartScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.menu.MenuScreen
import com.example.ui.screens.table.TableBookingScreen
import com.example.ui.screens.website.WebsiteScreen
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SaffronAmber
import com.example.viewmodel.RestaurantViewModel

enum class ScreenTab(val title: String, val icon: ImageVector, val tag: String) {
  HOME("Home", Icons.Filled.Home, "nav_tab_home"),
  MENU("Menu", Icons.Filled.RestaurantMenu, "nav_tab_menu"),
  BOOK_TABLE("Book Table", Icons.Filled.TableRestaurant, "nav_tab_book_table"),
  CART("Cart", Icons.Filled.ShoppingBag, "nav_tab_cart"),
  ABOUT("About", Icons.Filled.Place, "nav_tab_about")
}

class MainActivity : ComponentActivity() {

  private val viewModel: RestaurantViewModel by viewModels()

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        val context = LocalContext.current
        var currentTab by remember { mutableStateOf(ScreenTab.HOME) }
        var isViewingWebsite by remember { mutableStateOf(false) }

        val cartItemCount by viewModel.cartItemCount.collectAsStateWithLifecycle()

        if (isViewingWebsite) {
          WebsiteScreen(
            viewModel = viewModel,
            onBack = { isViewingWebsite = false }
          )
        } else {
          Scaffold(
            topBar = {
              TopAppBar(
                title = {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Surface(
                      shape = RoundedCornerShape(8.dp),
                      color = CrimsonPrimary,
                      modifier = Modifier.size(34.dp)
                    ) {
                      Box(contentAlignment = Alignment.Center) {
                        Text(
                          text = "M",
                          color = Color.White,
                          fontWeight = FontWeight.Black,
                          fontSize = 18.sp
                        )
                      }
                    }
                    Column {
                      Text(
                        text = "Maturi Restaurant",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                      )
                      Text(
                        text = "Nuzvid • Since 1998",
                        color = SaffronAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                      )
                    }
                  }
                },
                actions = {
                  IconButton(
                    onClick = { viewModel.openRestaurantInMaps(context) },
                    modifier = Modifier.testTag("topbar_maps_action")
                  ) {
                    Icon(
                      imageVector = Icons.Filled.Directions,
                      contentDescription = "Directions in Maps",
                      tint = CrimsonPrimary
                    )
                  }
                  IconButton(
                    onClick = { viewModel.callRestaurant(context) },
                    modifier = Modifier.testTag("topbar_call_action")
                  ) {
                    Icon(
                      imageVector = Icons.Filled.Call,
                      contentDescription = "Call Restaurant",
                      tint = CrimsonPrimary
                    )
                  }
                  IconButton(
                    onClick = { currentTab = ScreenTab.CART },
                    modifier = Modifier.testTag("topbar_cart_action")
                  ) {
                    BadgedBox(
                      badge = {
                        if (cartItemCount > 0) {
                          Badge(
                            containerColor = CrimsonPrimary,
                            contentColor = Color.White
                          ) {
                            Text("$cartItemCount")
                          }
                        }
                      }
                    ) {
                      Icon(
                        imageVector = Icons.Filled.ShoppingBag,
                        contentDescription = "View Cart",
                        tint = if (currentTab == ScreenTab.CART) CrimsonPrimary else MaterialTheme.colorScheme.onSurface
                      )
                    }
                  }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                  containerColor = MaterialTheme.colorScheme.surface
                )
              )
            },
            bottomBar = {
              NavigationBar(
                modifier = Modifier
                  .windowInsetsPadding(WindowInsets.navigationBars)
                  .testTag("main_bottom_nav"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
              ) {
                ScreenTab.values().forEach { tab ->
                  val isSelected = currentTab == tab
                  NavigationBarItem(
                    selected = isSelected,
                    onClick = { currentTab = tab },
                    icon = {
                      if (tab == ScreenTab.CART && cartItemCount > 0) {
                        BadgedBox(
                          badge = {
                            Badge(
                              containerColor = CrimsonPrimary,
                              contentColor = Color.White
                            ) {
                              Text("$cartItemCount")
                            }
                          }
                        ) {
                          Icon(tab.icon, contentDescription = tab.title)
                        }
                      } else {
                        Icon(tab.icon, contentDescription = tab.title)
                      }
                    },
                    label = {
                      Text(
                        text = tab.title,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                      )
                    },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = CrimsonPrimary,
                      selectedTextColor = CrimsonPrimary,
                      indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag(tab.tag)
                  )
                }
              }
            },
            modifier = Modifier.fillMaxSize()
          ) { innerPadding ->
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ) {
              when (currentTab) {
                ScreenTab.HOME -> {
                  HomeScreen(
                    viewModel = viewModel,
                    onNavigateToMenu = { currentTab = ScreenTab.MENU },
                    onNavigateToTableBooking = { currentTab = ScreenTab.BOOK_TABLE },
                    onNavigateToWebsite = { isViewingWebsite = true },
                    onNavigateToCart = { currentTab = ScreenTab.CART }
                  )
                }
                ScreenTab.MENU -> {
                  MenuScreen(
                    viewModel = viewModel,
                    onNavigateToCart = { currentTab = ScreenTab.CART }
                  )
                }
                ScreenTab.BOOK_TABLE -> {
                  TableBookingScreen(
                    viewModel = viewModel
                  )
                }
                ScreenTab.CART -> {
                  CartScreen(
                    viewModel = viewModel,
                    onExploreMenu = { currentTab = ScreenTab.MENU }
                  )
                }
                ScreenTab.ABOUT -> {
                  AboutScreen(
                    viewModel = viewModel,
                    onNavigateToWebsite = { isViewingWebsite = true }
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
