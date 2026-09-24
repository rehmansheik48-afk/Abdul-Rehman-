package com.example.ui.screens.table

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TableBooking
import com.example.ui.theme.*
import com.example.viewmodel.RestaurantViewModel

@Composable
fun TableBookingScreen(
  viewModel: RestaurantViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val existingBookings by viewModel.tableBookings.collectAsStateWithLifecycle()
  val bookingSuccessDialog by viewModel.bookingSuccessDialog.collectAsStateWithLifecycle()

  var partySize by remember { mutableStateOf(4) }
  var selectedDate by remember { mutableStateOf("Today") }
  var selectedTimeSlot by remember { mutableStateOf("7:30 PM (Dinner)") }
  var selectedSeating by remember { mutableStateOf("AC Family Dining") }
  var guestName by remember { mutableStateOf("") }
  var guestPhone by remember { mutableStateOf("") }
  var specialRequest by remember { mutableStateOf("") }

  val dateOptions = listOf("Today", "Tomorrow", "Weekend (Saturday)", "Next Sunday")
  val lunchSlots = listOf("12:30 PM", "1:15 PM", "2:00 PM", "2:45 PM")
  val dinnerSlots = listOf("7:00 PM", "7:45 PM", "8:30 PM", "9:15 PM", "10:00 PM")
  val seatingOptions = listOf("AC Family Dining", "Main Hall", "Private Banquet")

  // Success Dialog
  bookingSuccessDialog?.let { booked ->
    AlertDialog(
      onDismissRequest = { viewModel.dismissBookingDialog() },
      icon = {
        Surface(
          shape = CircleShape,
          color = SpiceGreenContainer,
          modifier = Modifier.size(56.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Filled.TableRestaurant,
              contentDescription = null,
              tint = SpiceGreen,
              modifier = Modifier.size(32.dp)
            )
          }
        }
      },
      title = {
        Text("Table Reserved Successfully!", fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text("Reservation Ref: #${booked.bookingRef}", fontWeight = FontWeight.Bold, color = CrimsonPrimary)
          Text("Guest Name: ${booked.customerName}")
          Text("Party Size: ${booked.partySize} Guests")
          Text("Time & Date: ${booked.timeSlot} • ${booked.bookingDate}")
          Text("Section: ${booked.seatingArea}")
          Text(
            text = "Your table at Maturi Restaurant will be reserved for you. Please arrive 10 minutes prior.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      confirmButton = {
        Button(
          onClick = { viewModel.dismissBookingDialog() },
          colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
          modifier = Modifier.testTag("dismiss_booking_dialog_btn")
        ) {
          Text("Great, View Bookings")
        }
      }
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("table_booking_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CrimsonPrimary),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              imageVector = Icons.Filled.TableBar,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(28.dp)
            )
            Column {
              Text(
                text = "Reserve a Table",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Maturi Restaurant & AC Family Hall • Nuzvid",
                color = CrimsonContainer,
                fontSize = 12.sp
              )
            }
          }
        }
      }
    }

    // Party Size Selector
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Party Size (Number of Guests)",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
          )
          Spacer(modifier = Modifier.height(10.dp))
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf(1, 2, 3, 4, 5, 6, 8, 10, 12, 15)) { size ->
              val isSelected = partySize == size
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) CrimsonPrimary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .clickable { partySize = size }
                  .testTag("guest_size_$size")
              ) {
                Text(
                  text = "$size",
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
              }
            }
          }
        }
      }
    }

    // Date & Seating Area
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Select Date",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
          )
          Spacer(modifier = Modifier.height(8.dp))
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(dateOptions) { date ->
              val isSelected = selectedDate == date
              FilterChip(
                selected = isSelected,
                onClick = { selectedDate = date },
                label = { Text(date) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = CrimsonPrimary,
                  selectedLabelColor = Color.White
                ),
                modifier = Modifier.testTag("date_chip_${date.take(4).lowercase()}")
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Dining Area Preference",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            seatingOptions.forEach { area ->
              val isSelected = selectedSeating == area
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) CrimsonContainer else MaterialTheme.colorScheme.surfaceVariant,
                border = if (isSelected) BorderStroke(1.5.dp, CrimsonPrimary) else null,
                modifier = Modifier
                  .weight(1f)
                  .clickable { selectedSeating = area }
                  .testTag("seating_${area.take(4).lowercase()}")
              ) {
                Text(
                  text = area,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) OnCrimsonContainer else MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                  textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
              }
            }
          }
        }
      }
    }

    // Time Slot
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Lunch Slots (12:00 PM – 3:30 PM)",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(6.dp))
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(lunchSlots) { slot ->
              val fullSlot = "$slot (Lunch)"
              val isSelected = selectedTimeSlot == fullSlot
              FilterChip(
                selected = isSelected,
                onClick = { selectedTimeSlot = fullSlot },
                label = { Text(slot) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = SaffronAmber,
                  selectedLabelColor = Color.White
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Dinner Slots (7:00 PM – 10:30 PM)",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(6.dp))
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(dinnerSlots) { slot ->
              val fullSlot = "$slot (Dinner)"
              val isSelected = selectedTimeSlot == fullSlot
              FilterChip(
                selected = isSelected,
                onClick = { selectedTimeSlot = fullSlot },
                label = { Text(slot) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = CrimsonPrimary,
                  selectedLabelColor = Color.White
                )
              )
            }
          }
        }
      }
    }

    // Guest Info & Submit
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Contact Details",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
          )
          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = guestName,
            onValueChange = { guestName = it },
            label = { Text("Lead Guest Name") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("booking_guest_name")
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = guestPhone,
            onValueChange = { guestPhone = it },
            label = { Text("Contact Phone Number") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("booking_guest_phone")
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = specialRequest,
            onValueChange = { specialRequest = it },
            label = { Text("Special Requests (Birthday, high chair, etc.)") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("booking_special_request")
          )

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = {
              viewModel.submitTableBooking(
                name = guestName,
                phone = guestPhone,
                partySize = partySize,
                dateStr = selectedDate,
                timeSlot = selectedTimeSlot,
                seatingArea = selectedSeating,
                specialRequest = specialRequest
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("confirm_table_reservation_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Filled.EventSeat, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("CONFIRM TABLE RESERVATION", fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Past / Upcoming Bookings list
    if (existingBookings.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Your Bookings (${existingBookings.size})",
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.titleMedium
        )
      }

      items(existingBookings) { booking ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("booking_card_${booking.bookingRef}")
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Ref: #${booking.bookingRef}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (booking.status == "Confirmed") SpiceGreen else MaterialTheme.colorScheme.error
              ) {
                Text(
                  text = booking.status,
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${booking.partySize} Guests • ${booking.bookingDate} at ${booking.timeSlot}",
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp
            )
            Text(
              text = "Area: ${booking.seatingArea} • Booked for: ${booking.customerName}",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (booking.status == "Confirmed") {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
              ) {
                TextButton(
                  onClick = { viewModel.cancelBooking(booking.id) },
                  modifier = Modifier.testTag("cancel_booking_${booking.id}")
                ) {
                  Text("Cancel Booking", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
              }
            }
          }
        }
      }
    }
  }
}
