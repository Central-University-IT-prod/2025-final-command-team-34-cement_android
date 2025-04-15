package com.itblaze.mentor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.itblaze.mentor.data.db.MainDB
import com.itblaze.mentor.ui.screens.menu.NavMenuUI
import com.itblaze.mentor.ui.theme.MentorTheme

class MainActivity : ComponentActivity() {
    private lateinit var dataBase: MainDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dataBase = MainDB.getInstance(this)
        setContent {
            MentorTheme {
                NavMenuUI(dataBase, this, application)
            }
        }
    }
}