EV3-projects
============

My Lego EV3 projects using Lejos

- EV3 clock - youtube preview 

[![Alt text for your video](http://img.youtube.com/vi/8ECvObJr_io/0.jpg)](http://www.youtube.com/watch?v=8ECvObJr_io)

# EV3 Setup

1. Downloaded JDK `ejdk-8-fcs-b132-linux-arm-sflt-03_mar_2014.tar.gz`
2. Create jre using : `bin\jrecreate.bat --profile compact2 --dest ejre1.7.0_60 --vm client` 
3. Use 7Zip to create `ejre-7u60-fcs-b19-linux-arm-sflt-headless-07_may_2014.tar` archive with above folder inside
4. Use 7Zip to create `ejre-7u60-fcs-b19-linux-arm-sflt-headless-07_may_2014.gz` with above file
5. Copy it to SD card
6. (Optional) Copy `wpa_supplicant.conf` (see below wifi setup)

Based on:
https://sourceforge.net/p/lejos/wiki/Installing%20leJOS/

>http://java.com/legomindstorms redirects to http://www.oracle.com/technetwork/java/embedded/downloads/javase/javaseemeddedev3-1982511.html
, but I used http://www.oracle.com/technetwork/java/embedded/embedded-se/downloads/legomindstormev3-2200042.html to get the jrecreate parameters.


>WARN: `compact3` (javax.naming) seems to be needed by logback: http://www.oracle.com/technetwork/java/embedded/resources/tech/compact-profiles-overview-2157132.html

# Connection configuration

Check PAN menu for ip address.

## USB

1. Connect usb cable
2. In PAN menu check USB client - set IP address from the same pool as IP on PC (USB Ethernet/RNDIS Gadget). For example `169.254.90.1` on EV3 and `169.254.90.196` on PC.
3. Exit menu - the ip will be set when PAN restarts

## Wifi

Entering password from EV3 menu is not the best option, so better hardcode some network settings.

1. Use `ssh` to connect to EV3  - user `root`, password blank.
2. Use `wpa_passphrase` to generate a passkey
3. Create a `/home/root/lejos/bin/utils/wpa_supplicant.conf`, eg.: 

```
ctrl_interface=/var/run/wpa_supplicant

network={
        ssid="mySSID"
		key_mgmt=WPA-PSK
        psk=myEncryptedPassword
}
```

> First line is taken from `/home/root/lejos/bin/utils/base_wpa_supplicant.conf`

Based on:
https://sourceforge.net/p/lejos/wiki/Configuring%20Wifi/

# Jar upload and run

1. Check IP in `pom.xml`
2. `mvn install` - this will build, test and upload project to EV3
3. Run it from EV3 menu