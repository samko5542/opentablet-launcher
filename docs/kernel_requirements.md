# Kernel / ROM Requirements

Required for USB HID gadget mode:

- Rooted Android with functioning `su`
- `configfs` mounted or mountable
- `/sys/kernel/config/usb_gadget` writable by root
- UDC driver available (`/sys/class/udc/*`)
- HID gadget support via:
  - built-in kernel support (`CONFIG_USB_CONFIGFS_HID=y`) OR
  - module (`usb_f_hid`, `libcomposite`)

Common failure modes:
- SELinux policy blocks writes to gadget nodes.
- Vendor kernel disables gadget composition at runtime.
- Device exposes only fixed USB function sets.
