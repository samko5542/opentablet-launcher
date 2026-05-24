package com.example.opentabletlauncher.hid

object HidDescriptor {
    // Generic absolute pointer digitizer (16-bit X/Y, tip switch, pressure byte)
    val descriptor: ByteArray = byteArrayOf(
        0x05, 0x0D, // Usage Page (Digitizers)
        0x09, 0x02, // Usage (Pen)
        0xA1.toByte(), 0x01, // Collection (Application)
        0x09, 0x20, //   Usage (Stylus)
        0xA1.toByte(), 0x00, //   Collection (Physical)
        0x09, 0x42, //     Usage (Tip Switch)
        0x15, 0x00, //     Logical Min 0
        0x25, 0x01, //     Logical Max 1
        0x75, 0x01, //     Report size 1
        0x95.toByte(), 0x01, //     Report count 1
        0x81.toByte(), 0x02, //     Input(Data,Var,Abs)
        0x95.toByte(), 0x07, //     pad
        0x81.toByte(), 0x03, //     Input(Const,Var,Abs)
        0x05, 0x01, //     Usage Page(Generic Desktop)
        0x09, 0x30, //     Usage X
        0x09, 0x31, //     Usage Y
        0x16, 0x00, 0x00, //     Logical Min 0
        0x26, 0xFF.toByte(), 0x7F, // Logical Max 32767
        0x75, 0x10, // Report size 16
        0x95.toByte(), 0x02, // Report count 2
        0x81.toByte(), 0x02, // Input(Data,Var,Abs)
        0x05, 0x0D, // Usage Page Digitizers
        0x09, 0x30, // Usage Tip Pressure
        0x15, 0x00,
        0x26, 0xFF.toByte(), 0x00,
        0x75, 0x08,
        0x95.toByte(), 0x01,
        0x81.toByte(), 0x02,
        0xC0.toByte(),
        0xC0.toByte()
    )
}
