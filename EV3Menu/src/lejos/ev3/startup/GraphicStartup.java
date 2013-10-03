package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import lejos.ev3.startup.GraphicListMenu;
import lejos.ev3.startup.Utils;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LocalEV3;
import lejos.nxt.Sound;
import lejos.util.Delay;
import lejos.ev3.startup.Config;
import lejos.ev3.startup.Settings;

public class GraphicStartup {
	
	static final String defaultProgramProperty = "lejos.default_program";
    static final String defaultProgramAutoRunProperty = "lejos.default_autoRun";
    static final String sleepTimeProperty = "lejos.sleep_time";
    static final String pinProperty = "lejos.bluetooth_pin";
	
    static final String ICMProgram = "\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0003\u00c0\u0003\u0000\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u000c\u00c0\u0003\u00c0\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u0003\u00c0\u0003\u0030\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00ff\u00cf\u00c3\u0003\u00ff\u00cf\u00c3\u0003\u0000\u0000\u00c3\u0003\u0000\u0000\u00c3\u0003\u00fc\u00f3\u00c0\u0003\u00fc\u00f3\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u00c3\u00ff\u003f\u00c0\u00c3\u00ff\u003f\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";
    static final String ICMSound = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00c0\u0000\u0003\u0000\u00c0\u0000\u0003\u0000\u00f0\u0030\u000c\u0000\u00f0\u0030\u000c\u0000\u00cc\u00c0\u0030\u0000\u00cc\u00c0\u0030\u0000\u00c3\u000c\u0033\u0000\u00c3\u000c\u0033\u00fc\u00c3\u0030\u0033\u00fc\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u003c\u00c3\u0030\u0033\u003c\u00c3\u0030\u0033\u00cc\u00cf\u0030\u0033\u00cc\u00cf\u0030\u0033\u00fc\u00f3\u0030\u0033\u00fc\u00f3\u0030\u0033\u0000\u00cf\u000c\u0033\u0000\u00cf\u000c\u0033\u0000\u00fc\u00c0\u0030\u0000\u00fc\u00c0\u0030\u0000\u00f0\u0030\u000c\u0000\u00f0\u0030\u000c\u0000\u00c0\u0000\u0003\u0000\u00c0\u0000\u0003\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    static final String ICMFile = "\u0000\u00c0\u0000\u0000\u0000\u00c0\u0000\u0000\u0000\u0030\u00ff\u000f\u0000\u0030\u00ff\u000f\u0000\u000c\u000c\u0030\u0000\u000c\u000c\u0030\u00fc\u0003\u0030\u00cc\u00fc\u0003\u0030\u00cc\u00c3\u0000\u00c0\u00f0\u00c3\u0000\u00c0\u00f0\u003f\u0000\u0000\u00c3\u003f\u0000\u0000\u00c3\u00ff\u003f\u0000\u00fc\u00ff\u003f\u0000\u00fc\u0003\u00c0\u0000\u00f0\u0003\u00c0\u0000\u00f0\u0003\u0000\u00ff\u00ff\u0003\u0000\u00ff\u00ff\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00f3\u0003\u0000\u00c0\u00cc\u0003\u0000\u00c0\u00cc\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00cc\u0003\u0000\u0000\u00cc\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00f3\u0003\u0000\u00c0\u00cc\u0003\u0000\u00c0\u00cc\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";

    static final String ICDefault = "\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0003\u00c0\u0003\u0000\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u000c\u00c0\u0003\u00c0\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u0003\u00c0\u0003\u0030\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u00f3\u00cf\u00cf\u00c3\u00f3\u00cf\u00cf\u00c3\u00c3\u000f\u0000\u00c3\u00c3\u000f\u0000\u00c3\u00f3\u00cf\u00f3\u00c0\u00f3\u00cf\u00f3\u00c0\u00f3\u000c\u0000\u00c0\u00f3\u000c\u0000\u00c0\u00f3\u00c3\u003f\u00c0\u00f3\u00c3\u003f\u00c0\u00c3\u0003\u0000\u00c0\u00c3\u0003\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";
    static final String ICProgram = "\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0003\u00c0\u0003\u0000\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u000c\u00c0\u0003\u00c0\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u0003\u00c0\u0003\u0030\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00ff\u00cf\u00c3\u0003\u00ff\u00cf\u00c3\u0003\u0000\u0000\u00c3\u0003\u0000\u0000\u00c3\u0003\u00fc\u00f3\u00c0\u0003\u00fc\u00f3\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u00c3\u00ff\u003f\u00c0\u00c3\u00ff\u003f\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";
    static final String ICFiles = "\u0000\u00c0\u0000\u0000\u0000\u00c0\u0000\u0000\u0000\u0030\u00ff\u000f\u0000\u0030\u00ff\u000f\u0000\u000c\u000c\u0030\u0000\u000c\u000c\u0030\u00fc\u0003\u0030\u00cc\u00fc\u0003\u0030\u00cc\u00c3\u0000\u00c0\u00f0\u00c3\u0000\u00c0\u00f0\u003f\u0000\u0000\u00c3\u003f\u0000\u0000\u00c3\u00ff\u003f\u0000\u00fc\u00ff\u003f\u0000\u00fc\u0003\u00c0\u0000\u00f0\u0003\u00c0\u0000\u00f0\u0003\u0000\u00ff\u00ff\u0003\u0000\u00ff\u00ff\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00f3\u0003\u0000\u00c0\u00cc\u0003\u0000\u00c0\u00cc\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00cc\u0003\u0000\u0000\u00cc\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00f3\u0003\u0000\u00c0\u00cc\u0003\u0000\u00c0\u00cc\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";
    static final String ICBlue = "\u0000\u00f0\u000f\u0000\u0000\u00f0\u000f\u0000\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00ff\u0000\u00c0\u003f\u00ff\u0003\u00c0\u003f\u00ff\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00f0\u003c\u00f0\u000f\u00f0\u003c\u00f0\u000f\u00f0\u0030\u00c3\u000f\u00f0\u0030\u00c3\u000f\u00f0\u0003\u00c3\u000f\u00f0\u0003\u00c3\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u0003\u00c3\u000f\u00f0\u0003\u00c3\u000f\u00f0\u0030\u00c3\u000f\u00f0\u0030\u00c3\u000f\u00f0\u003c\u00f0\u000f\u00f0\u003c\u00f0\u000f\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00ff\u0003\u00c0\u003f\u00ff\u0003\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00ff\u0000\u0000\u00f0\u000f\u0000\u0000\u00f0\u000f\u0000";

