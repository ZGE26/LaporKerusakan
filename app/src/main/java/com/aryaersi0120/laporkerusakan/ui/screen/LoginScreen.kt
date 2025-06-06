package com.aryaersi0120.laporkerusakan.ui.screen

import android.app.Activity
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aryaersi0120.laporkerusakan.R
import com.aryaersi0120.laporkerusakan.navigation.Screen
import com.aryaersi0120.laporkerusakan.ui.theme.LaporKerusakanTheme

@Composable
fun LoginScreen(navController: NavHostController) {
    var showConfirDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMassage by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var showDialogError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val visibilityOn = painterResource(R.drawable.visibility)
    val visibilityOff = painterResource(R.drawable.visibility_off_24)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.email_icon)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                Text(
                    text = stringResource(R.string.format_email),
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                )
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = stringResource(R.string.password_icon)
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                val image = if (passwordVisible) visibilityOn else visibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = image,
                        contentDescription = description
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    showDialogError = true
                    errorMassage = "Email dan Password tidak boleh kosong"
                } else {
                    Log.d("LoginScreen", "Login clicked with Email: $email and Password: $password")
                    navController.navigate(Screen.MainScreen.route)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.belum_punya_akun),
                style = MaterialTheme.typography.bodyMedium,
            )
            TextButton(
                onClick = {
                    navController.navigate(Screen.RegisterScreen.route)
                }
            ) {
                Text(
                    text = stringResource(R.string.daftar),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray
            )
            Text(
                text = "Atau",
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "Fitur login dengan akun Google belum tersedia",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color.LightGray),
            contentPadding = PaddingValues(horizontal = 16.dp)
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
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.login_dengan_google),
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }



        if (showDialogError) {
            ErrorDialog(
                message = errorMassage,
                onDismiss = { showDialogError = false }
            )
        }
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
