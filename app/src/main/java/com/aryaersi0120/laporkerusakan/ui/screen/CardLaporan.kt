package com.aryaersi0120.laporkerusakan.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aryaersi0120.laporkerusakan.R
import com.aryaersi0120.laporkerusakan.model.Kerusakan
import com.aryaersi0120.laporkerusakan.network.LaporApi
import com.aryaersi0120.laporkerusakan.ui.theme.LaporKerusakanTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.clickable
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.DialogProperties

@Composable
fun CardLaporan(
    kerusakan: Kerusakan,
    edit: () -> Unit,
    delete: () -> Unit
) {
    var showImageDialog by remember { mutableStateOf(false) }
    var imageUrlToDisplay by remember { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .clickable {
                        imageUrlToDisplay = LaporApi.getKerusakanUrl(kerusakan.imagepath)
                        showImageDialog = true
                    }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(LaporApi.getKerusakanUrl(kerusakan.imagepath))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto Barang",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_image),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = kerusakan.nama_barang,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = kerusakan.deskripsi_kerusakan,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Lokasi: ${kerusakan.lokasi}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Diunggah pada: ${kerusakan.upload_date}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                MenuCard(
                    edit = { edit() },
                    delete = { delete() }
                )
            }
        }
    }

    if (showImageDialog && imageUrlToDisplay != null) {
        Dialog(
            onDismissRequest = { showImageDialog = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable { showImageDialog = false }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrlToDisplay)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Full Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_image),
                )
            }
        }
    }
}

@Composable
fun MenuCard(edit :() -> Unit, delete:() -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.menu),
            tint = Color.Black
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.edit),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                onClick = {
                    expanded = false
                    edit()
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.hapus),
                        color = MaterialTheme.colorScheme.error
                    )
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun CardLaporanPreview() {
    LaporKerusakanTheme {
        CardLaporan(
            kerusakan = Kerusakan(
                id = 1,
                imagepath = "path/to/image.jpg",
                nama_barang = "Laptop Lenovo Ideapad 330S",
                deskripsi_kerusakan = "Layar retak di bagian kiri bawah, kadang-kadang berkedip, keyboard ada beberapa tombol yang tidak berfungsi dengan baik.",
                lokasi = "Ruang Kelas A101, Gedung Utama",
                upload_date = "2023-10-01",
                imageUrl = null
            ),
            delete = {},
            edit = {}
        )
    }
}