    static final String ICSound = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00c0\u0000\u0003\u0000\u00c0\u0000\u0003\u0000\u00f0\u0030\u000c\u0000\u00f0\u0030\u000c\u0000\u00cc\u00c0\u0030\u0000\u00cc\u00c0\u0030\u0000\u00c3\u000c\u0033\u0000\u00c3\u000c\u0033\u00fc\u00c3\u0030\u0033\u00fc\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u003c\u00c3\u0030\u0033\u003c\u00c3\u0030\u0033\u00cc\u00cf\u0030\u0033\u00cc\u00cf\u0030\u0033\u00fc\u00f3\u0030\u0033\u00fc\u00f3\u0030\u0033\u0000\u00cf\u000c\u0033\u0000\u00cf\u000c\u0033\u0000\u00fc\u00c0\u0030\u0000\u00fc\u00c0\u0030\u0000\u00f0\u0030\u000c\u0000\u00f0\u0030\u000c\u0000\u00c0\u0000\u0003\u0000\u00c0\u0000\u0003\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";

    static final String ICNXT = "\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u0030\u0000\u0000\u000c\u0030\u0000\u0000\u000c\u0030\u00ff\u00ff\u000c\u0030\u00ff\u00ff\u000c\u0030\u0003\u00c0\u000c\u0030\u0003\u00c0\u000c\u0030\u000f\u00c0\u000c\u0030\u000f\u00c0\u000c\u0030\u0033\u00c0\u000c\u0030\u0033\u00c0\u000c\u0030\u00cf\u00cc\u000c\u0030\u00cf\u00cc\u000c\u0030\u00ff\u00ff\u000c\u0030\u00ff\u00ff\u000c\u0030\u0000\u0000\u000c\u0030\u0000\u0000\u000c\u0030\u00cf\u00f3\u000c\u0030\u00cf\u00f3\u000c\u0030\u00cc\u0033\u000c\u0030\u00cc\u0033\u000c\u00f0\u00c0\u0003\u000c\u00f0\u00c0\u0003\u000c\u0030\u0033\u0000\u000c\u0030\u0033\u0000\u000c\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003";

    static final String ICLeJOS = "\u0000\u0000\u00fc\u000f\u0000\u0000\u00fc\u000f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00c0\u003f\u0000\u0000\u00c0\u003f\u0000\u0000\u00c0\u003f\u0000\u0000\u00c0\u003f\u0000\u00c0\u00cc\u003f\u0000\u00c0\u00cc\u003f\u0000\u0030\u00c3\u003f\u0000\u0030\u00c3\u003f\u0000\u00c0\u00cc\u003f\u0000\u00c0\u00cc\u003f\u00fc\u0033\u00c3\u003f\u00fc\u0033\u00c3\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00ff\u0000";

    static final String ICPower = "\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u00c0\u00cf\u00f3\u0003\u00c0\u00cf\u00f3\u0003\u00f0\u00cf\u00f3\u000f\u00f0\u00cf\u00f3\u000f\u00fc\u00c3\u00c3\u003f\u00fc\u00c3\u00c3\u003f\u00fc\u00c0\u0003\u003f\u00fc\u00c0\u0003\u003f\u00ff\u00c0\u0003\u00ff\u00ff\u00c0\u0003\u00ff\u003f\u00c0\u0003\u00fc\u003f\u00c0\u0003\u00fc\u003f\u00c0\u0003\u00fc\u003f\u00c0\u0003\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u00ff\u0000\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00fc\u0000\u0000\u003f\u00fc\u0000\u0000\u003f\u00fc\u000f\u00f0\u003f\u00fc\u000f\u00f0\u003f\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000";
    static final String ICVisibility = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u00f0\u0003\u00c0\u000f\u00f0\u0003\u00c0\u000f\u00fc\u00c0\u0003\u003f\u00fc\u00c0\u0003\u003f\u003f\u00f0\u000f\u00fc\u003f\u00f0\u000f\u00fc\u003f\u00f0\u000f\u00fc\u003f\u00f0\u000f\u00fc\u00fc\u00c0\u0003\u003f\u00fc\u00c0\u0003\u003f\u00f0\u0003\u00c0\u000f\u00f0\u0003\u00c0\u000f\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    static final String ICSearch = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00fc\u0003\u0000\u0000\u00fc\u0003\u0000\u00c0\u0003\u003c\u0000\u00c0\u0003\u003c\u0000\u00c0\u003c\u0030\u0000\u00c0\u003c\u0030\u0000\u0030\u000f\u00c0\u0000\u0030\u000f\u00c0\u0000\u0030\u0003\u00c0\u0000\u0030\u0003\u00c0\u0000\u0030\u0003\u00c0\u0000\u0030\u0003\u00c0\u0000\u0030\u0000\u00c0\u0000\u0030\u0000\u00c0\u0000\u00c0\u0000\u0030\u0000\u00c0\u0000\u0030\u0000\u00c0\u0003\u00fc\u0000\u00c0\u0003\u00fc\u0000\u0000\u00fc\u0033\u0003\u0000\u00fc\u0033\u0003\u0000\u0000\u00c0\u000c\u0000\u0000\u00c0\u000c\u0000\u0000\u0000\u0033\u0000\u0000\u0000\u0033\u0000\u0000\u0000\u003c\u0000\u0000\u0000\u003c\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    static final String ICPIN = "\u0000\u0000\u00ff\u0003\u0000\u0000\u00ff\u0003\u0000\u00c0\u0000\u000c\u0000\u00c0\u0000\u000c\u0000\u0030\u0000\u0030\u0000\u0030\u0000\u0030\u0000\u000c\u00f0\u00c3\u0000\u000c\u00f0\u00c3\u0000\u000c\u0030\u00c3\u0000\u000c\u0030\u00c3\u0000\u000c\u00f0\u00c3\u0000\u000c\u00f0\u00c3\u0000\u000c\u0000\u00f0\u0000\u000c\u0000\u00f0\u0000\u000c\u0000\u00cc\u0000\u000c\u0000\u00cc\u0000\u0033\u0030\u0033\u0000\u0033\u0030\u0033\u00c0\u000c\u00cc\u000c\u00c0\u000c\u00cc\u000c\u0030\u00c3\u00ff\u0003\u0030\u00c3\u00ff\u0003\u00cc\u00c0\u0000\u0000\u00cc\u00c0\u0000\u0000\u0033\u00fc\u0000\u0000\u0033\u00fc\u0000\u0000\u000f\u000c\u0000\u0000\u000f\u000c\u0000\u0000\u0003\u000f\u0000\u0000\u0003\u000f\u0000\u0000\u00ff\u0000\u0000\u0000\u00ff\u0000\u0000\u0000";
    
