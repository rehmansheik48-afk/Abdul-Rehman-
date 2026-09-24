package com.example.ui.screens.about

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.RestaurantViewModel

@Composable
fun AboutScreen(
  viewModel: RestaurantViewModel,
  onNavigateToWebsite: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val profile = viewModel.restaurantProfile
  val reviews by viewModel.customerReviews.collectAsStateWithLifecycle()

  var showWriteReviewDialog by remember { mutableStateOf(false) }
  var reviewerName by remember { mutableStateOf("") }
  var reviewRating by remember { mutableStateOf(5) }
  var reviewComment by remember { mutableStateOf("") }
  var favDish by remember { mutableStateOf("") }

  if (showWriteReviewDialog) {
    AlertDialog(
      onDismissRequest = { showWriteReviewDialog = false },
      title = { Text("Write a Diner Review", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Rate your experience at Maturi Restaurant:")
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            (1..5).forEach { star ->
              IconButton(
                onClick = { reviewRating = star },
                modifier = Modifier.size(36.dp)
              ) {
                Icon(
                  imageVector = if (star <= reviewRating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                  contentDescription = "$star Stars",
                  tint = if (star <= reviewRating) Color(0xFFFFC107) else Color.Gray
                )
              }
            }
          }

          OutlinedTextField(
            value = reviewerName,
            onValueChange = { reviewerName = it },
            label = { Text("Your Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = favDish,
            onValueChange = { favDish = it },
            label = { Text("Favorite Dish (e.g. Gongura Biryani)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = reviewComment,
            onValueChange = { reviewComment = it },
            label = { Text("Your Review & Feedback") },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.submitReview(
              name = reviewerName,
              rating = reviewRating,
              comment = reviewComment,
              favDish = favDish
            )
            showWriteReviewDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary)
        ) {
          Text("Submit Review")
        }
      },
      dismissButton = {
        TextButton(onClick = { showWriteReviewDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("about_screen_column"),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // 1. Restaurant Header Photo
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_restaurant_hero),
          contentDescription = "Maturi Restaurant Building & Dining Hall",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )
      }
    }

    // 2. Info Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = profile.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = profile.tagline,
            color = CrimsonPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Rating badge
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFFFC107)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Filled.Star,
                  contentDescription = null,
                  tint = Color.Black,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "${profile.rating}",
                  color = Color.Black,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }
            }
            Text(
              text = "${profile.totalReviewsCount}+ Verified Reviews on Google Maps",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

          // Address & Maps
          Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              imageVector = Icons.Filled.LocationOn,
              contentDescription = null,
              tint = CrimsonPrimary,
              modifier = Modifier.size(22.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Address & Location",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
              Text(
                text = profile.address,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp,
                modifier = Modifier.padding(vertical = 2.dp)
              )
              Text(
                text = "Plus Code: ${profile.plusCode}",
                fontSize = 11.sp,
                color = SaffronAmber,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Direction & Call buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { viewModel.openRestaurantInMaps(context) },
              colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("open_in_google_maps_btn")
            ) {
              Icon(Icons.Filled.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Get Directions", fontSize = 12.sp)
            }

            OutlinedButton(
              onClick = { viewModel.callRestaurant(context) },
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("call_restaurant_btn")
            ) {
              Icon(Icons.Filled.Call, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Call Desk", fontSize = 12.sp)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedButton(
            onClick = { viewModel.shareRestaurant(context) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("share_restaurant_btn")
          ) {
            Icon(Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Share Restaurant Details with Friends", fontSize = 12.sp)
          }
        }
      }
    }

    // 3. Timings & Amenities Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Operating Hours",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
          )
          Spacer(modifier = Modifier.height(8.dp))

          TimingRow("Morning Tiffins & Breakfast", profile.breakfastTimings)
          TimingRow("Lunch & Famous Dum Biryanis", profile.lunchTimings)
          TimingRow("Dinner & Family Starters", profile.dinnerTimings)
          TimingRow("All Days", "Monday through Sunday (7 Days Open)")

          HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

          Text(
            text = "Hotel Maturi & Banquet Facilities",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Visiting Nuzvid for RGUKT IIIT campus, agricultural business, or local events? Hotel Maturi provides well-appointed AC lodging rooms, 24/7 power backup, fast room service, and banquet facilities for wedding receptions and corporate events.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 16.sp
          )
        }
      }
    }

    // 4. Client Website Access Card
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .clickable { onNavigateToWebsite() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CrimsonContainer)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Client Web Portal",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = OnCrimsonContainer
            )
            Text(
              text = "Explore the responsive web application built for Maturi Restaurant clients and customers.",
              fontSize = 12.sp,
              color = OnCrimsonContainer.copy(alpha = 0.85f)
            )
          }
          Icon(
            imageVector = Icons.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = CrimsonPrimary,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }

    // 5. Customer Reviews Section
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Diner Reviews",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Real feedback from Nuzvid diners",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Button(
          onClick = { showWriteReviewDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          modifier = Modifier.testTag("write_review_btn")
        ) {
          Icon(Icons.Filled.RateReview, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Add Review", fontSize = 12.sp)
        }
      }
      Spacer(modifier = Modifier.height(8.dp))
    }

    items(reviews) { review ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = SaffronContainer,
                modifier = Modifier.size(32.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = review.reviewerName.take(1).uppercase(),
                    fontWeight = FontWeight.Bold,
                    color = OnSaffronContainer
                  )
                }
              }
              Column {
                Text(
                  text = review.reviewerName,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
                Text(
                  text = review.dateFormatted,
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            // Stars
            Row {
              repeat(review.rating) {
                Icon(
                  imageVector = Icons.Filled.Star,
                  contentDescription = null,
                  tint = Color(0xFFFFC107),
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = review.comment,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 16.sp
          )

          if (review.favoriteDish.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MaterialTheme.colorScheme.surfaceVariant
            ) {
              Text(
                text = "Favorite: ${review.favoriteDish}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = CrimsonPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun TimingRow(label: String, time: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = time,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}
