package com.aryaersi0120.laporkerusakan.ui.screen

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
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
import com.aryaersi0120.laporkerusakan.network.TokenPreference
import com.aryaersi0120.laporkerusakan.ui.theme.LaporKerusakanTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    var showConfirDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

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
                        viewModel.logout(context, navController)
                    }
                }
            )
        },
    ) { innerPadding ->
        ScreenContent(modifier = Modifier.padding(innerPadding), context)

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
fun ScreenContent(modifier: Modifier, context: Context) {
    val token by TokenPreference.getToken(context).collectAsState(initial = null)

    LaunchedEffect(token) {
        if (!token.isNullOrEmpty()) {
            Log.d("Token", "Logged in with token: $token")

        }
    }

    Text("Testing Aplikasi Kerusakan", modifier)
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
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    LaporKerusakanTheme {
        MainScreen(rememberNavController())
    }
}