package com.aryaersi0120.laporkerusakan.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aryaersi0120.laporkerusakan.R
import com.aryaersi0120.laporkerusakan.model.Kerusakan
import com.aryaersi0120.laporkerusakan.network.LaporApi
import com.aryaersi0120.laporkerusakan.ui.theme.LaporKerusakanTheme

@Composable
fun DialogUpdate(
    kerusakan: Kerusakan,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var namaBarang by remember { mutableStateOf(kerusakan.nama_barang) }
    var deskripsiKerusakan by remember { mutableStateOf(kerusakan.deskripsi_kerusakan) }
    var lokasi by remember { mutableStateOf(kerusakan.lokasi) }

    val isValid = namaBarang.isNotBlank() && deskripsiKerusakan.isNotBlank() && lokasi.isNotBlank()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                val titleRes = if (kerusakan.nama_barang.isBlank()) R.string.laporkan_kerusakan else R.string.perbarui_data
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )


                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(LaporApi.getKerusakanUrl(kerusakan.imagepath))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto Barang",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(2f / 1.5f)
                        .padding(bottom = 12.dp),
                    error = painterResource(id = R.drawable.broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )

                OutlinedTextField(
                    value = namaBarang,
                    onValueChange = { namaBarang = it },
                    label = { Text(stringResource(R.string.nama_barang)) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                )

                OutlinedTextField(
                    value = deskripsiKerusakan,
                    onValueChange = { deskripsiKerusakan = it },
                    label = { Text(stringResource(R.string.deskripsi_kerusakan)) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                )

                OutlinedTextField(
                    value = lokasi,
                    onValueChange = { lokasi = it },
                    label = { Text(stringResource(R.string.lokasi)) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.batal),
                            color = Color.Black
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            if (isValid) onConfirm(namaBarang, deskripsiKerusakan, lokasi)
                        },
                        enabled = isValid,
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.simpan),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewDialogUpdate() {
    LaporKerusakanTheme {
        DialogUpdate(
            kerusakan = Kerusakan(
                id = 1,
                imagepath = "path/to/image.jpg",
                nama_barang = "Laptop",
                deskripsi_kerusakan = "Layar retak",
                lokasi = "Ruang Kelas",
                upload_date = "2023-10-01",
                imageUrl = null
            ),
            onDismiss = {},
            onConfirm = {_,_,_ ->}
        )
    }
}
