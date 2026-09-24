package com.example.ui.screens.website

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.*
import com.example.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebsiteScreen(
  viewModel: RestaurantViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val profile = viewModel.restaurantProfile
  var isWebViewLoading by remember { mutableStateOf(true) }

  val clientWebsiteHtml = remember {
    """
    <!DOCTYPE html>
    <html lang="en">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Maturi Restaurant & Hotel | Nuzvid</title>
      <style>
        :root {
          --primary: #9E1B1B;
          --primary-dark: #6C0E0E;
          --amber: #C06B00;
          --green: #238241;
          --bg: #FCF8F5;
          --card: #FFFFFF;
          --text: #1E1B19;
          --muted: #6C645E;
        }
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; }
        body { background: var(--bg); color: var(--text); line-height: 1.5; padding-bottom: 50px; }
        
        header { background: linear-gradient(135deg, var(--primary-dark), var(--primary)); color: white; padding: 24px 16px; text-align: center; }
        header h1 { font-size: 24px; font-weight: 800; }
        header p { color: #FFDAD6; font-size: 13px; margin-top: 4px; }
        .tagline { color: #FFDDB8; font-size: 14px; font-weight: 600; margin-top: 6px; }
        
        .badge-row { display: flex; justify-content: center; gap: 8px; margin-top: 12px; flex-wrap: wrap; }
        .badge { background: rgba(255,255,255,0.2); padding: 4px 10px; border-radius: 20px; font-size: 11px; font-weight: 600; }
        .badge.open { background: var(--green); color: white; }
        .badge.star { background: #FFC107; color: black; }
        
        .container { max-width: 800px; margin: 0 auto; padding: 16px; }
        
        .actions-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 8px; margin: 16px 0; }
        .action-card { background: var(--card); border-radius: 12px; padding: 12px 6px; text-align: center; text-decoration: none; color: var(--text); box-shadow: 0 2px 6px rgba(0,0,0,0.06); font-size: 11px; font-weight: 600; }
        .action-card .icon { font-size: 20px; display: block; margin-bottom: 4px; }
        
        .card { background: var(--card); border-radius: 14px; padding: 16px; margin-bottom: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
        .card h2 { font-size: 17px; margin-bottom: 10px; color: var(--primary); display: flex; align-items: center; justify-content: space-between; }
        
        .menu-list { display: flex; flex-direction: column; gap: 12px; }
        .menu-item { display: flex; justify-content: space-between; border-bottom: 1px solid #F0E6DF; padding-bottom: 8px; }
        .menu-name { font-weight: 700; font-size: 14px; }
        .menu-desc { font-size: 12px; color: var(--muted); }
        .menu-price { font-weight: 800; color: var(--primary); font-size: 14px; }
        
        .btn-primary { display: block; width: 100%; background: var(--primary); color: white; text-align: center; padding: 12px; border-radius: 8px; font-weight: bold; text-decoration: none; margin-top: 10px; border: none; cursor: pointer; }
        
        .footer { text-align: center; font-size: 12px; color: var(--muted); margin-top: 24px; padding: 16px; border-top: 1px solid #E5DCD5; }
      </style>
    </head>
    <body>
      <header>
        <span class="badge open">● OPEN TODAY • 6:00 AM – 10:30 PM</span>
        <h1>Maturi Restaurant & Hotel</h1>
        <p class="tagline">Legendary Biryani & Authentic Andhra Delicacies</p>
        <p>Revenue Ward 18, 19/186, H. Junction / Bandar Road, Ramanagaram, Nuzvid</p>
        <div class="badge-row">
          <span class="badge star">★ 4.3 (528+ Reviews)</span>
          <span class="badge">AC Family Hall</span>
          <span class="badge">Takeaway & Delivery</span>
          <span class="badge">Hotel Rooms</span>
        </div>
      </header>

      <div class="container">
        <div class="actions-grid">
          <a class="action-card" href="https://maps.app.goo.gl/tBf7pusyyvvvt9JX7?g_st=ac" target="_blank">
            <span class="icon">📍</span> Directions
          </a>
          <a class="action-card" href="tel:+918656232456">
            <span class="icon">📞</span> Call Us
          </a>
          <a class="action-card" href="#menu">
            <span class="icon">🍛</span> Menu
          </a>
          <a class="action-card" href="#reserve">
            <span class="icon">🪑</span> Book Table
          </a>
        </div>

        <div class="card" id="menu">
          <h2>Popular Dishes & Biryanis <span>🍛</span></h2>
          <div class="menu-list">
            <div class="menu-item">
              <div>
                <div class="menu-name">Maturi Special Chicken Dum Biryani ★</div>
                <div class="menu-desc">Slow-cooked fragrant basmati rice with tender chicken, secret Andhra masala, salan & raita</div>
              </div>
              <div class="menu-price">₹260</div>
            </div>
            <div class="menu-item">
              <div>
                <div class="menu-name">Andhra Gongura Chicken Biryani ★</div>
                <div class="menu-desc">Tangy sorrel Gongura leaves simmered with spicy chicken dum biryani</div>
              </div>
              <div class="menu-price">₹280</div>
            </div>
            <div class="menu-item">
              <div>
                <div class="menu-name">Maturi Signature Cashew Chicken Fry ★</div>
                <div class="menu-desc">Crispy fried chicken stir-fried with heaps of whole roasted cashews</div>
              </div>
              <div class="menu-price">₹290</div>
            </div>
            <div class="menu-item">
              <div>
                <div class="menu-name">Special Cashew Veg Dum Biryani (Veg)</div>
                <div class="menu-desc">Loaded with paneer, whole cashews, mint and aromatic spices</div>
              </div>
              <div class="menu-price">₹230</div>
            </div>
            <div class="menu-item">
              <div>
                <div class="menu-name">Ghee Karam Dosa (Morning Tiffins)</div>
                <div class="menu-desc">Golden crisp crepe with red chili garlic karam and pure desi ghee</div>
              </div>
              <div class="menu-price">₹80</div>
            </div>
          </div>
        </div>

        <div class="card" id="reserve">
          <h2>Location & Quick Reservation <span>📍</span></h2>
          <p style="font-size:13px; color:var(--muted); margin-bottom:8px;">
            Address: Revenue Ward 18, 19/186, H. Junction Road / Bandar Road, Ramanagaram, Nuzividu, Krishna District, AP - 521201.
          </p>
          <a class="btn-primary" href="https://maps.app.goo.gl/tBf7pusyyvvvt9JX7?g_st=ac" target="_blank">
            Open in Google Maps (QVP2+V76)
          </a>
          <a class="btn-primary" style="background:var(--amber); margin-top:8px;" href="tel:+918656232456">
            Call for Takeaway / Room Booking (+91 86562 32456)
          </a>
        </div>

        <div class="footer">
          © Maturi Restaurant & Hotel, Nuzvid. Built for Client. All rights reserved.
        </div>
      </div>
    </body>
    </html>
    """.trimIndent()
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text("Client Website Preview", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Responsive Web Application", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(
            onClick = {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile.googleMapsUrl))
              context.startActivity(intent)
            }
          ) {
            Icon(Icons.Filled.OpenInBrowser, contentDescription = "Open in browser")
          }
          IconButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Maturi Restaurant Maps", profile.googleMapsUrl)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "Google Maps & Website Link Copied!", Toast.LENGTH_SHORT).show()
            }
          ) {
            Icon(Icons.Filled.ContentCopy, contentDescription = "Copy link")
          }
        }
      )
    },
    modifier = modifier.testTag("website_screen_root")
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      AndroidView(
        factory = { ctx ->
          WebView(ctx).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            webViewClient = object : WebViewClient() {
              override fun onPageFinished(view: WebView?, url: String?) {
                isWebViewLoading = false
              }
            }
            loadDataWithBaseURL("https://maturi-nuzvid.local", clientWebsiteHtml, "text/html", "UTF-8", null)
          }
        },
        modifier = Modifier.fillMaxSize()
      )

      if (isWebViewLoading) {
        CircularProgressIndicator(
          modifier = Modifier.align(Alignment.Center),
          color = CrimsonPrimary
        )
      }
    }
  }
}
