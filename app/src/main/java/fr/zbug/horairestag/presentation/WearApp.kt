/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package fr.zbug.horairestag.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import fr.zbug.horairestag.presentation.screens.ClustersListScreen
import fr.zbug.horairestag.presentation.screens.LinesListScreen
import fr.zbug.horairestag.presentation.screens.NetworksListScreen
import fr.zbug.horairestag.presentation.screens.Screen
import fr.zbug.horairestag.presentation.screens.ScheduleScreen
import fr.zbug.horairestag.presentation.theme.HorairesTagTheme


@Composable
fun WearApp(navController: NavHostController) {
    HorairesTagTheme {
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = Screen.NetworksList.route,
        ) {
            composable(Screen.NetworksList.route) {
                NetworksListScreen(
                    onNavigateToLinesList = fun(network: String) { Log.d("WearApp", "onNavigateToLinesList"); navController.navigate("linesList/$network") },
                )
            }
            composable(
                route = Screen.LinesList.route,
                arguments = listOf(
                    navArgument("networkId") { type = NavType.StringType }
                )
            ) {backStackEntry ->
                LinesListScreen(
                    backStackEntry.arguments?.getString("networkId")?:"",
                    onNavigateToClustersList = fun(lineId: String) { navController.navigate("clustersList/$lineId") },
                )
            }
            composable(
                route = Screen.ClustersList.route,
                arguments = listOf(
                    navArgument("lineId") { type = NavType.StringType }
                )
            ) {backStackEntry ->
                ClustersListScreen(
                    backStackEntry.arguments?.getString("lineId")?:"",
                    onNavigateToSchedule = fun(lineId: String, clusterId: String) { navController.navigate("ScheduleScreen/$lineId/$clusterId/1") },
                )
            }
            composable(
                route = Screen.ScheduleScreen.route,
                arguments = listOf(
                    navArgument("lineId") { type = NavType.StringType },
                    navArgument("clusterId") { type = NavType.StringType },
                    navArgument("direction") { type = NavType.IntType },
                )
            ) {backStackEntry ->
                ScheduleScreen()
            }
        }
    }
}