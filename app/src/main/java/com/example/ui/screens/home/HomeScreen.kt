package com.example.ui.screens.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.CartItem
import com.example.data.model.MenuCategory
import com.example.data.model.MenuItem
import com.example.ui.components.FoodItemCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.RestaurantViewModel

@Composable
fun HomeScreen(
  viewModel: RestaurantViewModel,
  onNavigateToMenu: () -> Unit,
  onNavigateToTableBooking: () -> Unit,
  onNavigateToWebsite: () -> Unit,
  onNavigateToCart: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
  val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

  val bestSellers = viewModel.fullMenuList.filter { it.isBestSeller }
  val biryanis = viewModel.fullMenuList.filter { it.category == MenuCategory.BIRYANI }
  val tiffins = viewModel.fullMenuList.filter { it.category == MenuCategory.TIFFINS }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("home_screen_column"),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // 1. HERO BANNER
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_restaurant_hero),
          contentDescription = "Maturi Restaurant Ambiance",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Black.copy(alpha = 0.35f),
                  Color.Black.copy(alpha = 0.85f)
                )
              )
            )
        )

        // Banner Content
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp)
        ) {
          // Status Chip
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = SpiceGreen,
            shadowElevation = 4.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(Color.White, CircleShape)
              )
              Text(
                text = "OPEN NOW • 6:00 AM – 10:30 PM",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Maturi Restaurant & Hotel",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
          )

          Text(
            text = "మాతూరి రెస్టారెంట్ • Nuzvid, Andhra Pradesh",
            color = SaffronAmberDark,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFFFC107)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
              ) {
                Icon(
                  imageVector = Icons.Filled.Star,
                  contentDescription = null,
                  tint = Color.Black,
                  modifier = Modifier.size(13.dp)
                )
                Text(
                  text = "4.3",
                  color = Color.Black,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
            }

            Text(
              text = "528+ Reviews on Maps",
              color = Color.White.copy(alpha = 0.9f),
              fontSize = 12.sp
            )

            Text(
              text = "•",
              color = Color.White.copy(alpha = 0.6f)
            )

            Text(
              text = "Bandar Road, Nuzvid",
              color = Color.White.copy(alpha = 0.9f),
              fontSize = 12.sp
            )
          }
        }
      }
    }

    // 2. QUICK ACTION TILES
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          QuickActionButton(
            icon = Icons.Filled.Directions,
            label = "Directions",
            subLabel = "Google Maps",
            onClick = { viewModel.openRestaurantInMaps(context) },
            tag = "quick_action_directions"
          )
          QuickActionButton(
            icon = Icons.Filled.Phone,
            label = "Call Us",
            subLabel = "+91 86562...",
            onClick = { viewModel.callRestaurant(context) },
            tag = "quick_action_call"
          )
          QuickActionButton(
            icon = Icons.Filled.TableRestaurant,
            label = "Book Table",
            subLabel = "Reserve AC Hall",
            onClick = onNavigateToTableBooking,
            tag = "quick_action_table"
          )
          QuickActionButton(
            icon = Icons.Filled.Language,
            label = "Website",
            subLabel = "Client Portal",
            onClick = onNavigateToWebsite,
            tag = "quick_action_website"
          )
        }
      }
    }

    // 3. SPECIAL PROMO BANNER
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .clickable { viewModel.applyCoupon("MATURI50") }
          .testTag("coupon_promo_banner"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CrimsonContainer)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = CrimsonPrimary,
              modifier = Modifier.size(42.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Filled.LocalOffer,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
            Column {
              Text(
                text = "Use Code MATURI50",
                fontWeight = FontWeight.Bold,
                color = OnCrimsonContainer,
                fontSize = 15.sp
              )
              Text(
                text = "Get ₹50 OFF on food orders above ₹299",
                color = OnCrimsonContainer.copy(alpha = 0.8f),
                fontSize = 12.sp
              )
            }
          }

          Button(
            onClick = { viewModel.applyCoupon("MATURI50") },
            colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
            modifier = Modifier.height(34.dp)
          ) {
            Text("APPLY", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // 4. RESTAURANT HIGHLIGHTS / CUISINE BADGES
    item {
      Spacer(modifier = Modifier.height(16.dp))
      SectionHeader(
        title = "Nuzvid Specialties",
        subtitle = "What makes Maturi Restaurant iconic in Krishna district"
      )

      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        item {
          HighlightCard(
            title = "Legendary Dum Biryani",
            desc = "Aromatic long-grain basmati with Andhra spices",
            imageRes = R.drawable.img_signature_biryani,
            onClick = onNavigateToMenu
          )
        }
        item {
          HighlightCard(
            title = "Maturi Cashew Fry",
            desc = "Crispy chicken/paneer loaded with roasted cashews",
            imageRes = R.drawable.img_andhra_starters,
            onClick = onNavigateToMenu
          )
        }
        item {
          HighlightCard(
            title = "Fresh Morning Tiffins",
            desc = "Desi Ghee Karam Dosa, Idli Sambar & MLA Pesarattu",
            imageRes = R.drawable.img_restaurant_hero,
            onClick = onNavigateToMenu
          )
        }
      }
    }

    // 5. BEST SELLERS SECTION
    item {
      Spacer(modifier = Modifier.height(16.dp))
      SectionHeader(
        title = "Crowd Favorites",
        subtitle = "Most ordered dishes by our regular diners",
        actionText = "Full Menu",
        onActionClick = onNavigateToMenu
      )
    }

    items(bestSellers) { dish ->
      val qty = cartItems.find { it.menuItemId == dish.id }?.quantity ?: 0
      val isFav = favoriteIds.contains(dish.id)

      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        FoodItemCard(
          item = dish,
          cartQuantity = qty,
          isFavorite = isFav,
          onAddToCart = { viewModel.addToCart(dish) },
          onDecrement = { viewModel.decrementCart(dish.id) },
          onToggleFavorite = { viewModel.toggleFavorite(dish.id) }
        )
      }
    }

    // 6. CLIENT WEBSITE TEASER BANNER
    item {
      Spacer(modifier = Modifier.height(20.dp))
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .clickable { onNavigateToWebsite() }
          .testTag("website_teaser_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = CrimsonPrimary
            ) {
              Text(
                text = "CLIENT WEBSITE READY",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
            Icon(
              imageVector = Icons.Filled.OpenInBrowser,
              contentDescription = "Open Website",
              tint = CrimsonPrimary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Official Maturi Restaurant Website",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "We built a matching modern responsive website for your client with table reservation, live digital menu, and Google Maps integration.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 4.dp)
          )

          Button(
            onClick = onNavigateToWebsite,
            colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .align(Alignment.End)
              .testTag("open_website_preview_btn")
          ) {
            Text("Preview Website")
          }
        }
      }
    }
  }
}

@Composable
fun QuickActionButton(
  icon: ImageVector,
  label: String,
  subLabel: String,
  onClick: () -> Unit,
  tag: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(4.dp)
      .testTag(tag)
  ) {
    Surface(
      shape = CircleShape,
      color = CrimsonContainer,
      modifier = Modifier.size(48.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = CrimsonPrimary,
          modifier = Modifier.size(24.dp)
        )
      }
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = label,
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = subLabel,
      fontSize = 10.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
fun HighlightCard(
  title: String,
  desc: String,
  imageRes: Int,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .width(220.dp)
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column {
      Image(
        painter = painterResource(id = imageRes),
        contentDescription = title,
        modifier = Modifier
          .fillMaxWidth()
          .height(100.dp),
        contentScale = ContentScale.Crop
      )
      Column(modifier = Modifier.padding(10.dp)) {
        Text(
          text = title,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          maxLines = 1
        )
        Text(
          text = desc,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2,
          lineHeight = 14.sp
        )
      }
    }
  }
}
