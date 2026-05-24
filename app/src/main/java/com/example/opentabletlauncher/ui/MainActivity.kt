package com.example.opentabletlauncher.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.opentabletlauncher.config.TabletConfig
import com.example.opentabletlauncher.databinding.ActivityMainBinding
import com.example.opentabletlauncher.diagnostics.CapabilityDetector
import com.example.opentabletlauncher.hid.InputMapper
import com.example.opentabletlauncher.hid.UsbGadgetManager
import com.example.opentabletlauncher.root.RootShell

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val rootShell = RootShell()
    private val gadget = UsbGadgetManager(rootShell)
    private val capabilities = CapabilityDetector(rootShell)
    private var mapper: InputMapper? = null
    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        hideSystemUi()

        val report = capabilities.collect()
        binding.statusText.text = report.summary()

        binding.startStopButton.setOnClickListener {
            if (running) stopTabletMode() else startTabletMode()
        }

        binding.root.setOnTouchListener { _, event ->
            if (running && mapper != null) {
                gadget.writeReport(mapper!!.map(event))
            }
            true
        }
    }

    private fun startTabletMode() {
        mapper = InputMapper(TabletConfig(binding.root.width.coerceAtLeast(1), binding.root.height.coerceAtLeast(1)))
        val result = gadget.setup()
        running = result.code == 0
        binding.startStopButton.text = if (running) "STOP" else "START"
        binding.statusText.text = if (running) {
            "Tablet mode active (hidg0)"
        } else {
            "Failed to start:\n${result.stderr.ifBlank { result.stdout }}"
        }
    }

    private fun stopTabletMode() {
        running = false
        gadget.teardown()
        binding.startStopButton.text = "START"
        binding.statusText.text = "Tablet mode stopped"
    }

    private fun hideSystemUi() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
            )
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUi()
    }
}
