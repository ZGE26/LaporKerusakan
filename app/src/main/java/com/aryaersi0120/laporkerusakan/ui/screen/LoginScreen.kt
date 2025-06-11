package com.aryaersi0120.laporkerusakan.ui.screen

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aryaersi0120.laporkerusakan.BuildConfig
import com.aryaersi0120.laporkerusakan.R
import com.aryaersi0120.laporkerusakan.model.User
import com.aryaersi0120.laporkerusakan.navigation.Screen
import com.aryaersi0120.laporkerusakan.network.UserDataStore
import com.aryaersi0120.laporkerusakan.ui.theme.LaporKerusakanTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavHostController) {
    var showConfirDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { innerPadding ->
        LoginContent(modifier = Modifier.padding(innerPadding), navController)
    }

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

@Composable
fun LoginContent(modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    var errorMassage by remember { mutableStateOf("") }
    var showDialogError by remember { mutableStateOf(false) }
    val dataStore = UserDataStore(context)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.lapor_kerusakan),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )


        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "LaporKerusakan",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Bantu kami menjaga fasilitas tetap aman & nyaman.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    signIn(context, navController, dataStore)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color.LightGray),
            contentPadding = PaddingValues(vertical = 12.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_ico),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.login_dengan_google),
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = "Dengan login, kamu menyetujui syarat dan ketentuan kami.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dialog Error
        if (showDialogError) {
            ErrorDialog(
                message = errorMassage,
                onDismiss = { showDialogError = false }
            )
        }
    }
}


private suspend fun signIn(
    context: Context,
    navController: NavHostController,
    dataStore: UserDataStore
) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)

        withContext(Dispatchers.Main) {
            handleSignIn(result, navController, dataStore)
        }
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(
    result: GetCredentialResponse,
    navController: NavHostController,
    dataStore: UserDataStore
    ) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.savaData(User(nama, email, photoUrl))

            navController.navigate(Screen.LoadingScreen.route) {
                popUpTo(Screen.LoginScreen.route) { inclusive = true }
            }

        } catch (e: GetCredentialException) {
            Log.e("SIGN-IN", "Error : ${e.errorMessage}")
        }
    } else {
        Log.e("SIGN-IN", "Error: unrecognized credential type")
    }
}


@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LoginScreenPreview() {
    LaporKerusakanTheme {
        LoginScreen(rememberNavController())
    }
}
