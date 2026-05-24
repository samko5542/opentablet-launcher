package com.example.opentabletlauncher.hid

import com.example.opentabletlauncher.root.RootShell

class UsbGadgetManager(private val shell: RootShell) {
    private val gadgetPath = "/sys/kernel/config/usb_gadget/opentablet"

    fun setup(): RootShell.Result {
        val descriptorHex = HidDescriptor.descriptor.joinToString("") { "%02x".format(it) }
        val cmd = """
            set -e
            mkdir -p $gadgetPath
            echo 0x18D1 > $gadgetPath/idVendor
            echo 0x4E12 > $gadgetPath/idProduct
            mkdir -p $gadgetPath/strings/0x409
            echo "OpenTablet" > $gadgetPath/strings/0x409/manufacturer
            echo "Root Tablet" > $gadgetPath/strings/0x409/product
            echo "OTB001" > $gadgetPath/strings/0x409/serialnumber
            mkdir -p $gadgetPath/configs/c.1/strings/0x409
            echo "Tablet" > $gadgetPath/configs/c.1/strings/0x409/configuration
            mkdir -p $gadgetPath/functions/hid.usb0
            echo 0 > $gadgetPath/functions/hid.usb0/protocol
            echo 0 > $gadgetPath/functions/hid.usb0/subclass
            echo 8 > $gadgetPath/functions/hid.usb0/report_length
            xxd -r -p <<'HD' > $gadgetPath/functions/hid.usb0/report_desc
$descriptorHex
HD
            ln -sf $gadgetPath/functions/hid.usb0 $gadgetPath/configs/c.1/hid.usb0
            UDC=$(ls /sys/class/udc | head -n 1)
            echo $UDC > $gadgetPath/UDC
        """.trimIndent()
        return shell.run(cmd)
    }

    fun teardown() {
        shell.run("""
            set +e
            echo '' > $gadgetPath/UDC
            rm -f $gadgetPath/configs/c.1/hid.usb0
            rmdir $gadgetPath/functions/hid.usb0
            rmdir $gadgetPath/configs/c.1/strings/0x409
            rmdir $gadgetPath/configs/c.1
            rmdir $gadgetPath/strings/0x409
            rmdir $gadgetPath
        """.trimIndent())
    }

    fun writeReport(report: ByteArray) {
        val hex = report.joinToString("") { "%02x".format(it) }
        shell.run("xxd -r -p <<'R' > /dev/hidg0\n$hex\nR")
    }
}
