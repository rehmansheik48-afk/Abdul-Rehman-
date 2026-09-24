package com.example.ui.screens.menu

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MenuCategory
import com.example.ui.components.FoodItemCard
import com.example.ui.components.VegNonVegBadge
import com.example.ui.theme.*
import com.example.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
  viewModel: RestaurantViewModel,
  onNavigateToCart: () -> Unit,
  modifier: Modifier = Modifier
) {
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
  val vegOnlyFilter by viewModel.vegOnlyFilter.collectAsStateWithLifecycle()
  val filteredDishes by viewModel.filteredMenu.collectAsStateWithLifecycle()
  val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
  val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("menu_screen_root")
  ) {
    // 1. Search Bar
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      shape = RoundedCornerShape(12.dp),
      color = MaterialTheme.colorScheme.surfaceVariant,
      shadowElevation = 1.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Filled.Search,
          contentDescription = "Search",
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
          value = searchQuery,
          onValueChange = { viewModel.onSearchQueryChanged(it) },
          placeholder = { Text("Search biryani, tiffins, cashew fry...", fontSize = 14.sp) },
          colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
          ),
          singleLine = true,
          modifier = Modifier
            .weight(1f)
            .testTag("menu_search_input")
        )
        if (searchQuery.isNotBlank()) {
          IconButton(
            onClick = { viewModel.onSearchQueryChanged("") },
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Filled.Close,
              contentDescription = "Clear",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // 2. Category Chips & Veg Only Toggle
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Pure Veg toggle button
      item {
        FilterChip(
          selected = vegOnlyFilter,
          onClick = { viewModel.toggleVegFilter() },
          label = {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              VegNonVegBadge(isVeg = true, size = 12)
              Text("Veg Only", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SpiceGreenContainer,
            selectedLabelColor = SpiceGreen
          ),
          modifier = Modifier.testTag("veg_filter_chip")
        )
      }

      // Categories
      items(MenuCategory.values()) { category ->
        val isSelected = selectedCategory == category
        FilterChip(
          selected = isSelected,
          onClick = { viewModel.onCategorySelected(category) },
          label = {
            Text(
              text = category.displayName,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = CrimsonPrimary,
            selectedLabelColor = Color.White
          ),
          modifier = Modifier.testTag("category_chip_${category.name.lowercase()}")
        )
      }
    }

    // 3. Results Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${filteredDishes.size} items in ${selectedCategory.displayName}",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // 4. Menu Items List
    if (filteredDishes.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Filled.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No dishes found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Try searching for 'biryani', 'kaju', or reset filters",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = {
              viewModel.onSearchQueryChanged("")
              viewModel.onCategorySelected(MenuCategory.ALL)
              if (vegOnlyFilter) viewModel.toggleVegFilter()
            },
            colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary)
          ) {
            Text("Reset All Filters")
          }
        }
      }
    } else {
      LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.testTag("menu_list_lazy_column")
      ) {
        items(filteredDishes, key = { it.id }) { dish ->
          val qty = cartItems.find { it.menuItemId == dish.id }?.quantity ?: 0
          val isFav = favoriteIds.contains(dish.id)

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
    }
  }
}
