/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package fr.zbug.horairestag.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import fr.zbug.horairestag.presentation.screens.LinesListScreen
import fr.zbug.horairestag.presentation.screens.NetworksListScreen
import fr.zbug.horairestag.presentation.screens.Screen
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
                    onNavigateToLinesList = fun(network: String) { navController.navigate("linesList/$network") },
                )
            }
            composable(Screen.LinesList.route) {backStackEntry ->
                LinesListScreen(
                    onNavigateToFriends = { navController.navigate("friendsList") } ,
                    backStackEntry.arguments?.getString("networkId")
                )

            }
        }
    }
}