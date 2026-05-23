# OpenTabletDriver Compatibility Notes

- Device enumerates as generic HID digitizer.
- In many setups OTD can bind generic HID devices and map to tablet area.
- VID/PID can be tuned in `UsbGadgetManager` for profile matching experiments.
- If OTD does not pick up full tablet semantics, test with custom OTD JSON or fallback to raw HID support.

For osu!, keep smoothing disabled in host tooling for lowest latency.
