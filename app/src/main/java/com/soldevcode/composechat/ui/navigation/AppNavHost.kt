package com.soldevcode.composechat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.soldevcode.composechat.ui.ConversationScreen
import com.soldevcode.composechat.ui.LanguageListScreen
import com.soldevcode.composechat.ui.SettingScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Start.name) {
        composable(AppScreen.Start.name) {
            ConversationScreen(navController = navController)
        }
        composable(AppScreen.Settings.name) {
            SettingScreen(navController = navController)
        }
        composable(AppScreen.Language.name) {
            LanguageListScreen(navController = navController)
        }
        // Define other destinations...
    }
}