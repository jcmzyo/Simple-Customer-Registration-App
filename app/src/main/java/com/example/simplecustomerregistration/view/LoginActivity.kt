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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
                ){
                    val isCredentialsCorrectState = loginViewModel.isCredentialsCorrect.collectAsStateWithLifecycle()
                    val isCredentialsCorrect = isCredentialsCorrectState.value
                    val errorStringState = loginViewModel.errorString.collectAsStateWithLifecycle()
                    val errorString = errorStringState.value
                    val isPasswordHiddenState = loginViewModel.isPasswordHidden.collectAsStateWithLifecycle()
                    val isPasswordHidden= isPasswordHiddenState.value
                    LoginScreen(
                        { loginViewModel.getUsername() },
                        { loginViewModel.updateUsername(it) },
                        { loginViewModel.getPassword() },
                        { loginViewModel.updatePassword(it) },
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

fun startCustomerListActivity(context: Context){
    context.startActivity(Intent(context, CustomerListActivity::class.java))
}

@Composable
fun LoginScreen(
    getUsername: () -> String,
    updateUsername: (String) -> Unit,
    getPassword: () -> String,
    updatePassword: (String) -> Unit,
    validateCredentials: () -> Unit,
    getErrorString: () -> String,
    updateIsPasswordHidden: (Boolean) -> Unit,
    isCredentialsCorrect: Boolean,
    errorString: String,
    isPasswordHidden: Boolean
){
    val context = LocalContext.current

    if (isCredentialsCorrect){
        startCustomerListActivity(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Login")

        Spacer(modifier = Modifier.height(16.dp))

        UsernameTextField(getUsername()) { updateUsername(it) }

        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField(getPassword(), isPasswordHidden) { updatePassword(it) }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isCredentialsCorrect && errorString.isNotBlank()){
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(5.dp)
                    .background(Color.Red)
            ){
                Text(
                    text = getErrorString()
                )
            }
        }

        Row(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ){
            Checkbox(
                checked = isPasswordHidden,
                onCheckedChange = { updateIsPasswordHidden(it) },
                modifier = Modifier
                    .size(16.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Hide Password"
            )
        }

        Button(
            onClick = {
                validateCredentials()
            },
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(5.dp)
        ) {
            Text("Log In")
        }

    }
}

@Composable
fun UsernameTextField(
    username: String,
    updateUsername: (String) -> Unit,
){
    TextField(
        value = username,
        onValueChange = updateUsername,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(5.dp),
        placeholder = { Text(text = "Enter Username") },
        label = { Text("Username") }
    )
}

@Composable
fun PasswordTextField(
    password: String,
    isPasswordHidden: Boolean,
    updatePassword: (String) -> Unit,
){
    TextField(
        value = password,
        onValueChange = updatePassword,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(5.dp),
        visualTransformation = if (isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        placeholder = { Text(text = "Enter Password") },
        label = { Text("Password") }
    )
}