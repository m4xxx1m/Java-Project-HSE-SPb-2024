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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import model.SignUpManager
import navigation.BackButton

class SignUpEmailScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BackButton(navigator)
        SignUpEmailForm(navigator)
    }
}

@Composable
fun SignUpEmailForm(navigator: Navigator) {
    val email = remember { mutableStateOf("") }
    val isErrorEmail = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(35.dp).fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth(),
            value = email.value,
            onValueChange = {
                email.value = it
                isErrorEmail.value = false
            },
            trailingIcon = {
                if (isErrorEmail.value) {
                    Icon(
                        Icons.Filled.Warning,
                        "error",
                        tint = MaterialTheme.colors.error
                    )
                }
            },
            label = { Text("Почта") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            val emailTrimmed = email.value.trim()
            if (!emailTrimmed.matches(emailAddressRegex)) {
                isErrorEmail.value = true
                return@Button
            }
            val signUpManager = SignUpManager(emailTrimmed)
            navigator.push(SignUpUserDataScreen(signUpManager))
        }, modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth()) {
            Text("Зарегистрироваться")
        }
    }
}

private val emailAddressRegex = Regex(
    "[a-zA-Z0-9+._%\\-]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)