    static final String ICDelete = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00c0\u000f\u0000\u0000\u00c0\u000f\u0000\u00c0\u00ff\u00ff\u000f\u00c0\u00ff\u00ff\u000f\u0030\u0000\u0000\u0030\u0030\u0000\u0000\u0030\u00c0\u00ff\u00ff\u000f\u00c0\u00ff\u00ff\u000f\u00c0\u0000\u0000\u000c\u00c0\u0000\u0000\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u000c\u000f\u00c0\u00cc\u000c\u000f\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u000c\u0030\u000f\u00c0\u000c\u0030\u000f\u00c0\u00c0\u00cc\u000c\u00c0\u00c0\u00cc\u000c\u0000\u00ff\u00ff\u0003\u0000\u00ff\u00ff\u0003\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    static final String ICFormat = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u0003\u00c0\u0000\u0000\u0003\u00c0\u0000\u0000\u00f3\u00cf\u0000\u0000\u00f3\u00cf\u0000\u00c0\u000c\u0030\u0003\u00c0\u000c\u0030\u0003\u00c0\u0000\u0000\u0003\u00c0\u0000\u0000\u0003\u0030\u0003\u00c0\u000c\u0030\u0003\u00c0\u000c\u0030\u00fc\u003f\u000c\u0030\u00fc\u003f\u000c\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u000c\u0000\u00cc\u0030\u000c\u0000\u00cc\u0030\u00cc\u00ff\u0000\u0030\u00cc\u00ff\u0000\u0030\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    static final String ICSleep = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00fc\u0003\u0000\u0000\u00fc\u0003\u0000\u0000\u00c0\u00f0\u000f\u0000\u00c0\u00f0\u000f\u0000\u0030\u0000\u0033\u0000\u0030\u0000\u0033\u0000\u00fc\u00cf\u00c0\u0003\u00fc\u00cf\u00c0\u0003\u0000\u00f3\u000f\u000c\u0000\u00f3\u000f\u000c\u0000\u0003\u0000\u000c\u0000\u0003\u0000\u000c\u00c0\u000c\u000f\u0033\u00c0\u000c\u000f\u0033\u00c0\u00f0\u00f0\u0030\u00c0\u00f0\u00f0\u0030\u00c0\u0000\u0000\u0030\u00c0\u0000\u0000\u0030\u00c0\u0000\u0000\u0030\u00c0\u0000\u0000\u0030\u0000\u0003\u000f\u000c\u0000\u0003\u000f\u000c\u0000\u0003\u000f\u000c\u0000\u0003\u000f\u000c\u0000\u003c\u00c0\u0003\u0000\u003c\u00c0\u0003\u0000\u00c0\u003f\u0000\u0000\u00c0\u003f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    static final String ICAutoRun = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00ff\u00f0\u00ff\u00ff\u00ff\u00f0\u00ff\u00ff\u0000\u000c\u0000\u00c0\u0000\u000c\u0000\u00c0\u003f\u000c\u00c0\u00cc\u003f\u000c\u00c0\u00cc\u0000\u00ff\u00ff\u003f\u0000\u00ff\u00ff\u003f\u000f\u0003\u0000\u0030\u000f\u0003\u0000\u0030\u00c0\u0000\u0000\u000c\u00c0\u0000\u0000\u000c\u00c3\u0000\u0000\u000f\u00c3\u0000\u0000\u000f\u0030\u0000\u00cc\u0003\u0030\u0000\u00cc\u0003\u0033\u0030\u0033\u0003\u0033\u0030\u0033\u0003\u00f0\u00ff\u00ff\u0000\u00f0\u00ff\u00ff\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
 
    static final String ICYes = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u000f\u0000\u0000\u0000\u000f\u0000\u0000\u00c0\u003f\u0000\u0000\u00c0\u003f\u0000\u0000\u00f0\u00ff\u0000\u0000\u00f0\u00ff\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0030\u0000\u00ff\u000f\u0030\u0000\u00ff\u000f\u00fc\u00c0\u00ff\u0003\u00fc\u00c0\u00ff\u0003\u00ff\u00f3\u00ff\u0000\u00ff\u00f3\u00ff\u0000\u00ff\u00ff\u003f\u0000\u00ff\u00ff\u003f\u0000\u00fc\u00ff\u000f\u0000\u00fc\u00ff\u000f\u0000\u00f0\u00ff\u0003\u0000\u00f0\u00ff\u0003\u0000\u00c0\u00ff\u0000\u0000\u00c0\u00ff\u0000\u0000\u0000\u003f\u0000\u0000\u0000\u003f\u0000\u0000\u0000\u000c\u0000\u0000\u0000\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    static final String ICNo = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00f0\u0000\u0000\u000f\u00f0\u0000\u0000\u000f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u000f\u00f0\u003f\u00fc\u000f\u00f0\u003f\u00f0\u003f\u00fc\u000f\u00f0\u003f\u00fc\u000f\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00ff\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00ff\u0000\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u00f0\u003f\u00fc\u000f\u00f0\u003f\u00fc\u000f\u00fc\u000f\u00f0\u003f\u00fc\u000f\u00f0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00f0\u0000\u0000\u000f\u00f0\u0000\u0000\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";

    static final int defaultSleepTime = 2;
    static final int maxSleepTime = 10;

    private IndicatorThread ind = new IndicatorThread();
    private BatteryIndicator indiBA = new BatteryIndicator();
    private RConsole rcons = new RConsole();
    
    private GraphicMenu curMenu;
    private int timeout = 0;
    private boolean btPowerOn;
    private boolean btVisibility;
    private static String version = "Unknown";
    private static String hostname;
    private List<String> ips = getIPAddresses();
    
    /**
     * Main method
     */
	public static void main(String[] args) throws Exception {
		System.out.println("Menu started");
		
        if (args.length > 0) {    	
        	hostname = args[0];
        }
        
        if (args.length > 1) {    	
        	version = args[1];
        }
        
        System.out.println("Host name: " + hostname);
        System.out.println("Version: " + version);
        
        // Check for autorun
    	File f = getDefaultProgram();
    	if (f != null)
    	{
        	String auto = Settings.getProperty(defaultProgramAutoRunProperty, "");        	
    		if (auto.equals("ON") && !Button.LEFT.isDown())
            {
            	System.out.println("Executing " + f.getPath());
                exec("jrun -jar " + f.getPath());
            }
    	}
        
        TuneThread tuneThread = new TuneThread();
        
        //Fade in
        tuneThread.start();
        
        // Tell thread to play tune
        tuneThread.setState(1);
        
        InitThread initThread = new InitThread();
        initThread.start();
        initThread.join();
        
        initThread.menu.start();
        
        tuneThread.setState(3);
        
        initThread.menu.mainMenu();
        
        System.out.println("Menu finished");
        
        System.exit(0);
	}
	
