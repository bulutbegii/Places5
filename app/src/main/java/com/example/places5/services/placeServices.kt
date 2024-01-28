package com.example.places5.services

import com.google.gson.Gson
import com.example.places5.classes.NearbyPlacesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class PlacesService {
    private val gson = Gson()

    suspend fun getNearbyPlaces(latitude: Double, longitude: Double, radius: Double): NearbyPlacesResponse = withContext(Dispatchers.IO) {
        val url = "https://places.googleapis.com/v1/places:searchNearby"

        val requestBodyJson = createJsonRequestBody(radius, latitude, longitude)

        val client = OkHttpClient()
        val request = Request.Builder().url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Goog-Api-Key", "AIzaSyBSswhRH_ru-ATYWNK-WlCzzN9xm_8ID5U")
            .addHeader("X-Goog-FieldMask", "places.displayName,places.location,places.formattedAddress")
            .post(requestBodyJson)  // Use POST method and add the request body
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code ${response.code}")

            val jsonData = response.body?.string()
            // Use Gson to convert JSON to NearbyPlacesResponse
            return@withContext gson.fromJson(jsonData, NearbyPlacesResponse::class.java)
        }
    }

    private fun createJsonRequestBody(radius: Double, latitude: Double, longitude: Double): RequestBody {
        val requestBodyJson = """
            {
                "maxResultCount": 20,
                "locationRestriction": {
                    "circle": {
                        "center": {
                            "latitude": $latitude,
                            "longitude": $longitude
                        },
                        "radius": $radius
                    }
                }
            }
        """.trimIndent()

        return requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull())
    }
}