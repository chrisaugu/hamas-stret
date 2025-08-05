package io.fantastix.hamasstret.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.fantastix.hamasstret.ui.theme.PngRed
import io.fantastix.hamasstret.ui.theme.PngShapes
import io.fantastix.hamasstret.ui.theme.PngWhite

@Composable
fun TribalButton(
    isPrimary: Boolean = false,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) PngRed else MaterialTheme.colorScheme.primary,
            contentColor = if (isPrimary) PngWhite else MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text)
    }
}
