package com.example.opentabletlauncher.config

data class TabletConfig(
    val screenWidth: Int,
    val screenHeight: Int,
    val invertX: Boolean = false,
    val invertY: Boolean = false,
    val pressureSimulation: Boolean = true
)
