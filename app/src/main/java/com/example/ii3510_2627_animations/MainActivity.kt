package com.example.ii3510_2627_animations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ii3510_2627_animations.ui.theme.II3510_2627_AnimationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            II3510_2627_AnimationsTheme {
                CampusDemo()
            }
        }
    }
}
