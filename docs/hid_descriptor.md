# HID Descriptor Notes

Current descriptor:
- Usage Page: Digitizers (0x0D)
- Usage: Pen/Stylus
- Tip switch bit
- Absolute X/Y, 16-bit each (0..32767)
- Pressure byte (0..255)
- Report length: 8 bytes

This is intentionally generic for broad compatibility with host HID stacks.

Future improvements:
- Add in-range barrel switch/eraser usages
- Add report IDs for alternate modes
- Add optional Wacom-like descriptor variant for better app heuristics
