package org.lejos.sample.graphicssample;

import java.io.File;
import java.io.FileInputStream;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import lejos.hardware.Button;
import lejos.hardware.DeviceManager;
import lejos.hardware.LCD;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.UARTPort;
import lejos.hardware.sensor.I2CSensor;
import lejos.internal.ev3.EV3I2CPort;
import lejos.internal.ev3.EV3UARTPort;
import lejos.utility.Delay;

/**
 * Demonstrate various leJOS graphics techniques.
 */
public class GraphicsSample extends Thread
{

    Graphics g = new Graphics();
    final int SW = LCD.SCREEN_WIDTH;
    final int SH = LCD.SCREEN_HEIGHT;
    final int DELAY = 2000;
    final int TITLE_DELAY = 1000;
    Image duke = new Image(100, 64, new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x1c, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x1e, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1e, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x60, (byte) 0x3e, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xbe, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xf0, (byte) 0xbe, (byte) 0x07, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xfd, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x01, (byte) 0xe0, (byte) 0xff, (byte) 0x07, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, 
            (byte) 0xe0, (byte) 0xff, (byte) 0x07, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0xc0, (byte) 0xff, 
            (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x1f, (byte) 0xc0, (byte) 0xff, (byte) 0x07, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3f, (byte) 0x80, 
            (byte) 0xff, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x7f, (byte) 0x00, (byte) 0xff, (byte) 0x07, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xe1, (byte) 0xff, 
            (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0xf3, (byte) 0xff, (byte) 0x07, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xf7, 
            (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0xef, (byte) 0xfe, (byte) 0x01, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xf8, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x1f, (byte) 0xe0, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x3f, (byte) 0xc0, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x7f, 
            (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x7f, (byte) 0xc0, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0xff, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xc1, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0xff, (byte) 0xc0, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xfd, 
            (byte) 0xc3, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x3f, (byte) 0xea, (byte) 0x7f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5f, 
            (byte) 0x55, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x80, (byte) 0x8f, (byte) 0xf8, (byte) 0x3c, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x80, 
            (byte) 0x57, (byte) 0x55, (byte) 0x3c, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0xaf, (byte) 0xea, 
            (byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x80, (byte) 0x55, (byte) 0x55, (byte) 0x38, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc0, (byte) 0xad, 
            (byte) 0x7a, (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xc0, (byte) 0x5d, (byte) 0x15, (byte) 0x30, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc0, 
            (byte) 0xb9, (byte) 0x1e, (byte) 0x60, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xf0, (byte) 0x07, 
            (byte) 0x60, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0x80, (byte) 0x00, (byte) 0xe0, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf8, (byte) 0x00, 
            (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xf8, (byte) 0x00, (byte) 0x00, (byte) 0xc0, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xcc, 
            (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x01, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x6c, (byte) 0x00, (byte) 0x00, 
            (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x6c, (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x01, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x6c, (byte) 0x00, 
            (byte) 0x00, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x6c, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x78, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x38, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x38, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3c, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x3c, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x1e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1f, (byte) 0x00, 
            (byte) 0x0c, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x1f, (byte) 0x80, (byte) 0x7f, (byte) 0x00, 
            (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1f, 
            (byte) 0xe0, (byte) 0xff, (byte) 0x00, (byte) 0x06, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x18, (byte) 0xf0, (byte) 0x80, 
            (byte) 0x01, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x18, (byte) 0x38, (byte) 0x00, (byte) 0x03, (byte) 0x06, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x18, (byte) 0x1c, 
            (byte) 0x00, (byte) 0x06, (byte) 0x07, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x18, (byte) 0x07, (byte) 0x00, (byte) 0x0e, 
            (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x98, 
            (byte) 0x03, (byte) 0x00, (byte) 0x0c, (byte) 0x03, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf8, (byte) 0x01, (byte) 0x00, 
            (byte) 0x18, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x01, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xe0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, });
    
    Image logo = new Image(52, 64, new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, 
            (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xff, (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xcc, 
            (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0xcc, (byte) 0xcc, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x33, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x30, (byte) 0x33, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0xcc, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xcc, 
            (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x30, (byte) 0x33, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x33, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xcc, (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0xcc, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x30, 
            (byte) 0x33, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x30, (byte) 0x33, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0xcc, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xcc, (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, 
            (byte) 0x3f, (byte) 0x00, (byte) 0x00, (byte) 0xc0, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xff, (byte) 0x3f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xc0, (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0xff, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xfc, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x03, (byte) 0xfc, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0x03, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0x00, (byte) 0x00, });
    
    void splash()
    {
        g.clear();
        g.setFont(Font.getLargeFont());
        g.drawString("Lego EV3", SW/2, SH/2, Graphics.BASELINE|Graphics.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        g.drawString("+", SW/2, SH/2, Graphics.BASELINE|Graphics.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        g.drawString("Java", SW/2, SH/2, Graphics.BASELINE|Graphics.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        g.drawString("=", SW/2, SH/2, Graphics.BASELINE|Graphics.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();       
        //g.setFont(Font.getDefaultFont());
        g.drawRegion(logo, 0, 0, logo.getWidth(), logo.getHeight(), 0, SW / 2, SH / 4+10, Graphics.HCENTER | Graphics.VCENTER);
        g.drawString("leJOS/EV3", SW/2, 3*SH/4+10, Graphics.BASELINE|Graphics.HCENTER);
        //g.drawString("Preview", SW/2, 3*SH/4+24, Graphics.BASELINE|Graphics.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY*2);
        g.setFont(Font.getDefaultFont());
        g.clear();
    }
   
    
    void titles()
    {
        LCD.setContrast(0);
        g.setFont(Font.getLargeFont());
        //g.drawString("leJOS", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        g.drawString("Graphics", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        LCD.refresh();
        g.setFont(Font.getDefaultFont());
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        LCD.refresh();
        LCD.setContrast(0x60);
    }
    
    void buttons()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("Buttons", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        g.setFont(Font.getDefaultFont());
        for(;;)
        {
            int but = Button.waitForAnyPress(TITLE_DELAY);
            g.clear();
            String pressed = "";
            if (but == 0)
                pressed = "None";
            if ((but & Button.ID_ENTER) != 0)
                pressed += "Enter ";
            if ((but & Button.ID_LEFT) != 0)
                pressed += "Left ";
            if ((but & Button.ID_RIGHT) != 0)
                pressed += "Right ";
            if ((but & Button.ID_UP) != 0)
                pressed += "Up ";
            if ((but & Button.ID_DOWN) != 0)
                pressed += "Down ";
            if ((but & Button.ID_ESCAPE) != 0)
                pressed += "Escape ";
            g.drawString(pressed, SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
            if (but == Button.ID_ESCAPE)
                break;
            
        }
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        LCD.refresh();
    }    
    
    void leds()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("LEDS", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.setFont(Font.getDefaultFont());
        for(int i = 1; i < 10; i++)
        {
            g.clear();
            g.drawString("Pattern " + i, SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
            LCD.refresh();
            Button.LEDPattern(i);
            Button.waitForAnyPress(DELAY*2);
            
        }
        Button.LEDPattern(0);
        g.clear();
        LCD.refresh();
    }

    void displaySensorValues(UARTPort p)
    {
        g.setFont(Font.getLargeFont());
        g.drawString(p.getModeName(0), SW/2, SH/4, Graphics.BOTTOM|Graphics.HCENTER);
        for(int i = 0; i < 20; i++)
        {
            g.clear();
            g.setFont(Font.getDefaultFont());
            g.drawString(p.getModeName(0), SW/2, SH/4, Graphics.BOTTOM|Graphics.HCENTER);
            g.setFont(Font.getLargeFont());
            g.drawString(p.toString(), SW/2, 3*SH/4, Graphics.BOTTOM|Graphics.HCENTER);
            LCD.refresh();
            Button.waitForAnyPress(500);
        }
        
    }
    
    void sensorPorts()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("Sensors", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.setFont(Font.getDefaultFont());
        int chHeight = g.getFont().getHeight();
        int chWidth = g.getFont().stringWidth("M");
        int [] current = new int[DeviceManager.PORTS];
        UARTPort[] uarts = new UARTPort[DeviceManager.PORTS];
        I2CPort[] i2c = new I2CPort[DeviceManager.PORTS];
        DeviceManager dm = new DeviceManager();
        int devCnt = 0;
        while(devCnt < DeviceManager.PORTS)
        {
            g.clear();
            devCnt = 0;
            for(int i = 0; i < DeviceManager.PORTS; i++)
            {
                //System.out.println("Type " + i + " is " + current[i]);
                g.drawString("Port "+i, 0, i*chHeight*2, 0);
                g.drawString(dm.getPortTypeName(current[i]), 8*chWidth,i*chHeight*2, 0);
                if (uarts[i] != null)
                {
                    g.drawString(uarts[i].getModeName(0), 2*chWidth, (2*i + 1)*chHeight, 0);
                    devCnt++;
                }
                else if (i2c[i] != null)
                {
                    I2CSensor s = new I2CSensor(i2c[i]);
                    g.drawString(s.getProductID(), 2*chWidth, (2*i + 1)*chHeight, 0);
                }
                else
                    g.drawString("Unknown", 2*chWidth, (2*i + 1)*chHeight, 0);
                    
            }
            LCD.refresh();
            // Check for changes
            for(int i = 0; i < DeviceManager.PORTS; i++)
            {
                int typ = dm.getPortType(i);
                //System.out.println("Type " + typ);
                if (current[i] != typ)
                {
                    System.out.println("Changed to " + typ);
                    current[i] = typ;
                    if (typ == DeviceManager.CONN_INPUT_UART)
                    {
                        System.out.println("Open port " + i);
                        UARTPort u = new EV3UARTPort();
                        if (u.open(i))
                            uarts[i] = u;
                        break;
                    }
                    else if (typ == DeviceManager.CONN_NXT_IIC)
                    {
                        I2CPort ii = new EV3I2CPort();
                        ii.open(i);
                        i2c[i] = ii;
                    }
                    else
                    {
                        if (uarts[i] != null)
                            uarts[i].close();
                        if (i2c[i] != null)
                            i2c[i].close();
                        uarts[i] = null;
                        i2c[i] = null;
                    }
                }
            }
        }
        Button.waitForAnyPress(DELAY*2);
        displaySensorValues(uarts[0]);
        displaySensorValues(uarts[3]);
        g.clear();
        LCD.refresh();
    }

    public void run()
    {
        System.out.println("Thread running");
        File f = new File("popcorn.wav");
        Sound.playSample(f, 100);
        //g.clear();
    }
    
    void sound()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("Sound", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.setFont(Font.getDefaultFont());
        g.clear();
        g.drawString("Tones", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        LCD.refresh();
        Button.waitForAnyPress(DELAY/2);
        Sound.setVolume(50);
        Sound.beepSequenceUp();
        Button.waitForAnyPress(DELAY/2);
        Sound.beepSequence();
        Button.waitForAnyPress(DELAY/2);
        Sound.twoBeeps();
        Button.waitForAnyPress(DELAY/2);
        Sound.setVolume(100);
        g.clear();
        g.drawString("Wav files", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        //this.setDaemon(true);
        this.start();
        Button.waitForAnyPress(DELAY*2);
        g.clear();
        g.drawString("Popcorn anyone?", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        Button.waitForAnyPress(DELAY*2);
        
        g.clear();
        LCD.refresh();
    }

    void displayTacho(NXTRegulatedMotor m)
    {
        g.clear();
        while(m.isMoving())
        {
            g.clear();
            g.drawString("Position: " + m.getTachoCount(), SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);            
            LCD.refresh();
        }
        Button.waitForAnyPress(DELAY);
        g.clear();
    }
    
    void motors()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("Motors", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        g.setFont(Font.getDefaultFont());
        NXTRegulatedMotor ma = Motor.A;
        ma.resetTachoCount();
        ma.setAcceleration(600);
        ma.setSpeed(500);
        g.drawString("Forward 720", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        Button.waitForAnyPress(DELAY);
        ma.rotate(720, true);
        displayTacho(ma);
        g.drawString("Backward 720", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        Button.waitForAnyPress(DELAY);
        ma.rotate(-720, true);
        displayTacho(ma);
        ma.setSpeed(80);
        g.drawString("Slow Forward 360", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        Button.waitForAnyPress(DELAY);
        ma.rotate(360, true);
        displayTacho(ma);
        ma.setSpeed(800);
        g.drawString("Fast Backward 360", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        Button.waitForAnyPress(DELAY);
        ma.rotate(-360, true);
        displayTacho(ma);        
    }
    
    void credits()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("leJOS/EV3", SW/2, SH/2, Graphics.BOTTOM|Graphics.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY*100);
        g.setFont(Font.getDefaultFont());        
    }

    void displayTitle(String text)
    {
        g.clear();
        g.drawString(text, SW / 2, SH / 2, Graphics.HCENTER | Graphics.BASELINE);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
    }

    void characterSet()
    {
        displayTitle("Character Set");
        int chHeight = g.getFont().getHeight();
        int chWidth = g.getFont().stringWidth("M");
        for(int l = 0; l < 8; l++)
            for(int c = 0; c < 16; c++)
                g.drawChar((char)(l*16 + c), c*chWidth, l*chHeight - 8, 0);
        Button.waitForAnyPress(DELAY);
    }

    void textAnchors()
    {
        displayTitle("Text Anchors");
        int chHeight = g.getFont().getHeight();
        g.drawString("Left", SW / 2, 0, Graphics.LEFT);
        g.drawString("Center", SW / 2, chHeight, Graphics.HCENTER);
        g.drawString("Right", SW / 2, chHeight * 2, Graphics.RIGHT);
        g.drawString("Left", SW / 2, chHeight * 4, Graphics.LEFT, true);
        g.drawString("Center", SW / 2, chHeight * 5, Graphics.HCENTER, true);
        g.drawString("Right", SW / 2, chHeight * 6, Graphics.RIGHT, true);
        Button.waitForAnyPress(DELAY);
    }

    void fonts()
    {
        displayTitle("Fonts");
        g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
        g.drawString("Small", SW / 2, 16, Graphics.HCENTER | Graphics.BASELINE);
        g.setFont(Font.getFont(0, 0, Font.SIZE_MEDIUM));
        g.drawString("Medium", SW / 2, 48, Graphics.HCENTER | Graphics.BASELINE);
        g.setFont(Font.getFont(0, 0, Font.SIZE_LARGE));
        g.drawString("Large", SW / 2, 96, Graphics.HCENTER | Graphics.BASELINE);
        g.setFont(Font.getDefaultFont());
        Button.waitForAnyPress(DELAY);
    }

    void rotatedText()
    {
        displayTitle("Rotated Text");
        Font large = Font.getFont(0, 0, Font.SIZE_LARGE);
        Image base = Image.createImage(SW, large.getHeight());
        Graphics bg = base.getGraphics();
        bg.setFont(large);
        bg.drawString("Top", SW / 2, 0, Graphics.HCENTER);
        g.drawImage(base, 0, 0, 0);
        bg.clear();
        bg.drawString("Bottom", SW / 2, 0, Graphics.HCENTER);
        Image rotImage = Image.createImage(base, 0, 0, SW, base.getHeight(), Sprite.TRANS_ROT180);
        g.drawImage(rotImage, 0, SH - 1, Graphics.BOTTOM);
        bg.clear();
        bg.drawString("Left", SH / 2, 0, Graphics.HCENTER);
        rotImage = Image.createImage(base, 0, 0, SH, base.getHeight(), Sprite.TRANS_ROT270);
        g.drawImage(rotImage, 0, 0, 0);
        bg.clear();
        bg.drawString("Right", SH / 2, 0, Graphics.HCENTER);
        rotImage = Image.createImage(base, 0, 0, SH, base.getHeight(), Sprite.TRANS_ROT90);
        g.drawImage(rotImage, SW - 1, 0, Graphics.RIGHT);
        Button.waitForAnyPress(DELAY);
    }

    void fileImage() throws Exception
    {
        displayTitle("File image");
        Image img = Image.createImage(new FileInputStream(new File("arm.lni")));
        g.drawRegion(img, 0, 0, SW, SH, Sprite.TRANS_NONE, SW / 2, SH / 2, Graphics.HCENTER | Graphics.VCENTER);
        Button.waitForAnyPress(DELAY);
    }

    void lines()
    {
        displayTitle("Lines");
        for (int i = 1; i < SH / 2; i += 4)
        {
            g.drawLine(i - 4, i, SW - i, i);
            g.drawLine(SW - i, i, SW - i, SH - i);
            g.drawLine(i, SH - i, SW - i, SH - i);
            g.drawLine(i, i + 4, i, SH - i);
        }
        g.drawLine(0, 0, SW, SH);
        g.drawLine(SW, 0, 0, SH);
        Button.waitForAnyPress(DELAY);
    }

    void rectangles()
    {
        displayTitle("Rectangles");
        for (int i = 1; i < 7; i++)
            g.drawRect(i * 25 - 10, 20 - 2 * i, i * 4, i * 4);
        for (int i = 1; i < 7; i++)
            g.fillRect(i * 25 - 10, 80 - 2 * i, i * 4, i * 4);
        Button.waitForAnyPress(DELAY);
    }

    void circles()
    {
        displayTitle("Circles");
        for (int i = 1; i < 7; i++)
            g.drawArc(i * 25 - 10, 20 - 2 * i, i * 4, i * 4, 0, 360);
        for (int i = 1; i < 7; i++)
            g.fillArc(i * 25 - 10, 80 - 2 * i, i * 4, i * 4, 0, 360);
        Button.waitForAnyPress(DELAY);
    }

    void scroll()
    {
        displayTitle("Scrolling");
        int line = g.getFont().getHeight();
        g.drawString("Hello from leJOS", SW / 2, SH - line, Graphics.HCENTER);
        g.setColor(Graphics.WHITE);
        for (int i = 0; i < 7; i++)
        {
            Delay.msDelay(250);
            g.copyArea(0, SH - (i+1) * line, SW, line, 0, SH - (i+2)*line, 0);
            g.fillRect(0, SH - (i+1) * line, SW, line);
        }
        for (int i = 6; i >= 0; i--)
        {
            Delay.msDelay(250);
            g.copyArea(0, SH - (i + 2) * line, SW, line, 0, SH - (i + 1) * line, 0);
            g.fillRect(0, SH - (i + 2) * line, SW, line);
        }
        Button.waitForAnyPress(DELAY);
        LCD.setAutoRefresh(false);
        for (int i = 0; i < 7*line; i++)
        {
            Delay.msDelay(10);
            g.copyArea(0, SH - line - i, SW, line, 0, SH - line - (i + 1), 0);
            g.fillRect(0, SH - i, SW, 1);
            LCD.refresh();
        }
        for (int i = 7*line - 1; i >= 0; i--)
        {
            Delay.msDelay(10);
            g.copyArea(0, SH - line - (i + 1), SW, line, 0, SH - line - i, 0);
            g.fillRect(0, SH - line - (i + 1), SW, 1);
            LCD.refresh();
        }
        LCD.setAutoRefresh(true);
        LCD.refresh();
        Button.waitForAnyPress(DELAY);
        g.setColor(Graphics.BLACK);
    }

    void image(int transform, String title)
    {
        displayTitle(title);
        g.drawRegion(duke, 0, 0, duke.getWidth(), duke.getHeight(), transform, SW / 2, SH / 2, Graphics.HCENTER | Graphics.VCENTER);
        Button.waitForAnyPress(DELAY);
    }

    void images()
    {
        displayTitle("Image Display");
        image(Sprite.TRANS_NONE, "Normal");
        image(Sprite.TRANS_ROT90, "Rotate 90");
        image(Sprite.TRANS_ROT180, "Rotate 180");
        //image(Sprite.TRANS_ROT270, "Rotate 270");
        image(Sprite.TRANS_MIRROR, "Mirror");
        //image(Sprite.TRANS_MIRROR_ROT90, "Mirror 90");
        //image(Sprite.TRANS_MIRROR_ROT180, "Mirror 180");
        //image(Sprite.TRANS_MIRROR_ROT270, "Mirror 270");
    }

    void animation()
    {
        displayTitle("Animation");
        Image arms = new Image(216, 33, new byte[] {(byte) 0x00, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3c, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x18, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40, 
                (byte) 0x1c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40, 
                (byte) 0x3c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x3e, (byte) 0x0f, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x3c, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0xe0, (byte) 0x38, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x3e, 
                (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x3e, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0x79, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x60, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0xbe, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x3e, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x3d, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xbd, (byte) 0x0f, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x3e, (byte) 0x0f, 
                (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0x3e, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x3c, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe0, 
                (byte) 0xff, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0x9f, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0x3f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x70, 
                (byte) 0x3e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf8, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0xe0, (byte) 0xff, (byte) 0x07, (byte) 0x00, 
                (byte) 0x00, (byte) 0xe0, (byte) 0xff, (byte) 0x07, (byte) 0x00, 
                (byte) 0x00, (byte) 0xe0, (byte) 0x3d, (byte) 0x0f, (byte) 0x00, 
                (byte) 0x00, (byte) 0x78, (byte) 0x1f, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x78, (byte) 0x1e, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xff, 
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x9f, 
                (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x78, (byte) 0x1f, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7c, (byte) 0x3f, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, 
                (byte) 0xc0, (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
                (byte) 0xe0, (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf8, (byte) 0x9f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0xfc, (byte) 0x1f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x78, (byte) 0x00, (byte) 0x80, (byte) 0xff, (byte) 0x03, 
                (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xff, (byte) 0x01, 
                (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xff, (byte) 0x07, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf8, (byte) 0xaf, (byte) 0x03, 
                (byte) 0x00, (byte) 0x00, (byte) 0xbc, (byte) 0x0f, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x7c, (byte) 0x00, (byte) 0x80, 
                (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xe0, 
                (byte) 0xff, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0xe2, 
                (byte) 0xff, (byte) 0x01, (byte) 0x00, (byte) 0x80, (byte) 0xf8, 
                (byte) 0xef, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xfe, 
                (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3e, 
                (byte) 0x02, (byte) 0xf8, (byte) 0xff, (byte) 0x03, (byte) 0x00, 
                (byte) 0x00, (byte) 0xfe, (byte) 0xff, (byte) 0x01, (byte) 0x00, 
                (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00, 
                (byte) 0xe0, (byte) 0xfd, (byte) 0xff, (byte) 0x03, (byte) 0x00, 
                (byte) 0x70, (byte) 0xfe, (byte) 0x47, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x9e, (byte) 0x0f, (byte) 0xfc, (byte) 0xff, 
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, 
                (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xff, (byte) 0xff, 
                (byte) 0x01, (byte) 0x00, (byte) 0xf0, (byte) 0xfe, (byte) 0x73, 
                (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0xdf, (byte) 0x0f, 
                (byte) 0xfc, (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
                (byte) 0xfe, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0xc0, 
                (byte) 0xff, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00, (byte) 0x1c, 
                (byte) 0xff, (byte) 0x07, (byte) 0xb8, (byte) 0xff, (byte) 0x01, 
                (byte) 0x00, (byte) 0x00, (byte) 0xfc, (byte) 0xff, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0xfa, (byte) 0x7f, (byte) 0x00, 
                (byte) 0x00, (byte) 0xc0, (byte) 0xff, (byte) 0x3f, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x01, 
                (byte) 0x00, (byte) 0xbc, (byte) 0xff, (byte) 0x03, (byte) 0x81, 
                (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0xe0, 
                (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xf8, 
                (byte) 0x3f, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0xff, 
                (byte) 0x1f, (byte) 0x00, (byte) 0x01, (byte) 0xc0, (byte) 0xff, 
                (byte) 0xff, (byte) 0x10, (byte) 0x00, (byte) 0xfc, (byte) 0xff, 
                (byte) 0x03, (byte) 0x01, (byte) 0x7e, (byte) 0x00, (byte) 0x00, 
                (byte) 0x10, (byte) 0xc0, (byte) 0x7f, (byte) 0x00, (byte) 0x00, 
                (byte) 0x01, (byte) 0xf0, (byte) 0x3f, (byte) 0x00, (byte) 0x10, 
                (byte) 0x00, (byte) 0xfe, (byte) 0x0f, (byte) 0x00, (byte) 0x01, 
                (byte) 0xc0, (byte) 0xff, (byte) 0x1f, (byte) 0x10, (byte) 0x00, 
                (byte) 0xfc, (byte) 0xff, (byte) 0x00, (byte) 0x03, (byte) 0x3c, 
                (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x80, (byte) 0x0f, 
                (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0xe0, (byte) 0x5f, 
                (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0xfe, (byte) 0x0f, 
                (byte) 0x00, (byte) 0x03, (byte) 0x80, (byte) 0xff, (byte) 0x0f, 
                (byte) 0x30, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0x1f, 
                (byte) 0x07, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0x70, 
                (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x07, 
                (byte) 0xe0, (byte) 0x1f, (byte) 0x00, (byte) 0x70, (byte) 0x00, 
                (byte) 0xfc, (byte) 0x07, (byte) 0x00, (byte) 0x07, (byte) 0x80, 
                (byte) 0xff, (byte) 0x03, (byte) 0x70, (byte) 0x00, (byte) 0xf8, 
                (byte) 0xff, (byte) 0x3f, (byte) 0x0f, (byte) 0x18, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x00, (byte) 0x03, (byte) 0x00, 
                (byte) 0x00, (byte) 0x0f, (byte) 0xc0, (byte) 0x01, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x00, (byte) 0xfc, (byte) 0x03, (byte) 0x00, 
                (byte) 0x0f, (byte) 0x80, (byte) 0xff, (byte) 0x01, (byte) 0xf0, 
                (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0x3f, (byte) 0x0f, 
                (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x00, 
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0xc0, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x00, (byte) 0x9c, 
                (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0x80, (byte) 0xff, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
                (byte) 0x1f, (byte) 0x1f, (byte) 0x18, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
                (byte) 0x1f, (byte) 0xe0, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0x01, (byte) 0x0c, (byte) 0x00, (byte) 0x00, (byte) 0x1f, 
                (byte) 0x80, (byte) 0xff, (byte) 0x00, (byte) 0xf0, (byte) 0x01, 
                (byte) 0xf0, (byte) 0xff, (byte) 0x01, (byte) 0x3f, (byte) 0x18, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x83, (byte) 0x01, 
                (byte) 0x00, (byte) 0x00, (byte) 0x3f, (byte) 0x60, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x03, (byte) 0x0e, (byte) 0x00, 
                (byte) 0x00, (byte) 0x3f, (byte) 0xc0, (byte) 0x01, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x03, (byte) 0xf0, (byte) 0xff, (byte) 0x00, 
                (byte) 0x1f, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0x81, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x1f, 
                (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x01, 
                (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x1f, (byte) 0xe0, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x01, (byte) 0xf8, 
                (byte) 0x3f, (byte) 0x00, (byte) 0x7f, (byte) 0x0c, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x87, (byte) 0x01, (byte) 0x00, 
                (byte) 0x00, (byte) 0x7f, (byte) 0x30, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x87, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
                (byte) 0x7f, (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0x07, (byte) 0xbc, (byte) 0x1f, (byte) 0x00, (byte) 0x7d, 
                (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xd0, (byte) 0xf7, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7d, (byte) 0x1e, 
                (byte) 0x00, (byte) 0x00, (byte) 0xd0, (byte) 0xf7, (byte) 0x01, 
                (byte) 0x00, (byte) 0x00, (byte) 0xfd, (byte) 0x1f, (byte) 0x00, 
                (byte) 0x00, (byte) 0xd0, (byte) 0x77, (byte) 0x0e, (byte) 0x00, 
                (byte) 0x00, (byte) 0x6a, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
                (byte) 0xa0, (byte) 0xf6, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0xea, (byte) 0x1e, (byte) 0x00, (byte) 0x00, (byte) 0xa0, 
                (byte) 0xfe, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xea, 
                (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xa0, (byte) 0xfe, 
                (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x9f, (byte) 0x07, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x79, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x9f, (byte) 0x07, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x79, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x9f, (byte) 0x07, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0xf9, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
                (byte) 0x8a, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0xa0, 
                (byte) 0x78, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8a, 
                (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0xa0, (byte) 0x78, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8a, (byte) 0x07, 
                (byte) 0x00, (byte) 0x00, (byte) 0xa0, (byte) 0x78, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x1d, (byte) 0x07, (byte) 0x00, 
                (byte) 0x00, (byte) 0xd0, (byte) 0x71, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x1d, (byte) 0x07, (byte) 0x00, (byte) 0x00, 
                (byte) 0xd0, (byte) 0x71, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x1d, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0xd0, 
                (byte) 0x71, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a, 
                (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0xa0, (byte) 0x70, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x07, 
                (byte) 0x00, (byte) 0x00, (byte) 0xa0, (byte) 0x70, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x07, (byte) 0x00, 
                (byte) 0x00, (byte) 0xa0, (byte) 0x70, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, });
        final int AW = 36;
        final int AH = 33;
        final int XPOS = (SW - duke.getWidth())/2;
        final int YPOS = (SH - duke.getHeight())/2;
        LCD.setAutoRefresh(false);
        for (int i = 0; i <= SH; i++)
        {
            g.clear();
            g.drawImage(duke, XPOS, YPOS + i - SH, 0);
            LCD.refresh();
            Delay.msDelay(20);
        }
        Delay.msDelay(1000);
        for (int wavecnt = 0; wavecnt < 3; wavecnt++)
        {
            for (int i = 0; i < 6; i++)
            {
                g.drawRegion(arms, AW * i, 0, AW, AH, 0, XPOS+51, YPOS, 0);
                LCD.refresh();
                Delay.msDelay(50);
            }
            for (int i = 7 - 1; i >= 0; i--)
            {
                g.drawRegion(arms, AW * i, 0, AW, AH, 0, XPOS+51, YPOS, 0);
                LCD.refresh();
                Delay.msDelay(50);
            }
            g.drawRegion(duke, 51, 0, AW, AH, 0, XPOS+51, YPOS, 0);
            LCD.refresh();
            Delay.msDelay(50);
        }

        Delay.msDelay(1000);
        // Remove the image using a split display...
        for (int i = 0; i < SW; i++)
        {
            g.drawRegionRop(duke, 0, 0, SW, SH, XPOS-i, YPOS, 0, 0x55aa00);
            g.drawRegionRop(duke, 0, 0, SW, SH, XPOS+i, YPOS, 0, 0xaa5500);
            LCD.refresh();
            //Delay.msDelay(20);
        }
        LCD.setAutoRefresh(true);
        LCD.refresh();
        Button.waitForAnyPress(DELAY);
    }

    public static void main(String[] options) throws Exception
    {
        EV3UARTPort.resetAll();
        GraphicsSample sample = new GraphicsSample();
        sample.sensorPorts();
        /*
        sample.splash();
        sample.buttons();
        sample.motors();
        sample.sound();
        sample.leds();
        sample.titles();
        //sample.characterSet();
        //sample.textAnchors();
        sample.fonts();
        sample.rotatedText();
        //sample.fileImage();
        sample.lines();
        sample.rectangles();
        sample.circles();
        //sample.scroll();
        sample.images();
        sample.animation();
        sample.credits();*/
    }
}
