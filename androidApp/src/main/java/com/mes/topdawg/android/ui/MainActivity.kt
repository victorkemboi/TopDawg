package com.mes.topdawg.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mes.topdawg.android.ui.dog.breed.DogBreedsScreen
import com.mes.topdawg.android.ui.dog.breeddetails.DogBreedDetailsScreen
import com.mes.topdawg.android.ui.home.HomeScreen
import com.mes.topdawg.android.ui.theme.TopDawgTheme
import com.mes.topdawg.android.ui.theme.orange200

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainLayout()
        }
    }
}

sealed class Screen(val title: String) {
    object Home : Screen("Home")
    object DogBreeds : Screen("Breeds")
    object DogBreedDetails : Screen("BreedDetails")
}

data class BottomNavigationItem(
    val route: String,
    val icon: ImageVector,
    val iconContentDescription: String
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        Screen.Home.title,
        Icons.Default.Home,
        "Home"
    ),
    BottomNavigationItem(
        Screen.DogBreeds.title,
        Icons.Filled.Person,
        "Breeds"
    )
)

@Composable
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
fun MainLayout() {
    val navController = rememberAnimatedNavController()

    TopDawgTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    backgroundColor = Color.Black
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    bottomNavigationItems.forEach { bottomNavigationitem ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    bottomNavigationitem.icon,
                                    contentDescription = bottomNavigationitem.iconContentDescription
                                )
                            },
                            selected = currentRoute == bottomNavigationitem.route,
                            onClick = {
                                if (currentRoute != bottomNavigationitem.route) {
                                    navController.navigate(bottomNavigationitem.route) {
                                        popUpTo(navController.graph.id)
                                        launchSingleTop = true
                                    }
                                }
                            },
                            selectedContentColor = orange200,
                            unselectedContentColor = Color.Gray
                        )
                    }
                }
            }
        ) { paddingValues ->

            AnimatedNavHost(navController, startDestination = Screen.Home.title) {
                composable(
                    route = Screen.Home.title,
                    exitTransition = {
                        slideOutHorizontally() +
                                fadeOut(animationSpec = tween(1000))
                    },
                    popEnterTransition = {
                        slideInHorizontally()
                    }
                ) {
                    HomeScreen(
                        paddingValues = paddingValues,
                        dogBreedSelected = {
                            navController.navigate(Screen.DogBreedDetails.title + "/${it.id}")
                        }
                    )
                }
                composable(
                    route = Screen.DogBreeds.title,
                    enterTransition = {
                        slideInHorizontally() +
                                fadeIn(animationSpec = tween(1000))
                    },
                    popExitTransition = {
                        slideOutHorizontally()
                    }
                ) {
                    DogBreedsScreen(
                        paddingValues = paddingValues,
                        breedSelected = {
                            navController.navigate(Screen.DogBreedDetails.title + "/${it.id}")
                        }
                    )
                }
                composable(
                    route = Screen.DogBreedDetails.title + "/{breedId}",
                    enterTransition = {
                        slideInHorizontally() +
                                fadeIn(animationSpec = tween(1000))
                    },
                    popExitTransition = {
                        slideOutHorizontally()
                    }
                ) {
                    DogBreedDetailsScreen()
                }
            }
        }
    }
}