	/**
     * Display the main system menu.
     * Allow the user to select File, Bluetooth, Sound, System operations.
     */
    private void mainMenu()
    {
        GraphicMenu menu = new GraphicMenu(new String[]
                {
                    "Run Default", "Files", "Samples", "Bluetooth", "Sound", "System", "Version"
                },new String[] {ICDefault,ICFiles,ICFiles,ICBlue,ICSound,ICNXT,ICLeJOS},3);
        int selection = 0;
        do
        {
            newScreen(hostname);
            int row = 1;
            for(String ip: ips) {
            	LCD.drawString(ip,8 - ip.length()/2,row++);
            }
            selection = getSelection(menu, selection);
            switch (selection)
            {
                case 0:
                    mainRunDefault();
                    break;
                case 1:
                    filesMenu();
                    break;
                case 2:
                    samplesMenu();
                    break;
                case 3:
                    bluetoothMenu();
                    break;
                case 4:
                    soundMenu();
                    break;
                case 5:
                    systemMenu();
                    break;
                case 6:
                    displayVersion();
                    break;
            }
            
            if (selection < 0)  {
            	LCD.bitBlt(null, 0, 0, 0, 0, 0, 64, 178, 64, LCD.ROP_CLEAR);
            	LCD.refresh();
            	if (getYesNo("  Shut down EV3 ?", false) == 1)
            		break;
            }
        } while (true);
        
        // Shit down the EV3
        shutdown();
    }
    
    /**
     * Present the Bluetooth menu to the user.
     */
    private void bluetoothMenu()
    {
        int selection = 0;
        GraphicMenu menu = new GraphicMenu(null,null,4);
        menu.setParentIcon(ICBlue);
        boolean visible = false;
        do
        {
            newScreen("Bluetooth");
            LCD.drawString("Power", 0, 1);
            LCD.drawString(btPowerOn ? "on" : "off", 11, 1);
            //visible = Bluetooth.getVisibility() == 1;
            if (btPowerOn)
            {
                LCD.drawString("Visibility", 0, 2);
                LCD.drawString(visible ? "on" : "off", 11, 2);
                menu.setItems(new String[]
                        {
                            "Power off", "Search/Pair", "Devices", "Visibility", "Change PIN"
                        },
                        new String[]{ICPower,ICSearch,
                		ICNXT,ICVisibility,ICPIN});
            }
            else
                menu.setItems(new String[]
                        {
                            "Power on"
                        },new String[] {ICPower});
            selection = getSelection(menu,selection);
            switch (selection)
            {
                case 0:
                    bluetoothPower(!btPowerOn);
                    break;
                case 1:
                    bluetoothSearch();
                    break;
                case 2:
                    bluetoothDevices();
                    break;
                case 3:
                    //Bluetooth.setVisibility((byte) (visible ? 0 : 1));
                    btVisibility = !visible;
                    //updateBTIcon();
                    ind.updateNow();
                    break;
                case 4:
                    bluetoothChangePIN();
                    break;
            }
        } while (selection >= 0);
    }
    
    /**
     * Clears the screen, displays a number and allows user to change
     * the digits of the number individually using the NXT buttons.
     * Note the array of bytes represent ASCII characters, not actual numbers.
     * 
     * @param digits Number of digits in the PIN.
     * @param title The text to display above the numbers.
     * @param number Start with a default PIN. Array of bytes up to 8 length.
     * @return
     */
    private boolean enterNumber(String title, byte[] number, int digits)
    {
        // !! Should probably check to make sure defaultNumber is digits in size
        int curDigit = 0;

        while (true)
        {
            newScreen();
            LCD.drawString(title, 0, 2);
            for (int i = 0; i < digits; i++)
                LCD.drawChar((char)number[i], i * 2 + 1, 4);
            
            if (curDigit >= digits)
            	return true;
            
            Utils.drawRect(curDigit * 12 + 3, 30, 10, 10);

            int ret = getButtonPress();
            switch (ret)
            {
                case Button.ID_ENTER:
                { // ENTER
                    curDigit++;
                    break;
                }
                case Button.ID_LEFT:
                { // LEFT
                    number[curDigit]--;
                    if (number[curDigit] < '0')
                        number[curDigit] = '9';
                    break;
                }
                case Button.ID_RIGHT:
                { // RIGHT
                    number[curDigit]++;
                    if (number[curDigit] > '9')
                        number[curDigit] = '0';
                    break;
                }
                case Button.ID_ESCAPE:
                { // ESCAPE
                    curDigit--;
                    // Return false if user backs out
                    if (curDigit < 0)
                        return false;
                    break;
                }
            }
        }
    }
 
    /**
     * Allow the user to change the Bluetooth PIN.
     */
    private void bluetoothChangePIN()
    {
        /*// 1. Retrieve PIN from System properties
        String pinStr = SystemSettings.getStringSetting(pinProperty, "1234");
        int len = pinStr.length();
        byte[] pin = new byte[len];
        for (int i = 0; i < len; i++)
            pin[i] = (byte) pinStr.charAt(i);

        // 2. Call enterNumber() method
        if (enterNumber("Enter NXT PIN", pin, 4))
        {
            // 3. Set PIN in system memory.
        	StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pin.length; i++)
                sb.append((char) pin[i]);
            Settings.setProperty(pinProperty, sb.toString());
            //Bluetooth.setPin(pin);
        }*/
    }
    
