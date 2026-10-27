package com.example.simplecustomerregistration.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplecustomerregistration.constants.CONSTANTS
import com.example.simplecustomerregistration.ui.theme.SimpleCustomerRegistrationTheme
import com.example.simplecustomerregistration.viewmodel.LoginViewModel

class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleCustomerRegistrationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val isCredentialsCorrectState =
                        loginViewModel.isCredentialsCorrect.collectAsStateWithLifecycle()
                    val isCredentialsCorrect = isCredentialsCorrectState.value
                    val errorStringState = loginViewModel.errorString.collectAsStateWithLifecycle()
                    val errorString = errorStringState.value
                    val isPasswordHiddenState =
                        loginViewModel.isPasswordHidden.collectAsStateWithLifecycle()
                    val isPasswordHidden = isPasswordHiddenState.value
                    LoginScreen(
                        loginViewModel.usernameState.collectAsStateWithLifecycle().value,
                        loginViewModel.passwordState.collectAsStateWithLifecycle().value,
                        { loginViewModel.validateCredentials() },
                        { loginViewModel.getErrorString() },
                        { loginViewModel.updateIsPasswordHidden(it) },
                        isCredentialsCorrect,
                        errorString,
                        isPasswordHidden
                    )
                }
            }
        }
    }
}

fun startCustomerListActivity(context: Context) {
    context.startActivity(Intent(context, CustomerListActivity::class.java))
}

@Composable
fun LoginScreen(
    usernameState: TextFieldState,
    passwordState: TextFieldState,
    validateCredentials: () -> Unit,
    getErrorString: () -> String,
    updateIsPasswordHidden: (Boolean) -> Unit,
    isCredentialsCorrect: Boolean,
    errorString: String,
    isPasswordHidden: Boolean
) {
    val context = LocalContext.current

    if (isCredentialsCorrect) {
        startCustomerListActivity(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CONSTANTS.DP_BIG_SPACING),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = CONSTANTS.TITLE_STRING_APP_NAME)

        Spacer(modifier = Modifier.height(CONSTANTS.DP_BIG_SPACING))

        UsernameTextField(usernameState)

        Spacer(modifier = Modifier.height(CONSTANTS.DP_BIG_SPACING))

        PasswordTextField(passwordState, isPasswordHidden)

        Spacer(modifier = Modifier.height(CONSTANTS.DP_BIG_SPACING))

        if (!isCredentialsCorrect && errorString.isNotBlank()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(CONSTANTS.DP_PADDING)
                    .background(MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = getErrorString(), color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Row(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
                .padding(CONSTANTS.DP_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Checkbox(
                checked = isPasswordHidden, onCheckedChange = { updateIsPasswordHidden(it) })

            Spacer(modifier = Modifier.width(CONSTANTS.DP_MEDIUM_SPACING))

            Text(
                text = CONSTANTS.TEXT_STRING_HIDE_PASSWORD
            )
        }

        Button(
            onClick = {
                validateCredentials()
            }, modifier = Modifier
                .fillMaxWidth(1f)
                .padding(CONSTANTS.DP_PADDING)
        ) {
            Text("Log In")
        }

    }
}

@Composable
fun UsernameTextField(
    usernameState: TextFieldState
) {
    OutlinedTextField(
        state = usernameState,
        lineLimits = TextFieldLineLimits.SingleLine,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(CONSTANTS.DP_PADDING),
        label = { Text(CONSTANTS.TEXT_STRING_USER_NAME) })
}

@Composable
fun PasswordTextField(
    passwordState: TextFieldState, isPasswordHidden: Boolean
) {
    if (isPasswordHidden) {
        OutlinedSecureTextField(
            state = passwordState,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(CONSTANTS.DP_PADDING),
            label = { Text(CONSTANTS.TEXT_STRING_PASSWORD) })
    } else {
        OutlinedTextField(
            state = passwordState,
            lineLimits = TextFieldLineLimits.SingleLine,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(CONSTANTS.DP_PADDING),
            label = { Text(CONSTANTS.TEXT_STRING_PASSWORD) })
    }
}