package cc.accountabilitybot.healthinspector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import cc.accountabilitybot.healthinspector.ui.theme.HealthInspectorTheme

private val STEP_PERMISSIONS = setOf(
    HealthPermission.getReadPermission(StepsRecord::class),
    HealthPermission.getReadPermission(NutritionRecord::class)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HealthInspectorTheme {
                HealthInspectorScreen(
                    onReadNutrition = { readNutritionSample() }
                )
            }
        }
    }

    private fun readNutritionSample() {
        val healthConnectClient = HealthConnectClient.getOrCreate(this)

        lifecycleScope.launch {
            try {
                val endTime = Instant.now()
                val startTime = endTime.minus(Duration.ofHours(24))

                val response = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        NutritionRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(
                            startTime,
                            endTime
                        )
                    )
                )

                println("Nutrition records found: ${response.records.size}")

                response.records.forEachIndexed { index, record ->
                    println("----- Nutrition Record $index -----")
                    println("Source: ${record.metadata.dataOrigin.packageName}")
                    println("Name: ${record.name}")
                    println("Meal type: ${record.mealType}")
                    println("Start: ${record.startTime}")
                    println("End: ${record.endTime}")
                    println("Energy: ${record.energy}")
                    println("Protein: ${record.protein}")
                    println("Carbohydrate: ${record.totalCarbohydrate}")
                    println("Fat: ${record.totalFat}")
                    println("Fiber: ${record.dietaryFiber}")
                    println("Sugar: ${record.sugar}")
                    println("Record ID: ${record.metadata.id}")
                }

            } catch (e: Exception) {
                    println("Nutrition read failed: ${e.message}")
                    e.printStackTrace()
               }
            }
        }
    }



@Composable
fun HealthInspectorScreen(
    onReadNutrition: () -> Unit
) {
    val context = LocalContext.current
    val healthConnectAvailable =
        HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE

    var status by remember {
        mutableStateOf(
            if (healthConnectAvailable) "Waiting for permission request"
            else "Health Connect unavailable"
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract()
    ) { grantedPermissions ->
        status = if (grantedPermissions.containsAll(STEP_PERMISSIONS)) {
            "Steps permission granted"
        } else {
            "Permission not granted\nReturned: $grantedPermissions"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Health Inspector", style = MaterialTheme.typography.headlineMedium)
        Text(
            if (healthConnectAvailable) "Health Connect available"
            else "Health Connect unavailable"
        )

        Button(
            enabled = healthConnectAvailable,
            onClick = onReadNutrition
        ) {
            Text("Read Nutrition Sample")
        }
        Button(
            enabled = healthConnectAvailable,
            onClick = { permissionLauncher.launch(STEP_PERMISSIONS) }
        ) {
            Text("Request Steps Permission")
        }


        Text(status)
    }
}

@Preview(showBackground = true)
@Composable
fun HealthInspectorPreview() {
    HealthInspectorTheme {
        HealthInspectorScreen(
            onReadNutrition = {}
        )
    }
}

private fun readNutritionSample() {
    val healthConnectClient = HealthConnectClient.getOrCreate(this)

    lifecycleScope.launch {
        try {
            val endTime = Instant.now()
            val startTime = endTime.minus(Duration.ofHours(24))

            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    NutritionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(
                        startTime,
                        endTime
                    )
                )
            )

            println("Nutrition records found: ${response.records.size}")

            response.records.forEachIndexed { index, record ->
                println("----- Nutrition Record $index -----")
                println("Source: ${record.metadata.dataOrigin.packageName}")
                println("Name: ${record.name}")
                println("Meal type: ${record.mealType}")
                println("Start: ${record.startTime}")
                println("End: ${record.endTime}")
                println("Energy: ${record.energy}")
                println("Protein: ${record.protein}")
                println("Carbohydrate: ${record.totalCarbohydrate}")
                println("Fat: ${record.totalFat}")
                println("Fiber: ${record.dietaryFiber}")
                println("Sugar: ${record.sugar}")
                println("Record ID: ${record.metadata.id}")
            }

        } catch (e: Exception) {
            println("Nutrition read failed: ${e.message}")
            e.printStackTrace()
        }
    }
}