    /**
     * Perform the Bluetooth search operation
     * Search for Bluetooth devices
     * Present those that are found
     * Allow pairing
     */
    private void bluetoothSearch()
    {
        /*newScreen("Searching");
        ArrayList<RemoteDevice> devList; 
        indiBT.incCount();
        try
        {
        	// 0 means "search for all"
	        devList = Bluetooth.inquire(5, 10, 0);
        }
	    finally
	    {
	    	indiBT.decCount();
	    }
        if (devList.size() <= 0)
        {
            msg("No devices found");
            return;
        }
        
        String[] names = new String[devList.size()];
        String[] icons = new String[devList.size()];
        for (int i = 0; i < devList.size(); i++)
        {
            RemoteDevice btrd = devList.get(i);
            names[i] = btrd.getFriendlyName(false);
            icons[i] = getDeviceIcon(btrd.getDeviceClass());
        }
        GraphicListMenu searchMenu = new GraphicListMenu(names, icons);
        searchMenu.setParentIcon(ICSearch);
        GraphicMenu subMenu = new GraphicMenu(new String[]{"Pair"}, new String[]{ICYes},4);
        subMenu.setParentIcon(ICSearch);
        int selected = 0;
        do
        {
            newScreen("Found");
            selected = getSelection(searchMenu, selected);
            if (selected >= 0)
            {
                RemoteDevice btrd = devList.get(selected);
                newScreen();
                LCD.bitBlt(
                	Utils.stringToBytes8(getDeviceIcon(btrd.getDeviceClass()))
                	, 7, 7, 0, 0, 2, 16, 7, 7, LCD.ROP_COPY);
                LCD.drawString(names[selected], 2, 2);
                LCD.drawString(btrd.getBluetoothAddress(), 0, 3);
                int subSelection = getSelection(subMenu, 0);
                if (subSelection == 0){
                    newScreen("Pairing");
                    Bluetooth.addDevice(btrd);
                    // !! Assuming 4 length
                    byte[] pin = { '0', '0', '0', '0' };
                    if (!enterNumber("PIN for " + btrd.getFriendlyName(false), pin, pin.length))
                    	break;
                    LCD.drawString("Please wait...", 0, 6);
                    BTConnection connection = Bluetooth.connect(btrd.getDeviceAddr(), 0, pin);
                    // Indicate Success or failure:
                    if (connection != null)
                    {
                        LCD.drawString("Paired!      ", 0, 6);
                        connection.close();
                    }
                    else
                    {
                        LCD.drawString("UNSUCCESSFUL  ", 0, 6);
                        Bluetooth.removeDevice(btrd);
                    }
                    LCD.drawString("Press any key", 0, 7);
                    getButtonPress();
                }
            }
        } while (selected >= 0);*/
    }

    /**
     * Display all currently known Bluetooth devices.
     */
    private void bluetoothDevices()
    {
    	/*
        ArrayList<RemoteDevice> devList = Bluetooth.getKnownDevicesList();
        if (devList.size() <= 0)
        {
            msg("No known devices");
            return;
        }
        
        newScreen("Devices");
        String[] names = new String[devList.size()];
        String[] icons = new String[devList.size()];
        for (int i = 0; i < devList.size(); i++)
        {
            RemoteDevice btrd = devList.get(i);
            names[i] = btrd.getFriendlyName(false);
            icons[i] = getDeviceIcon(btrd.getDeviceClass());
        }

        GraphicListMenu deviceMenu = new GraphicListMenu(names, icons);
        deviceMenu.setParentIcon(ICNXT);
        //TextMenu subMenu = new TextMenu(new String[] {"Remove"}, 6);
        GraphicMenu subMenu = new GraphicMenu(new String[] {"Remove"},new String[]{ICDelete},4);
        subMenu.setParentIcon(ICNXT);
        int selected = 0;
        do
        {
            newScreen();
            selected = getSelection(deviceMenu, selected);
            if (selected >= 0)
            {
                newScreen();
                RemoteDevice btrd = devList.get(selected);
                int devclass = btrd.getDeviceClass();
                LCD.bitBlt(
                    	Utils.stringToBytes8(getDeviceIcon(devclass))
                    	, 7, 7, 0, 0, 2, 16, 7, 7, LCD.ROP_COPY);
                LCD.drawString(btrd.getFriendlyName(false), 2, 2);
                LCD.drawString(btrd.getBluetoothAddress(), 0, 3);
                // TODO device class is overwritten by menu
                // LCD.drawString("0x"+Integer.toHexString(devclass), 0, 4);
                int subSelection = getSelection(subMenu, 0);
                if (subSelection == 0)
                {
                    Bluetooth.removeDevice(btrd);
                    break;
                }
            }
        } while (selected >= 0);*/
    }

    /**
     * Allow the user to turn Bluetooth power on/off
     * @param on New power setting
     */
    private void bluetoothPower(boolean on)
    {
        newScreen();
        
        LCD.drawString("Powering " + (on ? "on" : "off") +" ...", 0, 2);
        
        //indiBT.incCount();
        try
        {
        	btPowerOn = setBluetoothPower(on);
        }
        finally
        {
        	//indiBT.decCount();
        }
        //updateBTIcon();
        ind.updateNow();
    }

    
    /**
     * Turn Bluetooth power on/off.
     * Record this new setting in the status bytes held by the Bluetooth module.
     * @param powerOn
     * @return The new power state.
     */
    static boolean setBluetoothPower(boolean powerOn)
    {
        // Set the state of the Bluetooth power to be powerOn. Also record the
        // current state of this in the BT status bytes.                                                      
        // If power is not on we need it on to check things
        //if (!Bluetooth.getPower())
        //    Bluetooth.setPower(true);
        // First update the status bytes if needed
        //int status = Bluetooth.getStatus();
        //if (powerOn != (status == 0))
        //    Bluetooth.setStatus((powerOn ? 0 : 1));
        //Bluetooth.setPower(powerOn);
        return powerOn;
    }
    
    /**
     * Run the default program (if set).
     */
    private void mainRunDefault()
    {
    	File f = getDefaultProgram();
        if (f == null)
        {
       		msg("No default set");
        }
        else
        {
        	System.out.println("Executing " + f.getPath());
        	ind.suspend();
            exec("jrun -jar " + f.getPath());
        	ind.resume();
        }
    }
    
