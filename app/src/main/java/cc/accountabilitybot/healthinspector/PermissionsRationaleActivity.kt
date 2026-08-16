package cc.accountabilitybot.healthinspector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cc.accountabilitybot.healthinspector.ui.theme.HealthInspectorTheme

class PermissionsRationaleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthInspectorTheme {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "Health Inspector privacy information",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        "\nHealth Inspector requests read-only access to Health Connect data " +
                            "for testing. It does not modify or delete Health Connect records."
                    )
                }
            }
        }
    }
}
