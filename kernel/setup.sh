#! /bin/bash
# this script should be used to setup the leJOS krnel build environment prior
# to executing either of the build* scripts. It extracts the standard kernel
# and backports tree and then patches them for use with leJOS.
# Andy Shaw

# extract kernel source and patch it for use with lejos
tar xfa linux*.tgz
cd linux*
patch -p1 < ../lx.patch

# do the same for the backported network modules
tar xfa backports*.bz2
cd backports*
patch -p1 < ../bp.patch
