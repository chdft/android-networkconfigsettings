# Network Services Settings

This app provides a GUI to change some privileged Android settings which configure network services. If available, the system API is used (AOSP only allows privileged/system apps access to this API), but a simple root-shell wrapper is available as automatic fail-over.

Since the app changes the configuration used by the OS instead of intercepting requests at runtime, there are no race-conditions. Some ROMs may however use implementations which differ from AOSP, which can cause the changes to not take effect on those ROMs.

## Settings

### Captive Portal / Network Connectivity Check
Android sends 1 HTTP and 1 HTTPS request each time a network change is detected. The target of those requests can be configured.

### NTP / Network Time
Android periodically synchronizes the system clock using NTP. The used NTP server can be configured.

## Tested Android Versions
- Android 9 (tested with AOSP and HavocOS)
- Android 10 (tested with AOSP, RessurectionRemix and HavocOS)

## Future Development

- add support for AGPS source
- redirect NCC requests made by apps, which don't use the systems connectivity manager
- expand known servers list

## Known Servers List

The app includes a hard-coded list of "known servers" from which the user may choose (manually entering a server, which is not on the list is obviously also possible). The list is not intended as recommendation, but as list of working examples - enabling users to test their ROM before setting up their own servers. If you know of other servers which you would like to see included in a "known servers"-list, please open an issue.
