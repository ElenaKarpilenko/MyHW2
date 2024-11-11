package com.example.myhw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Sealed class для маршрутов
sealed class OnboardingRoute(val route: String) {
    object Welcome : OnboardingRoute("welcome")
    object Registration : OnboardingRoute("registration")
    object Confirmation : OnboardingRoute("confirmation/{userName}") {
        fun createRoute(userName: String) = "confirmation/$userName"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnboardingNavigation()
        }
    }
}

@Composable
fun OnboardingNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = OnboardingRoute.Welcome.route) {
        composable(OnboardingRoute.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(OnboardingRoute.Registration.route) {
            RegistrationScreen(navController)
        }
        composable(
            OnboardingRoute.Confirmation.route,
            arguments = listOf(navArgument("userName") { defaultValue = "" })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            ConfirmationScreen(userName = userName)
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to the App!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(OnboardingRoute.Registration.route) }) {
            Text("Go to Registration")
        }
    }
}

@Composable
fun RegistrationScreen(navController: NavController) {
    var userName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registration Screen", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Поле для ввода имени пользователя
        BasicTextField(
            value = userName,
            onValueChange = { userName = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
                        .padding(16.dp)
                ) {
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (userName.isNotEmpty()) {
                navController.navigate(OnboardingRoute.Confirmation.createRoute(userName))
            }
        }) {
            Text("Go to Confirmation")
        }
    }
}

@Composable
fun ConfirmationScreen(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Hello, $userName!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your registration is complete.", fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OnboardingNavigation()
}
