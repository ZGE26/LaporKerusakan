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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aryaersi0120.laporkerusakan.R
import com.aryaersi0120.laporkerusakan.ui.theme.LaporKerusakanTheme

@Composable
fun RegisterScreen() {
    var showExitConfirm by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        RegisterContent(modifier = Modifier.padding(padding))
    }

    BackHandler {
        showExitConfirm = true
    }

    if (showExitConfirm) {
        DialogKonfirmasi(
            title = stringResource(R.string.keluar),
            message = stringResource(R.string.pesan_keluar),
            onConfirm = { activity?.finish() },
            onDismiss = { showExitConfirm = false }
        )
    }
}

@Composable
fun RegisterContent(modifier: Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showDialogError by remember { mutableStateOf(false) }

    var context = LocalContext.current

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
            text = "Daftar Akun",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email Icon")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password Icon")
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = if (passwordVisible) visibilityOn else visibilityOff,
                        contentDescription = if (passwordVisible) "Sembunyikan" else "Tampilkan"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Konfirmasi Password") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password Icon")
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                        errorMessage = "Semua field harus diisi."
                        showDialogError = true
                    }
                    password != confirmPassword -> {
                        errorMessage = "Password tidak sama."
                        showDialogError = true
                    }
                    else -> {
                        Log.d("RegisterScreen", "Nama: $name, Email: $email, Password: $password")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Daftar")
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
                message = errorMessage,
                onDismiss = { showDialogError = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RegisterPreview() {
    LaporKerusakanTheme {
        RegisterScreen()
    }
}
