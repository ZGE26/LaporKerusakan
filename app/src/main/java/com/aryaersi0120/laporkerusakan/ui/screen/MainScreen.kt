package com.aryaersi0120.laporkerusakan.ui.screen

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aryaersi0120.laporkerusakan.R
import com.aryaersi0120.laporkerusakan.model.User
import com.aryaersi0120.laporkerusakan.navigation.Screen
import com.aryaersi0120.laporkerusakan.network.ApiStatus
import com.aryaersi0120.laporkerusakan.network.UserDataStore
import com.aryaersi0120.laporkerusakan.ui.theme.LaporKerusakanTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: KerusakanViewModel = viewModel()
    val activity = context as? Activity

    val dataStore = UserDataStore(context)
    var showConfirDialog by remember { mutableStateOf(false) }
    val user by dataStore.userFlow.collectAsState(User())
    val logoutSuccess by viewModel.logoutSuccess.collectAsState()

    LaunchedEffect(logoutSuccess) {
        if (logoutSuccess) {
            viewModel.resetLogoutState()
            navController.navigate(Screen.LoadingScreen.route) {
                popUpTo(Screen.MainScreen.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    Menu {
                        viewModel.logout(context) // hanya hapus data
                    }
                }
            )
        },
    ) { innerPadding ->
        ScreenContent(modifier = Modifier.padding(innerPadding), user, viewModel)

        BackHandler {
            showConfirDialog = true
        }

        if (showConfirDialog)
            DialogKonfirmasi(
                title = stringResource(R.string.keluar),
                message = stringResource(R.string.pesan_keluar),
                onConfirm = {
                    activity?.finish()
                },
                onDismiss = { showConfirDialog = false }
            )
    }
}


@Composable
fun ScreenContent(modifier: Modifier, user: User, viewModel: KerusakanViewModel) {
    val data by viewModel.kerusakanList
    val status by viewModel.apiStatus

    LaunchedEffect(user.email) {
        if (user.email.isNotEmpty()) {
            Log.d("UserData", "Logged in as: ${user.email}")
        } else {
            Log.d("UserData", "No user logged in.")
        }
        viewModel.retrieveData(user.email)
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
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxSize()
                    .padding(4.dp),
                columns = GridCells.Fixed(1),
            ) {
                if (data.isNotEmpty()) {
                    items(data) {
                        CardLaporan(kerusakan = it)
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
        }

        ApiStatus.FAILED -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.error))
                Button(
                    onClick = {
                        viewModel.retrieveData(user.email)
                    },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(stringResource(R.string.try_again))
                }
            }
        }
    }
}


@Composable
fun Menu(logout :() -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.menu),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {


            DropdownMenuItem(
                text = { Text(stringResource(R.string.logout)) },
                onClick = {
                    expanded = false
                    logout()
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