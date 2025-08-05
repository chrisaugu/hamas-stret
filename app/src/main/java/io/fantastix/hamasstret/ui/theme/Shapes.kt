package io.fantastix.hamasstret.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val PngShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),       // Buttons, Chips
    medium = RoundedCornerShape(16.dp),     // Cards
    large = RoundedCornerShape(24.dp),      // Dialogs
    extraLarge = RoundedCornerShape(32.dp)  // Full-width containers or banners
)

val TribalCutShape = CutCornerShape(topEnd = 12.dp, bottomStart = 16.dp,topStart = 12.dp, bottomEnd = 12.dp)