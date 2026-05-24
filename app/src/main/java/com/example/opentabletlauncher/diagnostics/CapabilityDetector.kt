package com.example.opentabletlauncher.diagnostics

import com.example.opentabletlauncher.root.RootShell

class CapabilityDetector(private val shell: RootShell) {

    fun collect(): CapabilityReport {
        val root = shell.isRootAvailable()
        if (!root) {
            return CapabilityReport(false, false, false, false, listOf("Root shell unavailable."))
        }

        val mounts = shell.run("cat /proc/mounts").stdout
        val configFs = mounts.lines().any { it.contains("configfs") && it.contains("/config") || it.contains("/sys/kernel/config") }
        val hidModule = shell.run("grep -E '(^| )libcomposite( |$)|(^| )usb_f_hid( |$)' /proc/modules").code == 0
        val writable = shell.run("test -w /sys/kernel/config/usb_gadget && echo ok").stdout.trim() == "ok"

        val details = mutableListOf<String>()
        if (!configFs) details += "ConfigFS is not mounted. Try: mount -t configfs none /sys/kernel/config"
        if (!hidModule) details += "libcomposite/usb_f_hid module not present. Kernel may not support HID gadget."
        if (!writable) details += "Cannot write /sys/kernel/config/usb_gadget as root. SELinux or kernel lock likely blocking."

        return CapabilityReport(root, configFs, writable, hidModule, details)
    }
}
