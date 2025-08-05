package io.fantastix.hamasstret.ui.components

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.fantastix.hamasstret.ui.theme.PngRed
import io.fantastix.hamasstret.ui.theme.PngWhite

@Composable
fun DistanceInputField(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PngRed,
            contentColor = PngWhite
        )
    ) {
        Text(text)
    }
}
