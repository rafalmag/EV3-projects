#! /bin/bash
#******************************************************************************************************************
#     COMPILE KERNEL
#******************************************************************************************************************
# as normal user on linux pc terminal:

echo
echo -------------------------------------------------------------------------------
echo BUILDING KERNEL
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

cd ${AM1808_KERNEL}

make distclean ARCH=arm CROSS_COMPILE=arm-none-linux-gnueabi-

cp pru-firmware-05-31-2011-1423-v3.0/PRU_SUART_Emulation.bin PRU/

cp ${project}/LEGOBoard.config .config
cp pru-firmware-05-31-2011-1423-v3.0/PRU_SUART_Emulation.bin firmware/omapl_pru/
make ARCH=arm CROSS_COMPILE=arm-none-linux-gnueabi-

make -j4 uImage ARCH=arm CROSS_COMPILE=arm-none-linux-gnueabi-

cp arch/arm/boot/uImage ${project}/uImage


make modules ARCH=arm CROSS_COMPILE=arm-none-linux-gnueabi-
mkdir ${project}/modules 2> /dev/null
sudo bash -c "PATH=${AM1808_COMPILER}:$PATH; make modules_install INSTALL_MOD_PATH=${project}/modules ARCH=arm CROSS_COMPILE=arm-none-linux-gnueabi-"

