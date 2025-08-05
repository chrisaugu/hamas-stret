package io.fantastix.hamasstret.model

data class Trip(
    val id: Long,
    val time: String,
    val distance: Double,
    var cost: Double
)