    /**
     * Present the system menu.
     * Allow the user to format the filesystem. Change timeouts and control
     * the default program usage.
     */
    private void systemMenu()
    {
        String[] menuData = {"Delete all", "", "Auto Run", "Unset default"};
        String[] iconData = {ICFormat,ICSleep,ICAutoRun,ICDefault};
        boolean rechargeable = false;
        GraphicMenu menu = new GraphicMenu(menuData,iconData,4);
        menu.setParentIcon(ICNXT);
        int selection = 0;
        do {
            newScreen("System");
            LCD.drawString("RAM", 0, 1);
            LCD.drawInt((int) (Runtime.getRuntime().freeMemory()), 11, 1);
            LCD.drawString("Battery", 0, 2);
            int millis = LocalEV3.ev3.getBattery().getVoltageMilliVolt() + 50;
            LCD.drawInt((millis - millis % 1000) / 1000, 11, 2);
            LCD.drawString(".", 12, 2);
            LCD.drawInt((millis % 1000) / 100, 13, 2);
            if (rechargeable)
                LCD.drawString("R", 15, 2);
            menuData[1] = "Sleep time: " + (timeout == 0 ? "off" : String.valueOf(timeout));
            File f = getDefaultProgram();
            if (f == null){
            	menuData[3] = null;
            	iconData[3] = null;
            }
            menu.setItems(menuData,iconData);
            selection = getSelection(menu, selection);
            switch (selection)
            {
                case 0:              	
                    if (getYesNo("Delete all files?", false) == 1) {
                    	File dir = new File("/home/lejos/programs");
                        for (String fn : dir.list()) {
                            File aFile = new File(dir,fn);
                            System.out.println("Deleting " + aFile.getPath());
                            //aFile.delete();
                        }
                    }
                    break;
                case 1:
                    timeout++;
                    if (timeout > maxSleepTime)
                        timeout = 0;
                    Settings.setProperty(sleepTimeProperty, String.valueOf(timeout));
                    break;
                case 2:
                    systemAutoRun();
                    break;
                case 3:
                    Settings.setProperty(defaultProgramProperty, "");
                    Settings.setProperty(defaultProgramAutoRunProperty, "");
                    selection = 0;
                    break;
            }
        } while (selection >= 0);
    }
    
    /**
     * Present details of the default program
     * Allow the user to specifiy run on system start etc.
     */
    private void systemAutoRun()
    {
    	File f = getDefaultProgram();
    	if (f == null)
    	{
       		msg("No default set");
       		return;
    	}
    	
    	newScreen("Auto Run");
    	LCD.drawString("Default Program:", 0, 2);
    	LCD.drawString(f.getName(), 1, 3);
    	
    	String current = Settings.getProperty(defaultProgramAutoRunProperty, "");
        int selection = getYesNo("Run at power up?", current.equals("ON"));
        if (selection >= 0)
        	Settings.setProperty(defaultProgramAutoRunProperty, selection == 0 ? "OFF" : "ON");
    }
    
    /**
     * Ask the user for confirmation of an action.
     * @param prompt A description of the action about to be performed
     * @return 1=yes 0=no < 0 escape
     */
    private int getYesNo(String prompt, boolean yes)
    {
    	//newScreen();
        GraphicMenu menu = new GraphicMenu(new String[]{"No", "Yes"},new String[]{ICNo,ICYes},5,prompt,4);
        return getSelection(menu, yes ? 1 : 0);
    }
    
    /**
     * Get the default program as a file
     */
    private static File getDefaultProgram()
    {
    	String file = Settings.getProperty(defaultProgramProperty, "");
    	if (file != null && file.length() > 0)
    	{
    		File f = new File(file);
        	if (f.exists())
        		return f;
        	
           	Settings.setProperty(defaultProgramProperty, "");
           	Settings.setProperty(defaultProgramAutoRunProperty, "OFF");
    	}
    	return null;
    }
    
    /**
     * Play the leJOS startup tune.
     */
    static void playTune()
    {
        int[] freq = { 523, 784, 659 };
        for (int i = 0; i < 3; i++) {
            Sound.playTone(freq[i], 300);
            Sound.pause(300);
        }
    }

    /**
     * Display the sound menu.
     * Allow the user to change volume and key click volume.
     */
    private void soundMenu()
    {
        String[] soundMenuData = new String[]{"Volume:    ", "Key click: "};
        String[] soundMenuData2 = new String[soundMenuData.length];
        GraphicMenu menu = new GraphicMenu(soundMenuData2, new String[] {ICSound,ICSound},3);
        int[][] Volumes =
        {
            {
                Sound.getVolume() / 10, 784, 250, 0
            },
            {
                Button.getKeyClickVolume() / 10, Button.getKeyClickTone(1), Button.getKeyClickLength(), 0
            }
        };
        int selection = 0;
        // Make a note of starting volumes so we know if it changes
        for (int i = 0; i < Volumes.length; i++)
            Volumes[i][3] = Volumes[i][0];
        // remember and Turn of tone for the enter key
        int tone = Button.getKeyClickTone(Button.ID_ENTER);
        Button.setKeyClickTone(Button.ID_ENTER, 0);
        do {
            newScreen("Sound");
            for (int i = 0; i < Volumes.length; i++)
                soundMenuData2[i] = soundMenuData[i] + formatVol(Volumes[i][0]);
            menu.setItems(soundMenuData2);
            selection = getSelection(menu, selection);
            if (selection >= 0)
            {
                Volumes[selection][0]++;
                Volumes[selection][0] %= 11;
                if (selection == 0)
                {
                    Sound.setVolume(Volumes[0][0] * 10);
                    Sound.playNote(Sound.XYLOPHONE, Volumes[selection][1], Volumes[selection][2]);
                }
                else
                    Sound.playTone(Volumes[selection][1], Volumes[selection][2], -Volumes[selection][0] * 10);
            }
        } while (selection >= 0);
        // Make sure key click is back on and has new volume
        Button.setKeyClickTone(Button.ID_ENTER, tone);
        Button.setKeyClickVolume(Volumes[1][0] * 10);
        // Save in settings
        if (Volumes[0][0] != Volumes[0][3])
            Settings.setProperty(Sound.VOL_SETTING, String.valueOf(Volumes[0][0] * 10));
        if (Volumes[1][0] != Volumes[1][3])
            Settings.setProperty(Button.VOL_SETTING, String.valueOf(Volumes[1][0] * 10));
    }
    
    /**
     * Format a string for use when displaying the volume.
     * @param vol Volume setting 0-10
     * @return String version.
     */
    private static String formatVol(int vol)
    {
        if (vol == 0)
            return "mute";
        if (vol == 10)
            return "10";        
        return new StringBuilder().append(' ').append(vol).toString();
    }
    
    /**
     * Display system version information.
     */
    private void displayVersion()
    {
        newScreen("Version");
        LCD.drawString("leJOS:", 0, 2);
        LCD.drawString(version, 6, 2);
        LCD.drawString("Menu:", 0, 3);
        LCD.drawString(Utils.versionToString(Config.VERSION),6, 3);
        List<String> ips = getIPAddresses();
        getButtonPress();
    }
    
    /**
     * Read a button press.
     * If the read timesout then exit the system.
     * @return The bitcode of the button.
     */
    private int getButtonPress()
    {
        int value = Button.waitForAnyPress(timeout*60000);
        if (value == 0) shutdown();
        return value;
    }

    /**
     *  Start the threads
     */
    private void start()
    {
        //bt.start();
        //usb.start();
    	ind.start();
    	rcons.start();
    }
	
