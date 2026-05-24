package com.example.opentabletlauncher.ui

import android.os.Bundle
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

        refreshStatus()

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

    private fun refreshStatus() {
        val report = capabilities.collect()
        val identity = gadget.readIdentityFromExistingGadget()
        binding.statusText.text = buildString {
            appendLine(report.summary().trimEnd())
            appendLine()
            appendLine("Using existing HID endpoint: /dev/hidg0")
            if (identity != null) {
                appendLine("Use these values for OTD matching:")
                appendLine("idVendor=${identity.idVendor}")
                appendLine("idProduct=${identity.idProduct}")
                appendLine("manufacturer=${identity.manufacturer}")
                appendLine("product=${identity.product}")
                appendLine("serialnumber=${identity.serialNumber}")
            } else {
                appendLine("Could not read gadget identity from /sys/kernel/config/usb_gadget.")
            }
        }
    }

    private fun startTabletMode() {
        mapper = InputMapper(TabletConfig(binding.root.width.coerceAtLeast(1), binding.root.height.coerceAtLeast(1)))
        val result = gadget.startUsingExistingHid()
        running = result.code == 0
        binding.startStopButton.text = if (running) "STOP" else "START"
        binding.statusText.text = if (running) {
            val identityText = gadget.readIdentityFromExistingGadget()?.let {
                "\nidVendor=${it.idVendor}\nidProduct=${it.idProduct}\nmanufacturer=${it.manufacturer}\nproduct=${it.product}\nserialnumber=${it.serialNumber}"
            } ?: ""
            "Tablet mode active on existing /dev/hidg0$identityText"
        } else {
            "Cannot use /dev/hidg0. Ensure it already exists and is writable by root.\n${result.stderr.ifBlank { result.stdout }}"
        }
    }

    private fun stopTabletMode() {
        running = false
        gadget.stopUsingExistingHid()
        binding.startStopButton.text = "START"
        refreshStatus()
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
