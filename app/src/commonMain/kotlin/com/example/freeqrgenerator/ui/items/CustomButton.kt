package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme

@Composable
fun CustomButton(
    text: String = "",
    onClick: () -> Unit,
    icon: ImageVector? = null,
    isCircular: Boolean = false,
    borderColor: Color? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
) {
    if (isCircular) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onClick,
                modifier = Modifier
                    .defaultMinSize(
                        minWidth = 48.dp,
                        minHeight = 48.dp
                    ),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = borderColor ?: containerColor
                ),
                contentPadding = PaddingValues(8.dp)
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            if (text.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    minLines = 2
                )
            }
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor
            ),
            border = BorderStroke(
                width = 1.dp,
                color = borderColor ?: containerColor
            ),
            contentPadding = PaddingValues(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomNormalButtonPreview() {
    FreeQrGeneratorTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomButton(
                text = "Boton Normal",
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
fun CustomNormalIconButtonPreview() {
    FreeQrGeneratorTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomButton(
                text = "Boton con Icono",
                icon = Icons.Default.Add,
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
fun CustomCircularButtonPreview() {
    FreeQrGeneratorTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomButton(
                text = "Circular",
                icon = Icons.Default.Add,
                isCircular = true,
                onClick = {}
            )
        }
    }
}
