# OpenTablet Launcher (ROOTED Android 9+)

OpenTablet Launcher turns a rooted Android 9+ phone into a **USB HID absolute-position tablet surface**.

## What this project does
- Creates a Linux USB gadget using ConfigFS (`/sys/kernel/config/usb_gadget`).
- Exposes a HID digitizer interface (`/dev/hidg0`) to the host PC.
- Maps Android touch input to absolute 16-bit X/Y HID reports.
- Provides an ultra-minimal fullscreen “tablet mode” UI with START/STOP control.

## Not included
- No screen mirroring
- No remote desktop
- No streaming path

## Build
1. Open in Android Studio (Koala+)
2. Sync Gradle
3. Build + install on rooted device (Android 9+)

## Root and kernel requirements
See `docs/kernel_requirements.md`.

## Setup workflow
1. Connect phone to PC by USB cable.
2. Open app and verify capability report.
3. Tap **START**.
4. PC should enumerate HID tablet device.
5. Select device in OpenTabletDriver or use directly in supported software.

## Latency notes
- Uses black minimal UI and immersive fullscreen.
- Touch path is direct `MotionEvent` -> HID report bytes.
- No background analytics or cloud services.

## Safety
This app uses root shell commands and writes ConfigFS nodes. Use only on test devices you control.
