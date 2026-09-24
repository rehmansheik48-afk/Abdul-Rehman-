package com.example.ui.screens.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CartItem
import com.example.data.model.OrderRecord
import com.example.ui.components.VegNonVegBadge
import com.example.ui.theme.*
import com.example.viewmodel.RestaurantViewModel

@Composable
fun CartScreen(
  viewModel: RestaurantViewModel,
  onExploreMenu: () -> Unit,
  modifier: Modifier = Modifier
) {
  val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
  val subtotal by viewModel.cartSubtotal.collectAsStateWithLifecycle()
  val appliedCoupon by viewModel.appliedCoupon.collectAsStateWithLifecycle()
  val orders by viewModel.orders.collectAsStateWithLifecycle()
  val orderSuccessDialog by viewModel.orderSuccessDialog.collectAsStateWithLifecycle()

  var selectedOrderType by remember { mutableStateOf("Home Delivery") } // "Home Delivery", "Takeaway / Parcel", "Dine-in Order"
  var customerName by remember { mutableStateOf("") }
  var customerPhone by remember { mutableStateOf("") }
  var addressOrTable by remember { mutableStateOf("") }
  var paymentMethod by remember { mutableStateOf("UPI / PhonePe / GPay") }
  var couponInput by remember { mutableStateOf("") }
  var couponError by remember { mutableStateOf<String?>(null) }

  val taxGst = subtotal * 0.05
  val deliveryFee = if (selectedOrderType == "Home Delivery" && subtotal > 0) 40.0 else 0.0
  val discount = when (appliedCoupon) {
    "MATURI50" -> 50.0
    "WELCOME100" -> 100.0
    "BIRYANI20" -> subtotal * 0.20
    else -> 0.0
  }.coerceAtMost(subtotal)
  val grandTotal = (subtotal + taxGst + deliveryFee - discount).coerceAtLeast(0.0)

  // Order Success Dialog
  orderSuccessDialog?.let { placedOrder ->
    AlertDialog(
      onDismissRequest = { viewModel.dismissOrderDialog() },
      icon = {
        Surface(
          shape = CircleShape,
          color = SpiceGreenContainer,
          modifier = Modifier.size(56.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Filled.CheckCircle,
              contentDescription = null,
              tint = SpiceGreen,
              modifier = Modifier.size(36.dp)
            )
          }
        }
      },
      title = {
        Text(
          text = "Order Received!",
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.titleLarge
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "Your order #${placedOrder.orderNumber} has been sent to Maturi Restaurant kitchen in Nuzvid.",
            style = MaterialTheme.typography.bodyMedium
          )
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "Estimated Time: 25-35 mins",
                fontWeight = FontWeight.Bold,
                color = CrimsonPrimary,
                fontSize = 13.sp
              )
              Text(
                text = "Type: ${placedOrder.orderType}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "Destination: ${placedOrder.addressOrTable}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "Total Paid/Due: ₹${placedOrder.grandTotal.toInt()} (${placedOrder.paymentMethod})",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { viewModel.dismissOrderDialog() },
          colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
          modifier = Modifier.testTag("order_success_confirm_btn")
        ) {
          Text("Track Order")
        }
      }
    )
  }

  if (cartItems.isEmpty() && orders.isEmpty()) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(32.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Surface(
          shape = CircleShape,
          color = CrimsonContainer,
          modifier = Modifier.size(80.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Outlined.ShoppingBag,
              contentDescription = null,
              tint = CrimsonPrimary,
              modifier = Modifier.size(40.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "Your cart is empty",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Explore authentic biryanis, spicy starters, and tiffins from Maturi Restaurant.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
          onClick = onExploreMenu,
          colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("explore_menu_from_empty_cart")
        ) {
          Icon(Icons.Filled.RestaurantMenu, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Browse Menu")
        }
      }
    }
  } else {
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .testTag("cart_screen_lazy_column"),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Order Type Selection
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Choose Dining / Delivery Mode",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              listOf("Home Delivery", "Takeaway / Parcel", "Dine-in Order").forEach { type ->
                val isSelected = selectedOrderType == type
                Button(
                  onClick = { selectedOrderType = type },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) CrimsonPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                  ),
                  shape = RoundedCornerShape(8.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                  modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .testTag("order_type_${type.take(4).lowercase()}")
                ) {
                  Text(
                    text = type,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1
                  )
                }
              }
            }
          }
        }
      }

      // 2. Cart Items
      if (cartItems.isNotEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Order Items (${cartItems.sumOf { it.quantity }})",
                  fontWeight = FontWeight.Bold,
                  style = MaterialTheme.typography.titleMedium
                )
                TextButton(
                  onClick = { viewModel.clearCart() },
                  modifier = Modifier.testTag("clear_cart_btn")
                ) {
                  Text("Clear All", color = CrimsonPrimary, fontSize = 12.sp)
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              cartItems.forEachIndexed { index, item ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                  ) {
                    VegNonVegBadge(isVeg = item.isVeg, size = 14)
                    Column {
                      Text(
                        text = item.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        maxLines = 1
                      )
                      Text(
                        text = "₹${item.price.toInt()} each",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }
                  }

                  // Stepper
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CrimsonContainer,
                    border = BorderStroke(1.dp, CrimsonPrimary)
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp)
                    ) {
                      IconButton(
                        onClick = { viewModel.updateCartQuantity(item.id, item.quantity - 1) },
                        modifier = Modifier.size(28.dp)
                      ) {
                        Icon(
                          imageVector = if (item.quantity == 1) Icons.Filled.DeleteOutline else Icons.Filled.Remove,
                          contentDescription = "Minus",
                          tint = CrimsonPrimary,
                          modifier = Modifier.size(14.dp)
                        )
                      }

                      Text(
                        text = "${item.quantity}",
                        fontWeight = FontWeight.Bold,
                        color = OnCrimsonContainer,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 6.dp)
                      )

                      IconButton(
                        onClick = { viewModel.updateCartQuantity(item.id, item.quantity + 1) },
                        modifier = Modifier.size(28.dp)
                      ) {
                        Icon(
                          imageVector = Icons.Filled.Add,
                          contentDescription = "Plus",
                          tint = CrimsonPrimary,
                          modifier = Modifier.size(14.dp)
                        )
                      }
                    }
                  }

                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                    text = "₹${(item.price * item.quantity).toInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  )
                }

                if (index < cartItems.size - 1) {
                  HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
              }
            }
          }
        }

        // 3. Customer & Address Details
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Contact & Delivery Details",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
              )
              Spacer(modifier = Modifier.height(10.dp))

              OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Your Name") },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("cart_customer_name_input")
              )

              Spacer(modifier = Modifier.height(8.dp))

              OutlinedTextField(
                value = customerPhone,
                onValueChange = { customerPhone = it },
                label = { Text("Phone Number") },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("cart_customer_phone_input")
              )

              Spacer(modifier = Modifier.height(8.dp))

              val labelText = when (selectedOrderType) {
                "Home Delivery" -> "Delivery Address (Nuzvid)"
                "Dine-in Order" -> "Table Number (e.g. Table 4, AC Hall)"
                else -> "Pickup Note (e.g. 15 mins pickup)"
              }

              OutlinedTextField(
                value = addressOrTable,
                onValueChange = { addressOrTable = it },
                label = { Text(labelText) },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("cart_address_input")
              )
            }
          }
        }

        // 4. Coupons & Offers
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Have a Coupon?",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
              )
              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                OutlinedTextField(
                  value = couponInput,
                  onValueChange = {
                    couponInput = it
                    couponError = null
                  },
                  placeholder = { Text("e.g. MATURI50", fontSize = 13.sp) },
                  singleLine = true,
                  modifier = Modifier
                    .weight(1f)
                    .testTag("coupon_input")
                )

                Button(
                  onClick = {
                    if (viewModel.applyCoupon(couponInput)) {
                      couponError = null
                    } else {
                      couponError = "Invalid code. Try MATURI50"
                    }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                  modifier = Modifier.testTag("apply_coupon_btn")
                ) {
                  Text("Apply")
                }
              }

              if (appliedCoupon != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "✓ Coupon '$appliedCoupon' Applied!",
                    color = SpiceGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  )
                  TextButton(onClick = { viewModel.removeCoupon() }) {
                    Text("Remove", color = CrimsonPrimary, fontSize = 11.sp)
                  }
                }
              } else if (couponError != null) {
                Text(
                  text = couponError!!,
                  color = MaterialTheme.colorScheme.error,
                  fontSize = 12.sp,
                  modifier = Modifier.padding(top = 4.dp)
                )
              }
            }
          }
        }

        // 5. Payment Method & Bill Summary
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Payment Method",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
              )
              Spacer(modifier = Modifier.height(8.dp))

              listOf("UPI / PhonePe / GPay", "Cash on Delivery", "Debit / Credit Card").forEach { method ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  RadioButton(
                    selected = paymentMethod == method,
                    onClick = { paymentMethod = method }
                  )
                  Text(text = method, fontSize = 13.sp)
                }
              }

              HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

              Text(
                text = "Bill Summary",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
              )

              Spacer(modifier = Modifier.height(8.dp))

              BillRow("Item Subtotal", "₹${subtotal.toInt()}")
              BillRow("GST Taxes (5%)", "₹${taxGst.toInt()}")
              if (deliveryFee > 0) {
                BillRow("Delivery Partner Fee", "₹${deliveryFee.toInt()}")
              }
              if (discount > 0) {
                BillRow("Coupon Discount", "- ₹${discount.toInt()}", isGreen = true)
              }

              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Grand Total",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.ExtraBold
                )
                Text(
                  text = "₹${grandTotal.toInt()}",
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.ExtraBold,
                  color = CrimsonPrimary
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Button(
                onClick = {
                  viewModel.placeOrder(
                    orderType = selectedOrderType,
                    customerName = customerName,
                    phone = customerPhone,
                    addressOrTable = addressOrTable,
                    paymentMethod = paymentMethod
                  )
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("place_order_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                shape = RoundedCornerShape(12.dp)
              ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "PLACE ORDER • ₹${grandTotal.toInt()}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                )
              }
            }
          }
        }
      }

      // 6. Recent Orders Tracking
      if (orders.isNotEmpty()) {
        item {
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Your Recent Orders",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        items(orders) { order ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("order_record_${order.orderNumber}")
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "#${order.orderNumber}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = CrimsonPrimary
                ) {
                  Text(
                    text = order.status,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }
              }

              Text(
                text = order.itemsSummary,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 4.dp)
              )

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Total: ₹${order.grandTotal.toInt()}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
                Text(
                  text = order.orderType,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun BillRow(label: String, value: String, isGreen: Boolean = false) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 2.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      fontSize = 13.sp,
      color = if (isGreen) SpiceGreen else MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = value,
      fontSize = 13.sp,
      fontWeight = FontWeight.SemiBold,
      color = if (isGreen) SpiceGreen else MaterialTheme.colorScheme.onSurface
    )
  }
}
