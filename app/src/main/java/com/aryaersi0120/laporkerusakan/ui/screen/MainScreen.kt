package com.aryaersi0120.laporkerusakan.ui.screen

import android.app.Activity
import android.content.ContentResolver
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aryaersi0120.laporkerusakan.R
import com.aryaersi0120.laporkerusakan.model.Kerusakan
import com.aryaersi0120.laporkerusakan.model.User
import com.aryaersi0120.laporkerusakan.navigation.Screen
import com.aryaersi0120.laporkerusakan.network.ApiStatus
import com.aryaersi0120.laporkerusakan.network.UserDataStore
import com.aryaersi0120.laporkerusakan.ui.theme.LaporKerusakanTheme
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: KerusakanViewModel = viewModel()
    val errorMessage by viewModel.errorMessage

    val activity = context as? Activity

    val dataStore = UserDataStore(context)
    var showConfirDialog by remember { mutableStateOf(false) }
    val user by dataStore.userFlow.collectAsState(User())
    val logoutSuccess by viewModel.logoutSuccess.collectAsState()
    var showKerusakan by remember { mutableStateOf(false) }

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val laucher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showKerusakan = true
    }

    LaunchedEffect(logoutSuccess) {
        if (logoutSuccess) {
            viewModel.resetLogoutState()
            navController.navigate(Screen.LoadingScreen.route) {
                popUpTo(Screen.MainScreen.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                ),
                actions = {
                    Menu(
                        logout = {
                            viewModel.logout(context)
                        },
                        user
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color.Black,
                contentColor = Color.White,
                onClick = {
                val options = CropImageContractOptions(
                    null, CropImageOptions(
                        imageSourceIncludeGallery = true,
                        imageSourceIncludeCamera = true,
                        fixAspectRatio = false
                    )
                )
                laucher.launch(options)
                },
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_kerusakan),
                )
            }
        }
    ) { innerPadding ->
        ScreenContent(modifier = Modifier.padding(innerPadding), user, viewModel)

        BackHandler {
            showConfirDialog = true
        }

        if (showConfirDialog) {
            DialogKonfirmasi(
                title = stringResource(R.string.keluar),
                message = stringResource(R.string.pesan_keluar),
                onConfirm = {
                    activity?.finish()
                },
                onDismiss = { showConfirDialog = false }
            )
        }

        if (showKerusakan) {
            KerusakanDialog(
                bitmap = bitmap,
                onDismiss = { showKerusakan = false },
            ) { namaBarang, deskripsiKerusakan, lokasi ->
                viewModel.saveData(user.email, namaBarang, deskripsiKerusakan, lokasi, bitmap!!)
                showKerusakan = false
            }
        }

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }
}


@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    user: User,
    viewModel: KerusakanViewModel
) {
    val data by viewModel.kerusakanList
    val status by viewModel.apiStatus

    var showDialog by remember { mutableStateOf(false) }
    var showEdit by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Kerusakan?>(null) }

    LaunchedEffect(key1 = user.email) {
        if (user.email.isNotEmpty()) {
            Log.d("UserData", "Logged in as: ${user.email}")
            viewModel.retrieveData(user.email)
        } else {
            Log.d("UserData", "No user logged in.")
        }
    }

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ApiStatus.SUCCESS -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                if (data.isNotEmpty()) {
                    items(data) { item ->
                        CardLaporan(
                            kerusakan = item,
                            edit = {
                                selectedItem = item
                                showEdit = true
                            },
                            delete = {
                                selectedItem = item
                                showDialog = true
                            }
                        )
                    }
                } else {
                    item {
                        Text(
                            text = stringResource(R.string.no_data_available),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            if (showDialog && selectedItem != null) {
                DialogKonfirmasi(
                    title = "Hapus Laporan",
                    message = "Apakah anda yakin menghapus laporan ini?",
                    onDismiss = {
                        showDialog = false
                        selectedItem = null
                    },
                    onConfirm = {
                        selectedItem?.let {
                            Log.d("Delete", "data ${it.id}")
                            viewModel.deleteData(user.email, it.id)
                        }
                        showDialog = false
                        selectedItem = null
                    }
                )
            }

            // Dialog update/edit data
            if (showEdit && selectedItem != null) {
                DialogUpdate(
                    kerusakan = selectedItem!!,
                    onDismiss = {
                        showEdit = false
                        selectedItem = null
                    },
                    onConfirm = { nama, deskripsi, lokasi ->
                        viewModel.updateData(user.email, selectedItem!!.id, nama, deskripsi, lokasi)
                        showEdit = false
                        selectedItem = null
                    }
                )
            }
        }

        ApiStatus.FAILED -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.error))
                Button(
                    onClick = { viewModel.retrieveData(user.email) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(R.string.try_again))
                }
            }
        }
    }
}


private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Rrror: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}


@Composable
fun Menu(
    logout :() -> Unit,
    user: User
) {
    var expanded by remember { mutableStateOf(false) }
    var showProfile by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.menu),
            tint = Color.White
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White
        ) {


            DropdownMenuItem(
                text = { Text(stringResource(R.string.logout)) },
                onClick = {
                    expanded = false
                    logout()
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.profile)) },
                onClick = {
                    expanded = false
                    showProfile = true
                }
            )
        }
        if(showProfile) {
            ProfilDialog(
                user = user,
                onDismissRequest = {
                    showProfile = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    LaporKerusakanTheme {
        MainScreen(rememberNavController())
    }
}