    /**
     *  Constructor
     */
	public GraphicStartup() {
	}
	
	/**
	 * Start-up thread
	 */
    static class InitThread extends Thread
    {
    	GraphicStartup menu;
    	
        /**
         * Startup the menu system.
         * Play the greeting tune.
         * Run the default program if auto-run is requested.
         * Initialize I/O etc.
         */            
        @Override
        public void run()
        {
            //setAddress();            
        	menu = new GraphicStartup();        	
        }
    }
	
    /**
     * Present the menu for a single file.
     * @param file
     */
    private void fileMenu(File file)
    {
        String fileName = file.getName();
        String ext = Utils.getExtension(fileName);
        int selectionAdd;
        String[] items;
        String[] icons;
        if (ext.equals("jar"))
        {
        	selectionAdd = 0;
            items = new String[]{"Execute program", "Debug program", "Set as Default", "Delete file"}; 
            icons = new String[]{ICProgram,ICProgram,ICDefault,ICDelete};
        }
        else if (ext.equals("wav"))
        {
        	selectionAdd = 10;
        	items = new String[]{"Play sample", "Delete file"};
        	icons = new String[]{ICSound,ICDelete};
        }
        else
        {
        	selectionAdd = 20;
        	items = new String[]{"Delete file"};
        	icons = new String[]{ICDelete};
        }
        newScreen();
        LCD.drawString("Size:", 0, 2);
        LCD.drawString(Long.toString(file.length()), 5, 2);
        GraphicMenu menu = new GraphicMenu(items,icons,3,fileName,1);
        menu.setParentIcon(ICFiles);
        int selection = getSelection(menu, 0);
        if (selection >= 0)
        {
	        switch(selection + selectionAdd)
	        {
	            case 0:
	            	System.out.println("Running program: " + file.getPath());
	            	ind.suspend();
	            	exec("jrun -jar " + file.getPath());
	            	ind.resume();
	                break;
	            case 1:
	            	System.out.println("Debugging program: " + file.getPath());
	            	ind.suspend();
	            	exec("jrun -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y -jar " + file.getPath());
	            	ind.resume();
	                break;
	            case 2:
	            	Settings.setProperty(defaultProgramProperty, file.getPath());
	            	break;
	            case 10:
	            	System.out.println("Playing " + file.getPath());
	                Sound.playSample(file);
	                break;
	            case 3:
	            case 11:
	            case 20:
	                file.delete();
	                break;
	        }
        }
    }
    
    /**
     * Execute a program and display its output to System.out and error stream to System.err
     */
    static void exec(String program) {
        try {
        	LCD.clearDisplay();
        	LCD.refresh();
        	LCD.setAutoRefresh(false);
            Process p = Runtime.getRuntime().exec(program);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader err= new BufferedReader(new InputStreamReader(p.getErrorStream()));
            
            EchoThread echoIn = new EchoThread(input, System.out);
            EchoThread echoErr = new EchoThread(err, System.err);
            
            echoIn.start();
            echoErr.start();
            
            while(true) {
              int b = Button.readButtons(); 
              if (b == 6) {
            	  System.out.println("Killing the process");
            	  p.destroy();
                  //echoIn.close();
                  //echoErr.close();            
                  break;
              }
              if (!echoIn.isAlive() && !echoErr.isAlive()) break;           
              Delay.msDelay(200);
            }
            System.out.println("Waiting for process to die");;
            p.waitFor();
            System.out.println("Program finished");
        	LCD.setAutoRefresh(false);
        	LCD.clearDisplay();
        	LCD.refresh();
          }
          catch (Exception err) {
            err.printStackTrace();
          } 	
    }
    
    static class EchoThread extends Thread {
    	BufferedReader in;
    	PrintStream out;
    	
    	public EchoThread(BufferedReader in, PrintStream out) {
    		super();
    		this.in= in;
    		this.out = out;
    	}
    	
    	public void close() {
    		try {
    			System.out.println("Closing echo stream");
				in.close();
			} catch (IOException e) {
				System.err.println("Close of echo stream failed");
			}
    		in = null;
    	}
    	
		@Override
		public void run()
		{
			while(in != null) {
				try {
					String line = in.readLine();
					if (line == null) {
						close();
						break;
					} else {
						out.println(line);
					}
				} catch (IOException e) {
					System.err.println("Echo stream Exception: " + e);
					close();
				}
			}
		}
    }
    
    /**
     * Display the files in the file system.
     * Allow the user to choose a file for further operations.
     */
    private void filesMenu()
    {
    	GraphicListMenu menu = new GraphicListMenu(null,null);
    	System.out.println("Finding files ...");
        int selection = 0;
        do {
            File[] files = (new File("/home/lejos/programs")).listFiles();
            int len = 0;
            for (int i = 0; i < files.length && files[i] != null; i++)
                len++;
            if (len == 0)
            {
                msg("No files found");
                return;
            }
            newScreen("Files");
            String fileNames[] = new String[len];
            String[] icons = new String[len];
            for (int i = 0; i < len; i++){
                fileNames[i] = files[i].getName();
                String ext = Utils.getExtension(files[i].getName());
               if (ext.equals("jar"))
                	icons[i] = ICMProgram;
                else if (ext.equals("wav"))
                	icons[i] = ICMSound;
                else
                	icons[i] = ICMFile;
            }
            menu.setItems(fileNames,icons);
            selection = getSelection(menu, selection);
            if (selection >= 0)
                fileMenu(files[selection]);
        } while (selection >= 0);
    }
    
    /**
     * Display the samples in the file system.
     * Allow the user to choose a file for further operations.
     */
    private void samplesMenu()
    {
    	GraphicListMenu menu = new GraphicListMenu(null,null);
    	System.out.println("Finding files ...");
        int selection = 0;
        do {
            File[] files = (new File("/home/root/lejos/samples")).listFiles();
            int len = 0;
            for (int i = 0; i < files.length && files[i] != null; i++)
                len++;
            if (len == 0)
            {
                msg("No samples found");
                return;
            }
            newScreen("Samples");
            String fileNames[] = new String[len];
            String[] icons = new String[len];
            for (int i = 0; i < len; i++){
                fileNames[i] = files[i].getName();
                String ext = Utils.getExtension(files[i].getName());
               if (ext.equals("jar"))
                	icons[i] = ICMProgram;
                else if (ext.equals("wav"))
                	icons[i] = ICMSound;
                else
                	icons[i] = ICMFile;
            }
            menu.setItems(fileNames,icons);
            selection = getSelection(menu, selection);
            if (selection >= 0)
                fileMenu(files[selection]);
        } while (selection >= 0);
    }
    
