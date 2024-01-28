package com.example.places5


import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.places5.services.LocationService
import com.example.places5.services.PlacesService
import com.example.places5.ui.theme.Places5Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Places5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        val locationService = LocationService(context)
        locationService.getLocation(object : LocationService.LocationCallback {
            override fun onLocationReceived(location: Location?) {
                if (location != null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val placesService = PlacesService()
                        val nearbyPlacesResponse = placesService.getNearbyPlaces(
                            location.latitude,
                            location.longitude,
                            1000.0
                        )
                        Log.d("Places", "Places: $nearbyPlacesResponse")
                    }
                } else {
                    //(coroutineContext as Activity).finish()
                    (context as? Activity)?.finish()

                }
            }


        })
    }

    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Places5Theme  {
        Greeting("Android")
    }
}