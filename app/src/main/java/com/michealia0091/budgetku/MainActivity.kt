package com.michealia0091.budgetku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.michealia0091.budgetku.navigation.SetupNavGraph
import com.michealia0091.budgetku.ui.screen.MainScreen
import com.michealia0091.budgetku.ui.theme.BudgetKuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetKuTheme {
                SetupNavGraph()
            }
        }
    }
}