    /**
     * Start a new screen display using the current title.
     */
    private void newScreen()
    {
        LCD.clear();
        ind.updateNow();
    }
    
    /**
     * Start a new screen display.
     * Clear the screen and set the screen title.
     * @param title
     */
    private void newScreen(String title)
    {
        indiBA.setTitle(title);
        newScreen();
    }
    
    /**
     * Display a status message
     * @param msg
     */
    private void msg(String msg)
    {
        newScreen();
        LCD.drawString(msg, 0, 2);
        long start = System.currentTimeMillis();
        int button;
        int buttons = Button.readButtons();
        do
        {
        	Thread.yield();
        	
        	int buttons2 = Button.readButtons();
        	button = buttons2 & ~buttons;
        } while (button != Button.ID_ESCAPE && System.currentTimeMillis() - start < 2000);
    }
    
    /**
     * Obtain a menu item selection
     * Allow the user to make a selection from the specified menu item. If a
     * power off timeout has been specified and no choice is made within this
     * time power off the NXT.
     * @param menu Menu to display.
     * @param cur Initial item to select.
     * @return Selected item or < 0 for escape etc.
     */
    private int getSelection(GraphicMenu menu, int cur)
    {
    	this.setCurMenu(menu);
    	
        int selection;
        // If the menu is interrupted by another thread, redisplay
        do {
        	selection = menu.select(cur, timeout*60000);
        } while (selection == -2);
        
        if (selection == -3)
            shutdown();
        
    	this.setCurMenu(menu);
    	
        return selection;
    }
    
    /**
     * Shut down the EV3
     */
    void shutdown() {
    	System.out.println("Shutting down the EV3");
        ind.suspend();
    	exec("init 0");
        LCD.drawString("  Shutting down", 0, 6);
        LCD.refresh();
    }
    
    /**
     * Set the current menu
     */
    synchronized void setCurMenu(GraphicMenu menu)
    {
    	this.curMenu = menu;
    }
    
    /**
     * Thread to play the tune
     */
	static class TuneThread extends Thread
	{
        int stState = 0;
    	
		@Override
		public void run()
		{
			Utils.fadeIn();
			this.waitState(1);
			playTune();
			// Tell others, that tune is complete
			this.setState(2);
            // Wait for init to complete
            this.waitState(3);
            // Fade in
            Utils.fadeIn();
		}
		
		/**
		 * Set the current state
		 */
        public synchronized void setState(int s)
        {
        	this.stState = s;
        	this.notifyAll();
        }
        
        /**
         * Wait for a specific state
         */
        public synchronized void waitState(int s)
        {
        	while (this.stState < s)
        	{
        		try
        		{
        			this.wait();
        		}
        		catch (InterruptedException e)
        		{
        			// nothing
        		}
        	}
        }
	}
	
    /**
     * Manage the top line of the display.
     * The top line of the display shows battery state, menu titles, and I/O
     * activity.
     */
	class IndicatorThread extends Thread
    {
		public IndicatorThread()
    	{
    		super();
            setDaemon(true);
    	}
    	
    	@Override
		public synchronized void run()
    	{
    		try
    		{
	    		while (true)
	    		{
	    			long time = System.currentTimeMillis();
	    			//int x = (USB.usbStatus() & USB.USB_CONFIGURED) != 0 ? Config.ICON_USB_X : Config.ICON_DISABLE_X;
	    			//indiUSB.setIconX(x);
	    			
	    			byte[] buf = LCD.getDisplay();
	    			// clear not necessary, pixels are always overwritten
	    			for (int i=0; i<LCD.SCREEN_WIDTH; i++)
	    				buf[i] = 0;	    			
	    			indiBA.draw(time, buf);
	    			//indiUSB.draw(time, buf);
	    			//indiBT.draw(time, buf);
	    			LCD.asyncRefresh();
    			
    				// wait until next tick
    				time = System.currentTimeMillis();
    				this.wait(Config.ANIM_DELAY - (time % Config.ANIM_DELAY));
    			}
    		}
    		catch (InterruptedException e)
    		{
    			//just terminate
    		}
    	}
    	
    	/**
    	 * Update the indicators
    	 */
    	public synchronized void updateNow()
    	{
    		this.notifyAll();
    	}
    }
	
	/**
	 * Get all the IP addresses for the device
	 */
    public static List<String> getIPAddresses()
    {
        List<String> result = new ArrayList<String>();
        Enumeration<NetworkInterface> interfaces;
        try
        {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1)
        {
            e1.printStackTrace();
            return null;
        }
        while (interfaces.hasMoreElements()){
            NetworkInterface current = interfaces.nextElement();
            try
            {
                if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
            } catch (SocketException e)
            {
                e.printStackTrace();
            }
            Enumeration<InetAddress> addresses = current.getInetAddresses();
            while (addresses.hasMoreElements()){
                InetAddress current_addr = addresses.nextElement();
                if (current_addr.isLoopbackAddress()) continue;
                result.add(current_addr.getHostAddress());
            }
        }
        return result;
    } 
    
    class RConsole extends Thread
    {
        static final int RCONSOLE_PORT = 8001;    
        ServerSocket ss = null;
        Socket conn = null;
        PrintStream origOut = System.out, origErr = System.err;
        
		public RConsole()
    	{
    		super();
            setDaemon(true);
    	}
		
		public boolean isConnected() {
			return (conn != null && conn.isConnected());
		}
    
        /**
         * Main console I/O thread.
         */
        @Override
        public void run()
        {      
        	// Create a server socket
        	try {
				ss = new ServerSocket(RCONSOLE_PORT);
				System.out.println("Server socket created");
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
        	
        	// Loop accepting remote console connections
        	while (true) {
	            try {
	            	System.out.println("Waiting for a connection");
            		conn = ss.accept();
            		OutputStream os = conn.getOutputStream();
            		BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            		System.setOut(new PrintStream(os));
            		System.setErr(new PrintStream(os));
            		System.out.println("Output redirected");
	                
	                // Loop waiting for commands
	                while (true)
	                {
	                	// Process commands
	                    String line = input.readLine();                
	                    if (line == null) break;
	                }
	                os.close();
	                input.close();
	                conn.close();
	                System.setOut(origOut);
	                System.setErr(origErr);
	                System.out.println("System output set back to original");
	            }
	            catch(IOException e)
	            {
	            	System.err.println("Error accepting connection " + e);
	            }
        	}
        }
    }
}
