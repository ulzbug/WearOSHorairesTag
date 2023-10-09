package fr.zbug.horairestag.presentation.screens

/**
 * Represent all Screens (Composables) in the app.
 */
sealed class Screen(
    val route: String
) {
    object NetworksList : Screen("networksList")
    object LinesList : Screen("linesList/{networkId}")
    object ClustersList : Screen("clustersList/{lineId}")
//    object ClustersList : Screen("clustersList/{lineId}")
}