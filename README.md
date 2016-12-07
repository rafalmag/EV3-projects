EV3-projects
============

My Lego EV3 projects using Lejos

- EV3 clock - youtube preview 

[![Alt text for your video](http://img.youtube.com/vi/8ECvObJr_io/0.jpg)](http://www.youtube.com/watch?v=8ECvObJr_io)

# EV3 Preparation

https://sourceforge.net/p/lejos/wiki/Installing%20leJOS/

>http://java.com/legomindstorms redirects to http://www.oracle.com/technetwork/java/embedded/downloads/javase/javaseemeddedev3-1982511.html
, but I used http://www.oracle.com/technetwork/java/embedded/embedded-se/downloads/legomindstormev3-2200042.html to get the jrecreate parameters

1. downloaded JDK `ejdk-8-fcs-b132-linux-arm-sflt-03_mar_2014.tar.gz`
2. create jre using : `bin/jrecreate.bat --profile compact2 --dest compact2-client --vm client` 
3. rename jre folder to `ejre1.7.0_60`
4. use 7Zip to create `ejre-7u60-fcs-b19-linux-arm-sflt-headless-07_may_2014.tar` archive with above folder inside
5. use 7Zip to create `ejre-7u60-fcs-b19-linux-arm-sflt-headless-07_may_2014.gz` with above file
6. move it to SD card

# Connection (usb/wifi) configuration

Check PAN menu for ip address.
## USB

1. connect usb
2. in PAN menu check USB client - set IP address from the same pool as IP on PC (USB Ethernet/RNDIS Gadget). For example 169.254.90.1 on EV3 and 169.254.90.196 on PC.
3. exit menu - the ip will be set

## Wifi

Entering password from EV3 menu is not the best option, so better hardcode some network settings.

1. Use `ssh` to connect to EV3  - user root, password blank.
2. use "wpa_passphrase" to generate a passkey
3. create a `/home/root/lejos/bin/utils/wpa_supplicant.conf`, example: 

```
ctrl_interface=/var/run/wpa_supplicant

network={
        ssid="mySSID"
		key_mgmt=WPA-PSK
        psk=myEncryptedPassword
}
```

First line is taken from `/home/root/lejos/bin/utils/base_wpa_supplicant.conf`

Based on:
https://sourceforge.net/p/lejos/wiki/Configuring%20Wifi/


