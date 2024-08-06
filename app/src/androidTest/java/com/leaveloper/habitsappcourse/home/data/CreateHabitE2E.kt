package com.leaveloper.habitsappcourse.home.data

import android.Manifest
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.leaveloper.habitsappcourse.MainActivity
import com.leaveloper.habitsappcourse.home.data.repository.FakeHomeRepository
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.DetailUseCases
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.GetHabitByIdUseCase
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.InsertHabitUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.CompleteHabitUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.GetAllHabitsForDateUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.HomeUseCases
import com.leaveloper.habitsappcourse.home.domain.home.usecase.SyncHabitUseCase
import com.leaveloper.habitsappcourse.home.presentation.detail.DetailScreen
import com.leaveloper.habitsappcourse.home.presentation.detail.DetailViewModel
import com.leaveloper.habitsappcourse.home.presentation.home.HomeScreen
import com.leaveloper.habitsappcourse.home.presentation.home.HomeViewModel
import com.leaveloper.habitsappcourse.navigation.NavigationRoute
import com.leaveloper.habitsappcourse.ui.theme.HabitsAppCourseTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@HiltAndroidTest
class CreateHabitE2E {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val notificationPermission = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    private lateinit var homeRepository: FakeHomeRepository
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var navController: NavHostController

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration.Builder().setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
        homeRepository = FakeHomeRepository()
        val homeUseCases = HomeUseCases(
            completeHabitUseCase = CompleteHabitUseCase(homeRepository),
            getAllHabitsForDateUseCase = GetAllHabitsForDateUseCase(homeRepository),
            syncHabitUseCase = SyncHabitUseCase(homeRepository)
        )
        homeViewModel = HomeViewModel(homeUseCases)

        val detailUseCase = DetailUseCases(
            getHabitByIdUseCase = GetHabitByIdUseCase(homeRepository),
            insertHabitUseCase = InsertHabitUseCase(homeRepository)
        )
        detailViewModel = DetailViewModel(SavedStateHandle(), detailUseCase)

        composeRule.activity.setContent {
            navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = NavigationRoute.Home.route
            ) {
                composable(NavigationRoute.Home.route) {
                    HomeScreen(
                        onNewHabit = {
                            navController.navigate(NavigationRoute.Detail.route)
                        },
                        onSettings = {
                            navController.navigate(NavigationRoute.Settings.route)
                        },
                        onEditHabit = {
                            navController.navigate(NavigationRoute.Detail.route + "?habitId=$it")
                        },
                        viewModel = homeViewModel
                    )
                }

                composable(
                    NavigationRoute.Detail.route + "?habitId={habitId}",
                    arguments = listOf(
                        navArgument("habitId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) {
                    DetailScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                        onSave = {
                            navController.popBackStack()
                        },
                        viewModel = detailViewModel
                    )
                }
            }
        }
    }

    @Test
    fun createHabit() {
        val habitToCreate = "Vamos al Gym"
        composeRule.onNodeWithText("Home").assertIsDisplayed()
        composeRule.onNodeWithText(habitToCreate).assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Add a new habit").performClick()
        assert(navController.currentDestination?.route?.startsWith(NavigationRoute.Detail.route) == true)
        composeRule.onNodeWithText("New habit").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Enter habit name").performClick()
            .performTextInput(habitToCreate)
        val today = LocalDate.now().dayOfWeek
        composeRule.onNodeWithContentDescription(today.name).performClick()
        composeRule.onNodeWithContentDescription("Enter habit name").performImeAction()
        composeRule.onNodeWithText("Home").assertIsDisplayed()
        assert(navController.currentDestination?.route?.startsWith(NavigationRoute.Home.route) == true)
        composeRule.onNodeWithText(habitToCreate).assertIsDisplayed()
    }
}