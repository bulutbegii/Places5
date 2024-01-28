package com.example.places5.classes

data class NearbyPlacesResponse(
    val places: List<Place>
)

data class Place(
    val formattedAddress: String,
    val location: Location,
    val displayName: DisplayText
)

data class Location(
    val latitude: Double,
    val longitude: Double
)

data class DisplayText(
    val text: String,
    val languageCode:String
)