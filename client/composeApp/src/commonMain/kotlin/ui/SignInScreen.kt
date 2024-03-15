package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import model.AuthManager
import model.SignInManager
import navigation.MainNavigation

class SignInScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        SignInForm(navigator)
        val authManager = AuthManager()
        authManager.tryLogin(navigator)
    }
}

@Composable
fun SignInForm(navigator: Navigator) {
    MaterialTheme {
        val emailOrUsername = remember { mutableStateOf("") }
        val isErrorUsername = remember { mutableStateOf(false) }
        val password = remember { mutableStateOf("") }
        val isErrorPassword = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.padding(35.dp).fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth(),
                value = emailOrUsername.value,
                onValueChange = {
                    emailOrUsername.value = it
                    isErrorUsername.value = false
                },
                trailingIcon = {
                    if (isErrorUsername.value) {
                        Icon(
                            Icons.Filled.Warning,
                            "error",
                            tint = MaterialTheme.colors.error
                        )
                    }
                },
                label = { Text("Email or username") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = isErrorUsername.value
            )
            OutlinedTextField(
                modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth(),
                value = password.value,
                onValueChange = {
                    password.value = it
                    isErrorPassword.value = false
                },
                trailingIcon = {
                    if (isErrorPassword.value) {
                        Icon(
                            Icons.Filled.Warning,
                            "error",
                            tint = MaterialTheme.colors.error
                        )
                    }
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = isErrorPassword.value
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                if (emailOrUsername.value.isEmpty()) {
                    isErrorUsername.value = true
                }
                if (password.value.isEmpty()) {
                    isErrorPassword.value = true
                }
                if (isErrorUsername.value || isErrorPassword.value) {
                    return@Button
                }
                val signInManager = SignInManager(
                    emailOrUsername.value.trim(),
                    password.value.trim()
                )
                signInManager.signIn {
                    navigator.popAll()
                    navigator.push(MainNavigation())
                }
            }, modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth()) {
                Text("Sign in")
            }
            Button(
                onClick = {
                    navigator.push(SignUpEmailScreen())
                },
                modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth()
            ) {
                Text("Register")
            }
        }
    }
}
