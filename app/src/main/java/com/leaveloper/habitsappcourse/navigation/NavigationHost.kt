package com.leaveloper.habitsappcourse.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.leaveloper.habitsappcourse.authentication.presentation.login.LoginScreen
import com.leaveloper.habitsappcourse.authentication.presentation.signup.SignupScreen
import com.leaveloper.habitsappcourse.home.presentation.detail.DetailScreen
import com.leaveloper.habitsappcourse.home.presentation.home.HomeScreen
import com.leaveloper.habitsappcourse.onboarding.presentation.OnboardingScreen
import com.leaveloper.habitsappcourse.settings.presentation.SettingsScreen

@Composable
fun NavigationHost(
    navHostController: NavHostController,
    startDestination: NavigationRoute,
    logout: () -> Unit
) {
    NavHost(navController = navHostController, startDestination = startDestination.route) {
        composable(NavigationRoute.Onboarding.route) {
            OnboardingScreen(onFinish = {
                // Eliminar pantalla anterior del stack
                navHostController.popBackStack()
                navHostController.navigate(NavigationRoute.Login.route)
            })
        }

        composable(NavigationRoute.Login.route) {
            LoginScreen(onLogin = {
                navHostController.popBackStack()
                navHostController.navigate(NavigationRoute.Home.route)
            }, onSignUp = {
                navHostController.navigate(NavigationRoute.Signup.route)
            })
        }

        composable(NavigationRoute.Signup.route) {
            SignupScreen(onSignIn = {
                /*
                * navHostController.popBackStack()
                *
                * Hacer esto solo elimina la pantalla de SignUp.
                * Sin embargo, no elimina la de Login.
                * Esto provoca que al ir hacia atras estando en Home
                * vaya a la pantalla de Login en lugar de salir de la app
                * */

                navHostController.navigate(NavigationRoute.Home.route) {
                    // Eliminar todos las pantallas hasta la raíz
                    popUpTo(navHostController.graph.id) {
                        inclusive = true
                    }
                }
            }, onLogin = {
                navHostController.popBackStack()
            })
        }

        composable(NavigationRoute.Home.route) {
            HomeScreen(onNewHabit = {
                navHostController.navigate(NavigationRoute.Detail.route)
            }, onSettings = {
                navHostController.navigate(NavigationRoute.Settings.route)
            }, onEditHabit = {
                navHostController.navigate(NavigationRoute.Detail.route + "?habitId=$it")
            })
        }

        composable(NavigationRoute.Detail.route + "?habitId={habitId}", arguments = listOf(
            navArgument("habitId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )) {
            DetailScreen(
                onBack = { navHostController.popBackStack() },
                onSave = { navHostController.popBackStack() })
        }

        composable(NavigationRoute.Settings.route) {
            SettingsScreen(
                onBack = {
                    navHostController.popBackStack()
                },
                onLogout = {
                    logout()
                    navHostController.navigate(NavigationRoute.Login.route) {
                        popUpTo(navHostController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}