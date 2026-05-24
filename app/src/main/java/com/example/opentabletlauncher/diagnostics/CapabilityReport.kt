package com.example.opentabletlauncher.diagnostics

data class CapabilityReport(
    val rootAvailable: Boolean,
    val configFsMounted: Boolean,
    val usbGadgetPathWritable: Boolean,
    val hidModulePresent: Boolean,
    val details: List<String>
) {
    fun summary(): String = buildString {
        appendLine("Root: ${if (rootAvailable) "OK" else "Missing"}")
        appendLine("ConfigFS: ${if (configFsMounted) "OK" else "Missing"}")
        appendLine("USB gadget path: ${if (usbGadgetPathWritable) "Writable" else "Not writable"}")
        appendLine("HID gadget module: ${if (hidModulePresent) "Present" else "Missing"}")
        details.forEach { appendLine("• $it") }
    }
}
