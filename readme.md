# Network Services Settings

This app provides a GUI to change some priviliged Android settings which configure network services. If available, the system API is used; a simple root-shell-wrapper is setup as failover.

## Settings

### Captive Portal / Network Connectivity Check
Android sends 1 HTTP and 1 HTTPS request each time a network change is detected. The target of those requests can be configured.

### NTP / Network Time
Android periodically synchronizes the system clock using NTP. The used NTP server can be configured.

## Future Development

- add support for AGPS source
- catch and redirect NCC requests made by other apps/frameworks (some to play.googleapis.com/generate_204 and www.google.com/gen_204 were observed)
