#! /bin/bash
# Create a set of files to allow the easy creation of leJOS SD card images
# Created by Andy Shaw

img=lejosimage
rm -rf $img 2> /dev/null
mkdir $img 2> /dev/null
cp scripts/* $img
cp -r lejosfs $img
cp external/uImage $img
cp external/lmsfs.tar.bz2 $img 
cp -r external/Linux_AM1808 $img 
cp -r external/netmods $img
cp ../modules/net/bin/* $img/netmods
mkdir $img/mod
cp ../modules/lms2012/bin/* $img/mod
dpkg-deb -x external/libjna* $img/libjna
dpkg-deb -x external/libffi* $img/libjna
cp ../ev3classes/ev3classes.jar $img
cp ../EV3HelloWorld/bin/EV3HelloWorld.class $img/lejosfs/lejos/samples
cp ../EV3Splash/bin/Splash.class $img/lejosfs/lejos/bin/utils
cp ../EV3PowerOff/bin/PowerOff.class $img/lejosfs/lejos/bin/utils
tar cfj lejosimage.bz2 lejosimage
