#! /bin/bash
#******************************************************************************************************************
#     COMPILE BACKPORTED KERNEL MODULES
#******************************************************************************************************************
# as normal user on linux pc terminal:

echo
echo -------------------------------------------------------------------------------
echo BUILDING BACKPORTS
echo -------------------------------------------------------------------------------
echo
sleep 1

script=`readlink -f "$0"`
# Absolute path this script is in
project=`dirname "$script"`
echo $project
source "$project"/env_setup
PATH=${AM1808_COMPILER}:$PATH
PATH=${AM1808_UBOOT_DIR}/tools:$PATH
BACKPORTS=./backports-3.11-rc3-1
cp LEGOExtra.config ${BACKPORTS}/.config
cd ${BACKPORTS}
make ARCH=arm CROSS_COMPILE=arm-none-linux-gnueabi- KLIB=../modules/lib/modules/2.6.33-rc4/ clean
make ARCH=arm CROSS_COMPILE=arm-none-linux-gnueabi- KLIB=../modules/lib/modules/2.6.33-rc4/
DEST=`echo ../modules/lib/modules/*/kernel/`
sudo mkdir -p $DEST/compat
sudo cp compat/*.ko $DEST/compat
sudo mkdir -p $DEST/net/mac80211
sudo cp net/mac80211/*.ko $DEST/net/mac80211
sudo mkdir -p $DEST/net/wireless
sudo cp net/wireless/*.ko $DEST/net/wireless
sudo mkdir -p $DEST/drivers/net/wireless/ath
sudo cp drivers/net/wireless/ath/*.ko $DEST/drivers/net/wireless/ath
sudo mkdir -p $DEST/drivers/net/wireless/ath/ath9k
sudo cp drivers/net/wireless/ath/ath9k/*.ko $DEST/drivers/net/wireless/ath/ath9k

