package com.ds.connectivityradar.main_menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainMenuButton(buttonText: String, buttonAction: () -> Unit, text: String) {
    Button(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(), onClick = buttonAction
    ) {
        Text(buttonText)
    }
    Text(
        modifier = Modifier.padding(5.dp), text = text
    )
}