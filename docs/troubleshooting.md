# Troubleshooting

## PC does not detect device
- Ensure START succeeded in app status output.
- Verify gadget bind: `cat /sys/kernel/config/usb_gadget/opentablet/UDC`
- Confirm host cable supports data.

## START fails with permission errors
- Confirm root manager granted `su` for app.
- Try permissive SELinux (test only): `setenforce 0`

## `/dev/hidg0` missing
- HID function creation likely failed.
- Check module support in `/proc/modules`.

## OpenTabletDriver sees mouse-like behavior
- Ensure absolute mode descriptor is used (default in this project).
- Configure area mapping inside OTD.
