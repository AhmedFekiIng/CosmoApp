import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presentation.screen.BluetoothScreen
import com.example.presentation.screen.DeviceDetailScreen
import com.example.presentation.screen.DeviceListScreen


@Composable
fun CosmoApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "deviceList") {
        composable("deviceList") {
            DeviceListScreen(navController)
        }
        composable("deviceDetail/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull()
            index?.let { DeviceDetailScreen(it) }
        }
        composable("bluetooth") {
            BluetoothScreen()
        }
    }
}