package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.MenuItem
import com.example.ui.theme.*

@Composable
fun VegNonVegBadge(
  isVeg: Boolean,
  modifier: Modifier = Modifier,
  size: Int = 16
) {
  val borderColor = if (isVeg) Color(0xFF2E7D32) else Color(0xFFC62828)
  val dotColor = if (isVeg) Color(0xFF2E7D32) else Color(0xFFC62828)

  Box(
    modifier = modifier
      .size(size.dp)
      .border(1.5.dp, borderColor, RoundedCornerShape(3.dp))
      .padding(2.5.dp),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size((size * 0.55).dp)
        .background(dotColor, CircleShape)
    )
  }
}

@Composable
fun SpiceLevelIndicator(
  level: Int,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(2.dp)
  ) {
    repeat(level.coerceIn(1, 3)) {
      Icon(
        imageVector = Icons.Filled.LocalFireDepartment,
        contentDescription = "Spicy level $level",
        tint = Color(0xFFE65100),
        modifier = Modifier.size(14.dp)
      )
    }
  }
}

@Composable
fun FoodItemCard(
  item: MenuItem,
  cartQuantity: Int,
  isFavorite: Boolean,
  onAddToCart: () -> Unit,
  onDecrement: () -> Unit,
  onToggleFavorite: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("food_item_${item.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Dish Header / Image
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(150.dp)
      ) {
        val imageId = if (item.imageRes != 0) item.imageRes else R.drawable.img_restaurant_hero
        androidx.compose.foundation.Image(
          painter = painterResource(id = imageId),
          contentDescription = item.name,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Gradient scrim for contrast
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              androidx.compose.ui.graphics.Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
              )
            )
        )

        // Top Row: Veg/Non-Veg & BestSeller Badge + Favorite Button
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color.White.copy(alpha = 0.95f),
              shadowElevation = 2.dp
            ) {
              Box(modifier = Modifier.padding(4.dp)) {
                VegNonVegBadge(isVeg = item.isVeg)
              }
            }

            if (item.isBestSeller) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = CrimsonPrimary,
                shadowElevation = 2.dp
              ) {
                Text(
                  text = "★ BESTSELLER",
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
              }
            }
          }

          IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier
              .size(34.dp)
              .background(Color.White.copy(alpha = 0.85f), CircleShape)
              .testTag("fav_button_${item.id}")
          ) {
            Icon(
              imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = "Favorite",
              tint = if (isFavorite) CrimsonPrimary else Color.DarkGray,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        // Bottom on-image badges: Price and Spice
        Row(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(alpha = 0.75f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = "₹${item.price.toInt()}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(alpha = 0.75f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "${item.rating}",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }

      // Content Details
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = item.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
          )
          if (item.spiceLevel > 0) {
            SpiceLevelIndicator(level = item.spiceLevel, modifier = Modifier.padding(start = 6.dp))
          }
        }

        if (item.teluguName.isNotBlank()) {
          Text(
            text = item.teluguName,
            color = SaffronAmber,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 2.dp)
          )
        }

        Text(
          text = item.description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.padding(vertical = 6.dp)
        )

        // Action Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = item.availableTimings,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
          )

          if (cartQuantity == 0) {
            Button(
              onClick = onAddToCart,
              modifier = Modifier
                .height(38.dp)
                .testTag("add_button_${item.id}"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
              contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            ) {
              Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "ADD",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
          } else {
            // Counter Stepper
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = CrimsonContainer,
              border = BorderStroke(1.dp, CrimsonPrimary)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              ) {
                IconButton(
                  onClick = onDecrement,
                  modifier = Modifier
                    .size(32.dp)
                    .testTag("decrement_${item.id}")
                ) {
                  Icon(
                    imageVector = if (cartQuantity == 1) Icons.Filled.DeleteOutline else Icons.Filled.Remove,
                    contentDescription = "Decrease",
                    tint = CrimsonPrimary,
                    modifier = Modifier.size(16.dp)
                  )
                }

                Text(
                  text = "$cartQuantity",
                  fontWeight = FontWeight.Bold,
                  color = OnCrimsonContainer,
                  fontSize = 14.sp,
                  modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                  onClick = onAddToCart,
                  modifier = Modifier
                    .size(32.dp)
                    .testTag("increment_${item.id}")
                ) {
                  Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Increase",
                    tint = CrimsonPrimary,
                    modifier = Modifier.size(16.dp)
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

@Composable
fun SectionHeader(
  title: String,
  subtitle: String? = null,
  actionText: String? = null,
  onActionClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      if (subtitle != null) {
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    if (actionText != null && onActionClick != null) {
      TextButton(
        onClick = onActionClick,
        modifier = Modifier.testTag("section_action_${title.lowercase().replace(" ", "_")}")
      ) {
        Text(
          text = actionText,
          color = CrimsonPrimary,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp
        )
      }
    }
  }
}
