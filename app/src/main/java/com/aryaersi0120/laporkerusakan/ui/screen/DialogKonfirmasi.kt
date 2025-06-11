package com.aryaersi0120.laporkerusakan.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.aryaersi0120.laporkerusakan.ui.theme.LaporKerusakanTheme

@Composable
fun DialogKonfirmasi(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ,onClick = onConfirm
            ) {
                Text(text = "Ya")
            }
        },
        containerColor = Color.White,
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                onClick = onDismiss
            ) {
                Text(text = "Tidak")
            }
        }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewDialogKonfirmasi() {
    LaporKerusakanTheme {
        DialogKonfirmasi(
            title = "Konfirmasi",
            message = "Apakah Anda yakin ingin melanjutkan?",
            onConfirm = {},
            onDismiss = {}
        )
    }
}