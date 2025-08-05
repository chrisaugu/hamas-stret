package io.fantastix.hamasstret.extensions

fun Long.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)

//    val seconds = (this / 1000) % 60
//    val minutes = (this / (1000 * 60)) % 60
//    val hours = (this / (1000 * 60 * 60))
//    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}