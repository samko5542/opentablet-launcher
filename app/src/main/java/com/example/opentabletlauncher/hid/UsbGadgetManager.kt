package com.example.opentabletlauncher.hid

import com.example.opentabletlauncher.root.RootShell

data class GadgetIdentity(
    val idVendor: String,
    val idProduct: String,
    val manufacturer: String,
    val product: String,
    val serialNumber: String
)

class UsbGadgetManager(private val shell: RootShell) {

    fun startUsingExistingHid(): RootShell.Result {
        return shell.run("test -w /dev/hidg0")
    }

    fun stopUsingExistingHid() {
        // Intentionally no-op: app does not create/destroy gadget anymore.
    }

    fun readIdentityFromExistingGadget(): GadgetIdentity? {
        val cmd = """
            set -e
            G=""
            for d in /sys/kernel/config/usb_gadget/*; do
              [ -d "$d" ] || continue
              if ls "$d/functions" 2>/dev/null | grep -q '^hid\.'; then
                G="$d"
                break
              fi
            done
            [ -n "$G" ] || exit 2
            LANG_DIR=$(ls "$G/strings" 2>/dev/null | head -n 1)
            [ -n "$LANG_DIR" ] || LANG_DIR=0x409
            IDV=$(cat "$G/idVendor" 2>/dev/null || true)
            IDP=$(cat "$G/idProduct" 2>/dev/null || true)
            MFG=$(cat "$G/strings/$LANG_DIR/manufacturer" 2>/dev/null || true)
            PRD=$(cat "$G/strings/$LANG_DIR/product" 2>/dev/null || true)
            SRL=$(cat "$G/strings/$LANG_DIR/serialnumber" 2>/dev/null || true)
            printf '%s\n%s\n%s\n%s\n%s\n' "$IDV" "$IDP" "$MFG" "$PRD" "$SRL"
        """.trimIndent()

        val result = shell.run(cmd)
        if (result.code != 0) return null
        val lines = result.stdout.lines().map { it.trim() }.filter { it.isNotEmpty() }
        if (lines.size < 5) return null
        return GadgetIdentity(
            idVendor = lines[0],
            idProduct = lines[1],
            manufacturer = lines[2],
            product = lines[3],
            serialNumber = lines[4]
        )
    }

    fun writeReport(report: ByteArray) {
        val hex = report.joinToString("") { "%02x".format(it) }
        shell.run("xxd -r -p <<'R' > /dev/hidg0\n$hex\nR")
    }
}
