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
import model.SignUpManager
import navigation.BackButton
import navigation.MainNavigation

class SignUpUserDataScreen(private val signUpManager: SignUpManager) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BackButton(navigator)
        SignUpUserDataForm(navigator, signUpManager)
    }
}

@Composable
fun SignUpUserDataForm(navigator: Navigator, signUpManager: SignUpManager) {
    MaterialTheme {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val repeatPassword = remember { mutableStateOf("") }
        val isErrorUsername = remember { mutableStateOf(false) }
        val isErrorPassword = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.padding(35.dp).fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth(),
                value = username.value,
                onValueChange = {
                    username.value = it
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
                label = { Text("Username") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
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
                singleLine = true
            )
            OutlinedTextField(
                modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth(),
                value = repeatPassword.value,
                onValueChange = { repeatPassword.value = it
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
                label = { Text("Repeat password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                if (password.value != repeatPassword.value) {
                    isErrorPassword.value = true
                }
                val usernameTrimmed = username.value.trim()
                if (usernameTrimmed.isEmpty() || !usernameTrimmed.matches(Regex(
                        "^[a-zA-Z0-9_\\-]{4,50}$"
                ))) {
                    isErrorUsername.value = true
                }
                val passwordTrimmed = password.value.trim()
                if (passwordTrimmed.isEmpty() || !passwordTrimmed.matches(Regex(
                        "^[a-zA-Z0-9#?!@$%^&*\\-]{8,255}$"
                ))) {
                    isErrorPassword.value = true
                }
                if (isErrorUsername.value || isErrorPassword.value) {
                    return@Button
                }
                signUpManager.initData(usernameTrimmed, password.value.trim())
                signUpManager.signUp {
                    navigator.popAll()
                    navigator.push(MainNavigation())
                }
            }, modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth()) {
                Text("Next")
            }
        }
    }
}
