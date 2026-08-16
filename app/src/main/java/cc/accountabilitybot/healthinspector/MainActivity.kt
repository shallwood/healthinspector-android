package cc.accountabilitybot.healthinspector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cc.accountabilitybot.healthinspector.ui.theme.HealthInspectorTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.HealthConnectClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_AVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.getOrCreate

class MainActivity : ComponentActivity() {
    private val healthPermissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class)
    )

    private val permissionLauncher =
        registerForActivityResult(
            PermissionController.createRequestPermissionResultContract()
        ) { grantedPermissions ->

            if (grantedPermissions.containsAll(healthPermissions)) {
                println("Health Connect permission granted")
            } else {
                println("Permission denied")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HealthInspectorTheme {
                Greeting("Health Inspector")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    var status by remember { mutableStateOf("Waiting") }

    val context = LocalContext.current

    val healthPermissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class)
    )

    Text(
        if (healthConnectAvailable)
            "\n\n\n\n\n\nHealth Connect available"
        else
            "\n\n\n\n\n\nHealth Connect unavailable"
    )

    val healthPermissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class)
    )

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = PermissionController.createRequestPermissionResultContract()
        ) { grantedPermissions ->
            if (grantedPermissions.containsAll(healthPermissions)) {
               status = "Health permission granted"
            } else {
                status = "Granted permissions:\n$grantedPermissions"
            }
        }

    Column {
        Text(
            text = name
        )

        Button(
            onClick = {
                // Health Connect permission request will go here
                permissionLauncher.launch(healthPermissions)
            }
        ) {
            Text("Request Health Permissions")
        }

        Text(status)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HealthInspectorTheme {
        Greeting("Health Inspector")


    }
}