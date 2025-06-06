package com.aryaersi0120.laporkerusakan.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview

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
            Button(onClick = onConfirm) {
                Text(text = "Ya")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Tidak")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDialogKonfirmasi() {
    DialogKonfirmasi(
        title = "Konfirmasi",
        message = "Apakah Anda yakin ingin melanjutkan?",
        onConfirm = {},
        onDismiss = {}
    )
}