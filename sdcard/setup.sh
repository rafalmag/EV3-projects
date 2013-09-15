#! /bin/bash
# Create a set of files to allow the easy creation of leJOS SD card images
# Created by Andy Shaw
LJHOME=lejosfs/home/root/lejos
img=lejosimage
rm -rf $img 2> /dev/null
mkdir $img 2> /dev/null
mkdir lejosfs/home/lejos 2> /dev/null
mkdir $LJHOME/lib 2> /dev/null
mkdir $LJHOME/libjna 2> /dev/null
mkdir $LJHOME/mod 2> /dev/null
mkdir $LJHOME/btcache 2> /dev/null
mkdir $LJHOME/samples 2> /dev/null
mkdir $LJHOME/bin/utils 2> /dev/null
cp scripts/* $img
cp -r lejosfs $img
cp ../kernel/uImage $img
cp -r ../kernel/modules $img
cp external/lmsfs.tar.bz2 $img 
cp -r external/Linux_AM1808 $img 
cp -r external/netmods $img
cp ../modules/net/bin/* $img/netmods
mkdir $img/mod
cp ../modules/lms2012/bin/* $img/mod
dpkg-deb -x external/libjna* $img/libjna
dpkg-deb -x external/libffi* $img/libjna
cd $img/lejosfs
dpkg-deb --fsys-tarfile ../../external/bridge-utils* | tar x ./usr/sbin/brctl
cd ../..
cp ../ev3classes/ev3classes.jar $img
cp ../EV3HelloWorld/bin/EV3HelloWorld.class $img/$LJHOME/samples
cp ../EV3Splash/bin/Splash.class $img/$LJHOME/bin/utils
cp ../EV3PowerOff/bin/PowerOff.class $img/$LJHOME/bin/utils
git describe > $img/version
cp readme $img
tar cfj lejosimage.bz2 lejosimage
