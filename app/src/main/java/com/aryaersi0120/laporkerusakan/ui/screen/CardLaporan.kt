package com.aryaersi0120.laporkerusakan.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@Composable
fun CardLaporan(
    kerusakan: Kerusakan
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
            ) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(LaporApi.getKerusakanUrl(kerusakan.imagepath))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto Barang",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(id = R.drawable.broken_image),
                    placeholder = painterResource(id = R.drawable.broken_image)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = kerusakan.nama_barang,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = kerusakan.deskripsi_kerusakan, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Lokasi: ${kerusakan.lokasi}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
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
                nama_barang = "Laptop",
                deskripsi_kerusakan = "Layar retak",
                lokasi = "Ruang Kelas",
                upload_date = "2023-10-01",
                imageUrl = null
            )
        )
    }
}
