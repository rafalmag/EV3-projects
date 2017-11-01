package lejos.hardware.device;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;
import lejos.remote.rcx.Opcode;

import java.util.*;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Supports for HiTechnic NXT IRLink Sensor (NIL1046) IRLink.
 *
 * @author Lawrie Griffiths
 * @author Szymon Madej
 *
 */
public class IRLink extends I2CSensor implements Opcode, IRTransmitter {

	/*
	 * Documentation: http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NIL1046
	 *
	 * ProductId: "HiTechnc"
	 * SensorType: "IRLink"
	 * (confirmed for version " V1.2")
	 */

	// *** Constants *** //

	// IRLink: Registers
	private static final byte TX_BUFFER = 0x40; // From 0x40 to 0x4C
	private static final byte TX_BUFFER_LEN = 0x4D;
	private static final byte TX_MODE = 0x4E;
	private static final byte TX_BUFFER_FLAG = 0x4F;
	private static final byte RX_BUFFER = 0x51; // From 0x51 to 0x5E
	private static final byte RX_BUFFER_LEN = 0x50;
	private static final byte TX_MAX_BUFFER_LEN = 13; // Max size of transmit buffer in bytes
	private static final byte RX_MAX_BUFFER_LEN = 15; // Max size of receive buffer in bytes
	private static final byte TXRX_MAX_RETRIES = 5; // Max retries for sending command to RCX when received response is other than expected

	// IRLink: Transmission modes
	private static final byte TX_MODE_RCX = 0;
	private static final byte TX_MODE_TRAIN = 1;
	private static final byte TX_MODE_PF = 2;

	// IRLink PF: IR signal encoding parameters
	private final byte MAX_BITS = TX_MAX_BUFFER_LEN * 8;
	private final byte STOP_START_PAUSE = 7;
	private final byte LOW_BIT_PAUSE = 2;
	private final byte HIGH_BIT_PAUSE = 4;

	// IRLink PF: Channels
	public static final byte PF_CHANNEL1 = 0;
	public static final byte PF_CHANNEL2 = 1;
	public static final byte PF_CHANNEL3 = 2;
	public static final byte PF_CHANNEL4 = 3;

	// IRLink PF: Extended Mode motor commands
	public static final byte PF_EM_COMMAND_BRAKEFLOAT_OUTPUT_A = 0;         // 00000000, Brake then float output A
	public static final byte PF_EM_COMMAND_INCREMENTSPEED_OUTPUT_A = 1;     // 00000001, Increment speed on output A
	public static final byte PF_EM_COMMAND_DECREMENTSPEED_OUTPUT_A = 2;     // 00000010, Decrement speed on output A
	public static final byte PF_EM_COMMAND_TOGGLEFORWARDFLOAT_OUTPUT_B = 4; // 00000100, Toggle forward/float on output B
	public static final byte PF_EM_COMMAND_SYNCTOGGLEBIT = 7;               // 00000111, Align toggle bit (get in sync)
	// IRLink PF: Combo Direct Mode motor operations
	public static final byte PF_CDM_COMMAND_FLOAT = 0;    // 00000000, Float output
	public static final byte PF_CDM_COMMAND_FORWARD = 1;  // 00000001, Forward on output
	public static final byte PF_CDM_COMMAND_BACKWARD = 2; // 00000010, Backward on output
	public static final byte PF_CDM_COMMAND_BRAKE = 3;    // 00000011, Brake then float output
	// IRLink PF: PWM motor operations for Single Output Mode with submode bit set to PWM (submode=PF_SOM_SUBMODE_PWM) or for Combo PWM Mode
	public static final byte PF_PWM_COMMAND_FLOAT = 0;       // 00000000, Float
	public static final byte PF_PWM_COMMAND_FORWARD_1 = 1;   // 00000001, PWM forward step 1
	public static final byte PF_PWM_COMMAND_FORWARD_2 = 2;   // 00000010, PWM forward step 2
	public static final byte PF_PWM_COMMAND_FORWARD_3 = 3;   // 00000011, PWM forward step 3
	public static final byte PF_PWM_COMMAND_FORWARD_4 = 4;   // 00000100, PWM forward step 4
	public static final byte PF_PWM_COMMAND_FORWARD_5 = 5;   // 00000101, PWM forward step 5
	public static final byte PF_PWM_COMMAND_FORWARD_6 = 6;   // 00000110, PWM forward step 6
	public static final byte PF_PWM_COMMAND_FORWARD_7 = 7;   // 00000111, PWM forward step 7
	public static final byte PF_PWM_COMMAND_BRAKEFLOAT = 8;  // 00001000, Brake then float
	public static final byte PF_PWM_COMMAND_BACKWARD_7 = 9;  // 00001001, PWM backward step 7
	public static final byte PF_PWM_COMMAND_BACKWARD_6 = 10; // 00001010, PWM backward step 6
	public static final byte PF_PWM_COMMAND_BACKWARD_5 = 11; // 00001011, PWM backward step 5
	public static final byte PF_PWM_COMMAND_BACKWARD_4 = 12; // 00001100, PWM backward step 4
	public static final byte PF_PWM_COMMAND_BACKWARD_3 = 13; // 00001101, PWM backward step 3
	public static final byte PF_PWM_COMMAND_BACKWARD_2 = 14; // 00001110, PWM backward step 2
	public static final byte PF_PWM_COMMAND_BACKWARD_1 = 15; // 00001111, PWM backward step 1
	// IRLink PF: Clear/Set/Toggle/Inc/Dec motor operations for Single Output Mode with submode bit set to Clear/Set/Toggle/Inc/Dec (submode=PF_SOM_SUBMODE_CSTID)
	public static final byte PF_CSTID_COMMAND_TOGGLEFULLFORWARD = 0;          // 00000000, Toggle full forward (Stop -> Fw, Fw -> Stop, Bw -> Fw)
	public static final byte PF_CSTID_COMMAND_TOGGLEDIRECTION = 1;            // 00000001, Toggle direction
	public static final byte PF_CSTID_COMMAND_INCREMENT_NUMERICAL_PWM = 2;    // 00000010, Increment numerical PWM
	public static final byte PF_CSTID_COMMAND_DECREMENT_NUMERICAL_PWM = 3;    // 00000011, Decrement numerical PWM
	public static final byte PF_CSTID_COMMAND_INCREMENT_PWM = 4;              // 00000100, Increment PWM
	public static final byte PF_CSTID_COMMAND_DECREMENT_PWM = 5;              // 00000101, Decrement PWM
	public static final byte PF_CSTID_COMMAND_FULLFORWARD = 6;                // 00000110, Full forward (timeout)
	public static final byte PF_CSTID_COMMAND_FULLBACKWARD = 7;               // 00000111, Full backward (timeout)
	public static final byte PF_CSTID_COMMAND_TOGGLEFULLFORWARDBACKWARD = 8;  // 00001000, Toggle full forward/backward (default forward)
	public static final byte PF_CSTID_COMMAND_CLEAR_C1 = 9;                   // 00001001, Clear C1 (negative logic - C1 high)
	public static final byte PF_CSTID_COMMAND_SET_C1 = 10;                    // 00001010, Set C1 (negative logic - C1 low)
	public static final byte PF_CSTID_COMMAND_TOGGLE_C1 = 11;                 // 00001011, Toggle C1
	public static final byte PF_CSTID_COMMAND_CLEAR_C2 = 12;                  // 00001100, Clear C2 (negative logic - C2 high)
	public static final byte PF_CSTID_COMMAND_SET_C2 = 13;                    // 00001101, Set C2 (negative logic - C2 low)
	public static final byte PF_CSTID_COMMAND_TOGGLE_C2 = 14;                 // 00001110, Toggle C2
	public static final byte PF_CSTID_COMMAND_TOGGLEFULLBACKWARD = 15;        // 00001111, Toggle full backward (Stop -> Bw, Bw -> Stop, Fwd -> Bw)

	// IRLink PF: Single Output Mode special bits
	public static final byte PF_SOM_SUBMODE_PWM = 0;   // Single Output Mode, submode with PWM operations
	public static final byte PF_SOM_SUBMODE_CSTID = 1; // Single Output Mode, submode with Clear/Set/Toggle/Inc/Dec operations
	public static final byte PF_SOM_OUTPUT_A = 0;   // Single Output Mode, Output A (Red) selector
	public static final byte PF_SOM_OUTPUT_B = 1; // Single Outout Mode, Output B (Blue) selector

	// IRLink PF: Standalone bits for various usage
	private static final byte PF_BIT_0 = 00000000;
	private static final byte PF_BIT_1 = 00000001;

	// IRLink RCX: Remote Control buttons codes
	public static int RCX_REMOTE_BEEP  = 0x8000;
	public static int RCX_REMOTE_STOP  = 0x4000;
	public static int RCX_REMOTE_P5    = 0x2000;
	public static int RCX_REMOTE_P4    = 0x1000;
	public static int RCX_REMOTE_P3    = 0x0800;
	public static int RCX_REMOTE_P2    = 0x0400;
	public static int RCX_REMOTE_P1    = 0x0200;
	public static int RCX_REMOTE_C_BWD = 0x0100;
	public static int RCX_REMOTE_B_BWD = 0x0080;
	public static int RCX_REMOTE_A_BWD = 0x0040;
	public static int RCX_REMOTE_C_FWD = 0x0020;
	public static int RCX_REMOTE_B_FWD = 0x0010;
	public static int RCX_REMOTE_A_FWD = 0x0008;
	public static int RCX_REMOTE_MSG3  = 0x0004;
	public static int RCX_REMOTE_MSG2  = 0x0002;
	public static int RCX_REMOTE_MSG1  = 0x0001;
	public static int RCX_REMOTE_NOOP  = 0x0000;

	// IRLink RCX: Motors
	public static int RCX_MOTOR_A = 0x01;
	public static int RCX_MOTOR_B = 0x02;
	public static int RCX_MOTOR_C = 0x04;
	// IRLink RCX: Motor Commnads
	public static int RCX_MOTOR_FLOAT = 0x00;
	public static int RCX_MOTOR_STOP  = 0x40;
	public static int RCX_MOTOR_OFF   = 0x40;
	public static int RCX_MOTOR_START = 0x80;
	public static int RCX_MOTOR_ON    = 0x80;
	public static int RCX_MOTOR_BACKWARD = 0x00;
	public static int RCX_MOTOR_REVERSE  = 0x40;
	public static int RCX_MOTOR_FORWARD  = 0x80;

	// IRLink RCX: Sensors
	public static int RCX_SENSOR_1 = 0x00;
	public static int RCX_SENSOR_2 = 0x01;
	public static int RCX_SENSOR_3 = 0x02;
	// IRLink RCX: Sensor Type
	public static int RCX_SENSOR_TYPE_NONE        = 0x00;
	public static int RCX_SENSOR_TYPE_TOUCH       = 0x01;
	public static int RCX_SENSOR_TYPE_TEMPERATURE = 0x02;
	public static int RCX_SENSOR_TYPE_LIGHT       = 0x03;
	public static int RCX_SENSOR_TYPE_ANGLE       = 0x04;

	// IRLink RCX: Sensor Mode
	public static int RCX_SENSOR_MODE_RAW           = 0x00; // Raw - Value in 0..1023
	public static int RCX_SENSOR_MODE_BOOLEAN       = 0x01; // Boolean - Either 0 or 1
	public static int RCX_SENSOR_MODE_TRANSITIONCUT = 0x02; // Edge count	- Number of boolean transitions
	public static int RCX_SENSOR_MODE_PERIODCOUNTER = 0x03; // Pulse count - Number of boolean transitions divided by two
	public static int RCX_SENSOR_MODE_PCTFULLSCALE  = 0x04; // Percentage - Raw value scaled to 0..100
	public static int RCX_SENSOR_MODE_CELSIUS       = 0x05; // Temperature in Celsius - 1/10ths of a degree, -19.8..69.5
	public static int RCX_SENSOR_MODE_FAHRENHEIT    = 0x06; // Temperature in Fahrenheit - 1/10ths of a degree, -3.6..157.1
	public static int RCX_SENSOR_MODE_ANGLESTEPS    = 0x07; // Angle - 1/16ths of a rotation, represented as a signed short

	// IRLink RCX: System Sound
	public static int RCX_SYSTEMSOUND_KEYCLICK    = 0x00;
	public static int RCX_SYSTEMSOUND_BEEP        = 0x01;
	public static int RCX_SYSTEMSOUND_SWEEPUP     = 0x02;
	public static int RCX_SYSTEMSOUND_SWEEPDOWN   = 0x03;
	public static int RCX_SYSTEMSOUND_ERROR       = 0x04;
	public static int RCX_SYSTEMSOUND_FASTSWEEPUP = 0x05;

	// IRLink RCX: Transmitter Range
	public static int RCX_TXRANGE_SHORT = 0x00;
	public static int RCX_TXRANGE_LONG  = 0x01;


	// *** Variables *** //
	private boolean debugMode = false; // Debug mode flag.
	private byte addressBit = 0; // PF: Address Bit (toggleable)
	private byte toggleBit = 0;  // PF: Toggle Bit (toggleable)
	private byte toggleBitArray[] = new byte[4];  // PF: Toggle Bit Array. One toggleBit for every Channel

	private byte toggleBit4RCXCmd = 0; // RCX: Toggle 0x08 in opcode every command.

	private BitSet bits = new BitSet(MAX_BITS);
	private int nextBit = 0;

	public IRLink(Port port) {
		this(port.open(I2CPort.class));
	}

	// *** Constructor *** //
	public IRLink(I2CPort port) {
		super(port);
		System.out.println("Custom IRLink");
	}


	// *** Methods *** //

	/*
	 * IRLink: Power Function transmission mode methods.
	 */

	/**
	 * Send commands to both outputs of PF IR receiver.
	 * Use PF Extended Mode (no timeout on commands, toggle bit is verified on receiver).
	 *
	 * @param channel - PF channel number (PF_CHANNEL1 - PF_CHANNEL4)
	 * @param function - Command for Output A (Red) (PF_EM_COMMAND_BRAKEFLOAT_OUTPUT_A or PF_EM_COMMAND_INCREMENTSPEED_OUTPUT_A or PF_EM_COMMAND_DECREMENTSPEED_OUTPUT_A)
	 *                   or command for Output B (Blue) (PF_EM_COMMAND_TOGGLEFORWARDFLOAT_OUTPUT_B)
	 * 				 or special value of PF_EM_COMMAND_SYNCTOGGLEBIT with no operation, for setting toggle bit in sync
	 */
	public void sendPFExtended(int channel, int function) {
		// nibble1 contains Toggle bit, Escape bit (Mode selector bit) set to zero and two bits of selected channel
		toggleBit = toggleBitArray[channel];
		toggleBitArray[channel] ^= 1;
		byte nibble1 = (byte) ((toggleBit << 3) | (PF_BIT_0 << 2) | channel);
		// nibble2 contains Address bit (usually zero) and three bits of selected mode
		//  Extended Mode has Mode bits set to 000
		byte nibble2 = (byte) ((addressBit << 3) | PF_BIT_0);
		// nibble3 contains four bits of function for both outputs, selected from 5 possible commands.
		byte nibble3 = (byte) function;
		sendPFCommand(nibble1, nibble2, nibble3);
	}

	/**
	 * Send commands to both outputs of PF IR receiver.
	 * Use PF Combo Direct Mode (all commands have timeout, toggle bit is not verified on receiver).
	 *
	 * @param channel - PF channel number (PF_CHANNEL1 - PF_CHANNEL4)
	 * @param aCmd - Command for Output A (Red) (PF_CDM_COMMAND_FLOAT or PF_CDM_COMMAND_FORWARD or PF_CDM_COMMAND_BACKWARD or PF_CDM_COMMAND_BRAKE)
	 * @param bCmd - Command for Output B (Blue) (PF_CDM_COMMAND_FLOAT or PF_CDM_COMMAND_FORWARD or PF_CDM_COMMAND_BACKWARD or PF_CDM_COMMAND_BRAKE)
	 */
	public void sendPFComboDirect(int channel, int aCmd, int bCmd) {
		// nibble1 contains Toggle bit (always send 0, because it is not verified od receiver), Escape bit (Mode selector bit) set to zero and two bits of selected channel
		toggleBit = PF_BIT_0;
		byte nibble1 = (byte) ((toggleBit << 3) | (PF_BIT_0 << 2) | channel);
		// nibble2 contains Address bit (usually zero) and three bits of selected mode
		//  Combo Direct Mode has Mode bits set to 001
		byte nibble2 = (byte) ((addressBit << 3) | PF_BIT_1);
		// nibble3 contains two bits of command for Output B (Blue?) and two bits of operation for Output A (Red?)
		byte nibble3 = (byte) ((bCmd << 2) | aCmd);
		sendPFCommand(nibble1, nibble2, nibble3);
	}

	/**
	 * Send command to single output of PF IR receiver.
	 * Use PF Single Output Mode (no timeout on commands except "Full forward" and "Full backward", toggle bit is verified on receiver for increment/decrement/toggle commands).
	 *
	 * @param channel - PF channel number (PF_CHANNEL1 - PF_CHANNEL4)
	 * @param submode - Submode selector (PF_SOM_SUBMODE_PWM for PWM operations or PF_SOM_SUBMODE_CSTID for Clear/Set/Toggle/Inc/Dec operations)
	 * @param output - Output selector (PF_SOM_OUTPUT_A (Red) or PF_SOM_OUTPUT_B (Blue))
	 * @param command - Command for output (One of PF_PWM_COMMAND_* for submode=PF_SOM_SUBMODE_PWM or one of PF_CSTID_COMMAND_* for submode=PF_SOM_SUBMODE_CSTID)
	 */
	public void sendPFSingleOutput(int channel, int submode, int output, int command) {
		// nibble1 contains Toggle bit, Escape bit (Mode selector bit) set to zero and two bits of selected channel
		if (submode == PF_SOM_SUBMODE_CSTID) {
			switch (command) {
				case PF_CSTID_COMMAND_TOGGLEFULLFORWARD:
				case PF_CSTID_COMMAND_TOGGLEDIRECTION:
				case PF_CSTID_COMMAND_INCREMENT_NUMERICAL_PWM:
				case PF_CSTID_COMMAND_DECREMENT_NUMERICAL_PWM:
				case PF_CSTID_COMMAND_INCREMENT_PWM:
				case PF_CSTID_COMMAND_DECREMENT_PWM:
				case PF_CSTID_COMMAND_TOGGLEFULLFORWARDBACKWARD:
				case PF_CSTID_COMMAND_TOGGLE_C1:
				case PF_CSTID_COMMAND_TOGGLE_C2:
				case PF_CSTID_COMMAND_TOGGLEFULLBACKWARD:
					toggleBit = toggleBitArray[channel];
					toggleBitArray[channel] ^= 1;
					break;

				default:
					toggleBit = PF_BIT_0;
					break;
			}
		} else {
			toggleBit = PF_BIT_0;
		}
		byte nibble1 = (byte) ((toggleBit << 3) | (PF_BIT_0 << 2) | channel);
		// nibble2 contains Address bit (usually zero) and three bits of selected mode
		//  Single Output Mode has Mode bits set to 1MO, where M is submode selector (mode within Single Output Mode) and O is Output selector
		byte nibble2 = (byte) ((addressBit << 3) | (PF_BIT_1 << 2) | (submode << 1) | output);
		// nibble3 contains four bits of command for selected output and for selected submode
		byte nibble3 = (byte) command;
		sendPFCommand(nibble1, nibble2, nibble3);
	}

	/**
	 * Send commands to both outputs of PF IR receiver.
	 * Use PF Combo PWM Mode (all commands have timeout, toggle bit does not exists in command so it is not verified on receiver).
	 *
	 * @param channel - PF channel number (PF_CHANNEL1 - PF_CHANNEL4)
	 * @param aCmd - Command for Output A (Red) (One of PF_PWM_COMMAND_*)
	 * @param bCmd - Command for Output B (Blue) (One of PF_PWM_COMMAND_*)
	 */
	public void sendPFComboPWM(int channel, int aCmd, int bCmd) {
		// nibble1 contains Address bit, Escape bit (Mode selector bit) set to one and two bits of selected channel
		byte nibble1 = (byte) ((addressBit << 3) | (PF_BIT_1 << 2) | channel);
		// nibble2 contains command for output B (Blue)
		byte nibble2 = (byte) bCmd;
		// nibble3 contains command for output A (Red)
		byte nibble3 = (byte) aCmd;
		sendPFCommand(nibble1, nibble2, nibble3);
	}

	/*
	 * Encode and send bytes from submitted nibbles to the PF IR receiver.
	 */
	private void sendPFCommand(int nibble1, int nibble2, int nibble3) {
		byte lrc = (byte) (0xF ^ nibble1 ^ nibble2 ^ nibble3);
		int pfData = (nibble1 << 12) | (nibble2 << 8) | (nibble3 << 4) | lrc;

		clearBits();
		nextBit = 0;
		setBit(STOP_START_PAUSE); // Start bit
		for(int i=15;i>=0;i--) {
			setBit(((pfData >> i) & 1) == 0 ? LOW_BIT_PAUSE : HIGH_BIT_PAUSE);  // Data bits
		}
		setBit(STOP_START_PAUSE); // Stop bit
		byte[] pfCommand = new byte[16];

		for(int i =0;i<MAX_BITS;i++) {
			boolean bit = bits.get(i);
			int byteIndex = i/8;
			int bitVal = (bit ? 1 : 0);
			pfCommand[byteIndex] |= (bitVal << (7 - i%8));
		}

		pfCommand[13] = TX_MAX_BUFFER_LEN;
	     pfCommand[14] = TX_MODE_PF;
	     pfCommand[15] = 1;

		// Start IR transmission from IRLink in PF mode using I2C sendData
		sendData(TX_BUFFER, pfCommand, TX_MAX_BUFFER_LEN+3);

		// Wait 20 milliseconds for the transmission to complete
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {}
	}

	private void setBit(int pause) {
		bits.set(nextBit++);
		nextBit += pause;
	}

	private void clearBits() {
		for(int i=0;i<MAX_BITS;i++) bits.clear(i);
	}

	/*
	 * IRLink: RCX transmission mode methods.
	 */

	/**
	 * Send commands to the RCX acting as RCX Remote.
	 * Emulate RCX Remote Control button press.
	 *
	 * @param msg - RCX Remote Control IR message (One of RCX_REMOTE_*)
	 */
	public void sendRemoteCommand(int msg) {
		byte[] buf = new byte[3];
		buf[0] = OPCODE_REMOTE_COMMAND;
		buf[1] = (byte) (msg >> 8);
		buf[2] = (byte) (msg & 0xFF);
		sendPacket(buf);
	}

	/**
	 * Send command to the RCX acting as RCX Remote.
	 * Emulates press of one from five (P1, P2, P3, P4 or P5) RCX Remote Control buttons.
	 *
	 * @param programNumber - Number of program stored on RCX to run (One of RCX_REMOTE_P*)
	 */
	public void runProgram(int programNumber) {
		sendRemoteCommand(RCX_REMOTE_P1 << (programNumber - 1));
	}

	/**
	 * Send command to the RCX acting as RCX Remote.
	 * Emulate press of "Beep" (loudspeaker symbol) button on RCX Remote Control.
	 */
	public void beep() {
		sendRemoteCommand(RCX_REMOTE_BEEP);
	}

	/**
	 * Send command to the RCX acting as RCX Remote.
	 * Emulate press of "Stop" button on RCX Remote Control.
	 */
	public void stopAllPrograms() {
		sendRemoteCommand(RCX_REMOTE_STOP);
	}

	/**
	 * Send command to the RCX acting as RCX Remote.
	 * Emulate press of one from three motor forward button on RCX Remote Control.
	 *
	 * @param motor - Number of motor for which forward button will be emulated (0 = Motor A, 1 = Motor B, 2 = Motor C)
	 */
	public void forwardStep(int motor) {
		sendRemoteCommand(RCX_REMOTE_A_FWD << motor);
	}

	/**
	 * Send command to the RCX acting as RCX Remote.
	 * Emulate press of one from three motor backward button on RCX Remote Control.
	 *
	 * @param motor - Number of motor for which backward button will be emulated (0 = Motor A, 1 = Motor B, 2 = Motor C)
	 */
	public void backwardStep(int motor) {
		sendRemoteCommand(RCX_REMOTE_A_BWD << motor);
	}

	private synchronized byte[] rcxCommLoop(byte[] sBuf) {
		byte[] rBuf;
		byte savedOpcode = sBuf[0];
		int count = 1;

		// Try to send packet up to TXRX_MAX_RETRIES times, stop if response matches.
		do {
			toggleBit4RCXCmd ^= 0x08; // Change toggle bit here, because it must change every time in the same command.
			sBuf[0] = (byte) (savedOpcode + toggleBit4RCXCmd);

			sendPacket(sBuf);
			rBuf = getPacket();

			if ((rBuf != null) && (rBuf.length > 0)) {
				// Response opcode is the ~opcode of command
				if (rBuf[0] == ((byte) (0xff - (sBuf[0] & 0xff)))) { break; }
			}
			count++;
		} while (count < TXRX_MAX_RETRIES);

		// If loop iterations exceeded max iterations, set rBuf to null.
		if (count == TXRX_MAX_RETRIES) { rBuf = null; }
		//printDebugMessage("CommLoop: " + count);

		return rBuf;
	}

	/**
	 * Send start/stop command to the RCX motor ports.
	 *
	 * @param motorList - Motor or motors for which start command will be issued (RCX_MOTOR_A and/or RCX_MOTOR_B and/or RCX_MOTOR_C)
	 * @param command - Command to be executed for motors selected in motorList (RCX_MOTOR_FLOAT or RCX_MOTOR_STOP (synonym RCX_MOTOR_OFF) or RCX_MOTOR_START (synonym RCX_MOTOR_ON))
	 */
	public void motorStartStop(int motorList, int command) {
		byte[] sBuf = new byte[2];
		byte[] rBuf;

		// Put data in send buffer.
		sBuf[0] = OPCODE_SET_MOTOR_ON_OFF; // opcode
		sBuf[1] = (byte) ((motorList + command) & 0xff);

		// Call communication loop.
		rBuf = rcxCommLoop(sBuf);
	}

	/**
	 * Send rotation direction command to the RCX motor ports.
	 *
	 * @param motorList - Motor or motors for which set direction command will be issued (RCX_MOTOR_A and/or RCX_MOTOR_B and/or RCX_MOTOR_C)
	 * @param direction - Direction command for motors selected in motorList (RCX_MOTOR_BACKWARD or RCX_MOTOR_REVERSE or RCX_MOTOR_FORWARD)
	 */
	public void motorDirection(int motorList, int direction) {
		byte[] sBuf = new byte[2];
		byte[] rBuf;

		// Put data in send buffer.
		sBuf[0] = OPCODE_SET_MOTOR_DIRECTION; // opcode
		sBuf[1] = (byte) ((motorList + direction) & 0xff);

		// Call communication loop.
		rBuf = rcxCommLoop(sBuf);
	}

	/**
	 * Send motor power command to the RCX motor ports.
	 *
	 * @param motorList - Motor or motors for which set power command will be issued (RCX_MOTOR_A and/or RCX_MOTOR_B and/or RCX_MOTOR_C)
	 * @param power - Desired power level for motors selected in motorList
	 */
	public void motorPower(int motorList, int power) {
		byte[] sBuf = new byte[4];
		byte[] rBuf;

		// Put data in send buffer.
		sBuf[0] = OPCODE_SET_MOTOR_POWER; // opcode
		sBuf[1] = (byte) (motorList & 0xff);
		sBuf[2] = (byte) 2;	// Power Source = Constant
		sBuf[3] = (byte) (power & 0xff); // Power Value

		// Call communication loop.
		rBuf = rcxCommLoop(sBuf);
	}

	/**
	 * Set type of sensor connected to RCX.
	 *
	 * @param sensorNumber - Sensor port for which sensor type will be set (RCX_SENSOR_1 or RCX_SENSOR_2 or RCX_SENSOR_3)
	 * @param sensorType - Sensor type to set (RCX_SENSOR_TYPE_NONE or RCX_SENSOR_TYPE_TOUCH or RCX_SENSOR_TYPE_TEMPERATURE or RCX_SENSOR_TYPE_LIGHT or RCX_SENSOR_TYPE_ANGLE)
	 * 	Default modes of operation for each type of sensor:
	 * 		Raw sensor (RCX_SENSOR_TYPE_NONE) -> Raw Mode (RCX_SENSOR_MODE_RAW)
	 * 		Touch sensor (RCX_SENSOR_TYPE_TOUCH) -> Boolean (RCX_SENSOR_MODE_BOOLEAN)
	 * 		Temperature sensor (RCX_SENSOR_TYPE_TEMPERATURE) -> Temp in Celsius (RCX_SENSOR_MODE_CELSIUS)
	 * 		Light sensor (RCX_SENSOR_TYPE_LIGHT) -> Percentage (RCX_SENSOR_MODE_PCTFULLSCALE)
	 * 		Rotation sensor (RCX_SENSOR_TYPE_ANGLE) -> Angle (RCX_SENSOR_MODE_ANGLESTEPS)
	 */
	public void setSensorType(int sensorNumber, int sensorType) {
		byte[] sBuf = new byte[3];
		byte[] rBuf;

		// Put data in send buffer.
		sBuf[0] = OPCODE_SET_SENSOR_TYPE; // opcode
		sBuf[1] = (byte) (sensorNumber & 0xff);
		sBuf[2] = (byte) (sensorType & 0xff);

		// Call communication loop.
		rBuf = rcxCommLoop(sBuf);
	}

	/**
	 * Set absolute measurement mode of sensor connected to RCX and clear sensor value.
	 *
	 * @param sensorNumber - Sensor port for which sensor mode will be set (RCX_SENSOR_1 or RCX_SENSOR_2 or RCX_SENSOR_3)
	 * @param sensorMode - Sensor type to set (RCX_SENSOR_TYPE_NONE or RCX_SENSOR_TYPE_TOUCH or RCX_SENSOR_TYPE_TEMPERATURE or RCX_SENSOR_TYPE_LIGHT or RCX_SENSOR_TYPE_ANGLE)
	 * 	Default modes of operation for each type of sensor:
	 * 		Raw Mode (RCX_SENSOR_MODE_RAW) -> Value in 0..1023
	 * 		Boolean (RCX_SENSOR_MODE_BOOLEAN) -> Either 0 or 1
	 * 		Edge count (RCX_SENSOR_MODE_TRANSITIONCUT) -> Number of boolean transitions
	 * 		Pulse count (RCX_SENSOR_MODE_PERIODCOUNTER) -> Number of boolean transitions divided by two
	 * 		Percentage (RCX_SENSOR_MODE_PCTFULLSCALE) -> Raw value scaled to 0..100
	 * 		Temperature in Celsius (RCX_SENSOR_MODE_CELSIUS) -> 1/10ths of a degree, -19.8..69.5
	 * 		Temperature in Fahrenheit (RCX_SENSOR_MODE_FAHRENHEIT) -> 1/10ths of a degree, -3.6..157.1
	 * 		Angle (RCX_SENSOR_MODE_ANGLESTEPS) -> 1/16ths of a rotation, represented as a signed short
	 *
	 * 	Sensor mode in this method is set using absolute measurement mode. To set sensor mode in
	 * 	dynamic measurement with selected slope value use setSensorModeWithSlope method.
	 */
	public void setSensorMode(int sensorNumber, int sensorMode) {
		setSensorModeWithSlope(sensorNumber, sensorMode, 0);
	}
	/**
	 * Set dynamic measurement mode of sensor connected to RCX and clear sensor value.
	 * This mode is for specific behavior of Boolean, Edge count and Pulse count modes.
	 * In most cases absolute measurement mode should be used.
	 *
	 * @param sensorNumber - Sensor port for which sensor mode will be set (RCX_SENSOR_1 or RCX_SENSOR_2 or RCX_SENSOR_3)
	 * @param sensorMode - Sensor type to set (RCX_SENSOR_TYPE_NONE or RCX_SENSOR_TYPE_TOUCH or RCX_SENSOR_TYPE_TEMPERATURE or RCX_SENSOR_TYPE_LIGHT or RCX_SENSOR_TYPE_ANGLE)
	 * @param slope - 0 for absolute measurement, 1..31 for dynamic (relative) measurement
	 *
	 * The slope value controls 0/1 detection for the three boolean modes. A slope of 0 causes raw sensor values
	 * greater than 562 to cause a transition to 0 and raw sensor values less than 460 to cause a transition to 1.
	 * The hysteresis prevents bouncing between 0 and 1 near the transition point. A slope value in 1..31, inclusive,
	 * causes a transition to 0 or to 1 whenever the difference between consecutive raw sensor values exceeds
	 * the slope. Increases larger than the slope result in transition to 0, while decreases larger than the slope
	 * result in transition to 1.
	 * Note the inversions: high raw values correspond to a boolean 0, while low raw values correspond to a boolean 1.
	 */
	public void setSensorModeWithSlope(int sensorNumber, int sensorMode, int slope) {
		byte[] sBuf = new byte[3];
		byte[] rBuf;

		// Put data in send buffer.
		sBuf[0] = OPCODE_SET_SENSOR_MODE; // opcode
		sBuf[1] = (byte) (sensorNumber & 0xff);
		sBuf[2] = (byte) (((sensorMode & 0xff) << 5) + (slope & 0xff));

		// Call communication loop.
		rBuf = rcxCommLoop(sBuf);
	}

	/**
	 * Play system sound on RCX.
	 *
	 * @param sound - System sound to play (One of RCX_SYSTEMSOUND_*)
	 */
	public void playSystemSound(int sound) {
		byte[] sBuf = new byte[2];
		byte[] rBuf;

		// Put data in send buffer.
		sBuf[0] = 0x51; // opcode
		sBuf[1] = (byte) (sound & 0xff);

		// Call communication loop.
		rBuf = rcxCommLoop(sBuf);
	}

	/**
	 * Set tramsmitter range on RCX.
	 *
	 * @param txrange - Transmit range to set (RCX_TXRANGE_SHORT or RCX_TXRANGE_LONG)
	 */
	public void setTXRange(int txrange) {
		byte[] sBuf = new byte[2];
		byte[] rBuf;

		// Put data in send buffer.
		sBuf[0] = 0x31; // opcode
		sBuf[1] = (byte) (txrange & 0xff);

		// Call communication loop.
		rBuf = rcxCommLoop(sBuf);
	}
	public void setShortRange() {
		setTXRange(RCX_TXRANGE_SHORT);
	}
	public void setLongRange() {
		setTXRange(RCX_TXRANGE_LONG);
	}

	/**
	 * Send alive to the RCX, to reset power-down counter.
	 *
	 */
	public void sendAlive() {
		byte[] sBuf = new byte[1];
		byte[] rBuf;

		// Put data in send buffer.
		sBuf[0] = OPCODE_ALIVE; // opcode

		// Call communication loop.
		rBuf = rcxCommLoop(sBuf);
	}

	/**
	 * Get battery level from RCX.
	 *
	 */
	public double getBatteryLevel() {
		byte[] sBuf = new byte[1];
		byte[] rBuf;
		double rc = 0.0;

		// Put data in send buffer.
		sBuf[0] = OPCODE_GET_BATTERY_POWER; // opcode

		// Call communication loop.
		rBuf = rcxCommLoop(sBuf);

		if (rBuf != null) {
			rc = ((rBuf[2] << 8) + (rBuf[1] & 0xff)) / 1000.0;
		}
		return rc;
	}

	/**
	 * Poll source value from RCX.
	 *
	 * @param source - Source type to poll (0-35)
	 * @param value - Source value (index dependant from type)
	 */
	public byte[] pollSourceValue(int source, int value) {
		byte[] sBuf = new byte[3];
		byte[] rBuf;

		// Put data in send buffer.
		sBuf[0] = OPCODE_GET_VALUE; // opcode
		sBuf[1] = (byte) (source & 0xff);
		sBuf[2] = (byte) (value & 0xff);

		// Call communication loop.
		rBuf = rcxCommLoop(sBuf);

		return rBuf;
	}

	/*
	 * Read the specific source of sensor value (9, 12 or 13) from RCX and return it converted to integer.
	 */
	private int pollFromSensor(int source, int value) {
		byte[] rBuf = pollSourceValue(source, value);
		int rc = 0;

		// rBuf is 0xE5/0xED, Low Byte, High Byte
		if (rBuf != null) {
			rc = ((rBuf[2] << 8) + (rBuf[1] & 0xff));
		}
		// Return 0 if read fails.
		return rc;
	}

	/**
	 * Sensor raw read - Get the un-processed 10 bit A/D value, straight off the hardware.
	 *
	 * @param sensorNumber - Sensor port for which sensor mode will be set (RCX_SENSOR_1 or RCX_SENSOR_2 or RCX_SENSOR_3)
	 */
	public int getSensorUnprocessedRawValue(int sensorNumber) {
		return pollFromSensor(12, sensorNumber);
	}
	/**
	 * Sensor raw boolean read - Get the raw value, with thresholds and hysteresis at 45/55%.
	 *
	 * @param sensorNumber - Sensor port for which sensor mode will be set (RCX_SENSOR_1 or RCX_SENSOR_2 or RCX_SENSOR_3)
	 */
	public boolean getSensorUnprocessedBooleanValue(int sensorNumber) {
		int rc = pollFromSensor(13, sensorNumber);

		return (rc == 1);
	}

	/**
	 * Methods for reading values from sensors. The returned value is generated in the firmware based on sensor type and mode.
	 *
	 * Default modes of operation for each type of sensor:
	 * 	Raw Mode (RCX_SENSOR_MODE_RAW) -> Value in 0..1023 -> Use getSensorIntValue method
	 * 	Boolean (RCX_SENSOR_MODE_BOOLEAN) -> Either 0 or 1 -> Use getSensorBoolValue method or getSensorIntValue method
	 * 	Edge count (RCX_SENSOR_MODE_TRANSITIONCUT) -> Number of boolean transitions -> Use getSensorIntValue method
	 * 	Pulse count (RCX_SENSOR_MODE_PERIODCOUNTER) -> Number of boolean transitions divided by two -> Use getSensorIntValue method
	 * 	Percentage (RCX_SENSOR_MODE_PCTFULLSCALE) -> Raw value scaled to 0..100 -> Use getSensorIntValue method
	 * 	Temp in Celsius (RCX_SENSOR_MODE_CELSIUS) -> 1/10ths of a degree, -19.8..69.5 -> Use getSensorDoubleValue method
	 * 	Temp in Fahrenheit (RCX_SENSOR_MODE_FAHRENHEIT) -> 1/10ths of a degree, -3.6..157.1 -> Use getSensorDoubleValue method
	 * 	Angle (RCX_SENSOR_MODE_ANGLESTEPS) -> 1/16ths of a rotation, represented as a signed short -> Use getSensorIntValue method
	 *
	 * @param sensorNumber - Sensor port for which sensor mode will be set (RCX_SENSOR_1 or RCX_SENSOR_2 or RCX_SENSOR_3)
	 */
	public int getSensorIntValue(int sensorNumber) {
		int rc = pollFromSensor(9, sensorNumber);
		return rc;
	}
	public boolean getSensorBooleanValue(int sensorNumber) {
		int rc = pollFromSensor(9, sensorNumber);
		return (rc == 1);
	}
	public double getSensorDoubleValue(int sensorNumber) {
		double rc = pollFromSensor(9, sensorNumber) / 10.0;
		return rc;
	}

	/**
	 * Play tone on RCX. Very specific method which ignores and don't wait for
	 * value returned by RCX to minimize silence time between consecutive tones.
	 *
	 * @param frequency - Tone freqency measured in Hz.
	 * @param duration - Duration of tone measured in 1/100ths of a second.
	 * 		Sending this command takes 65ms so using duration less than 65ms (values 1 to 6)
	 * 		results in a silence of a predetermined length between two consecutive tones.
	 */
	public void playTone(int frequency, int duration) {
		byte[] sBuf = new byte[4];

		// Put data in send buffer.
		toggleBit4RCXCmd ^= 0x08; // Change toggle bit.
		sBuf[0] = (byte) (0x23 + toggleBit4RCXCmd);
		sBuf[1] = (byte) (frequency & 0xff);
		sBuf[2] = (byte) ((frequency >> 8) & 0xff);
		sBuf[3] = (byte) (duration & 0xff);

		sendPacket(sBuf);

		// Sending this command takes circa 65ms. So we must wait for duration-65ms before starting next tone command.
		int sendTime = 65;
		if ((duration * 10) > sendTime) {
			try {
				Thread.sleep((duration * 10) - sendTime);
			} catch (InterruptedException e) {}
		}
	}


	/**
	 * Method for sending raw data packet to the RCX.
	 * Methods adds header, data bytes encoding and checksum before sending them.
	 *
	 * @param data - byte data of data packet (opcode with required parameters) to send
	 */
	public void sendPacket(byte[] data) {
		byte[] packet = new byte[data.length*2+5];
		int checksum = 0;

		// Header
		packet[0] = (byte) 0x55;
		packet[1] = (byte) 0xff;
		packet[2] = (byte) 0x00;

		//Data
		for(int i=0;i<data.length;i++) {
			packet[2*i + 3] = data[i];
			checksum += data[i];
			//Complement
			packet[2*i + 4] = (byte) (0xff - (data[i] & 0xff));
		}

		// Checksum
		checksum &= 0xff;
		packet[2*data.length+3] = (byte) checksum;
		packet[2*data.length+4] = (byte)(255 - checksum);

		// Send the packet
		sendBytes(packet,packet.length);
	}

	/*
	 * Send bytes from prepared packet to the RCX
	 */
	public void sendBytes(byte[] data, int len) {
		byte[] buf = new byte[8];
		int bufLen = 0;

		// Split data into 8 byte chunks and send it
		for(int i=0; i< (len-1)/8 + 1;i++) {
			bufLen = 0;
			for(int j=i*8;j<(i+1)*8 && j < len;j++) {
				buf[j % 8] = data[j];
				bufLen++;
			}
			sendBytes8(buf, bufLen);

			// Wait 5 milliseconds per byte for the transmission to complete
			try {
				Thread.sleep(bufLen * 5);
			} catch (InterruptedException e) {}
		}
	}

	/*
	 * Send up to 8 bytes to the RCX
	 */
	private void sendBytes8(byte[] data, int len) {
		byte[] buf = new byte[len+3];
		int register = TX_BUFFER_LEN - len;

		// Copy data up to the byte before TX_BUFFER_LEN
		for(int i=0;i<len;i++) buf[i] = data[i];
		buf[len] = (byte) len;
		buf[len+1] = TX_MODE_RCX;
		buf[len+2] = (byte) 1;

		// Start IR transmission from IRLink in RCX mode using I2C sendData
		sendData(register,buf, len+3);
	}

	/*
	 * Read buffer of IRLink sensor and return it's contents if any.
	 */
	public void clearReadBuffer() {
		// Clear whole buffer
		byte[] count = new byte[16];
		sendData(RX_BUFFER_LEN, count, 16);
		return;
	}
	public byte[] getReadBuffer() {
		byte[] count = new byte[1];

		// Wait 5 milliseconds per expected byte of answer.
		try {
			Thread.sleep(100); // 16 (bytes) * 5 ms = 80 ms
		} catch (InterruptedException e) {}

		// Check how many bytes has been received.
		getData(RX_BUFFER_LEN, count, 1);
		int dataSize = count[0];

		// Create buffer and fill it with received data.
		if (dataSize > 0) {
			byte[] buffer = new byte[dataSize];
			getData(RX_BUFFER, buffer, dataSize);
			return buffer;
		}
		return null;
	}

	public byte[] getPacket() {
		byte[] buffer = getReadBuffer();

		clearDebugMessage();

		if (buffer != null) {
			// Something has been received.
			if ((buffer.length % 2) == 0) {
				// Error: Buffer size must be odd number, because every byte has its complement, and preamble is always 3 bytes long.
				printDebugMessage("ERR: Even size");
				return null;
			}

			if (buffer.length < 7) {
				// Error: Buffer size must be at least 7 bytes to contain data.
				printDebugMessage("ERR: No data");
				return null;
			}

			// Check preamble
			if ((buffer[0] != ((byte) 0x55))
			 || (buffer[1] != ((byte) 0xff))
			 || (buffer[2] != ((byte) 0x00))) {
			 	// Malformed preamble.
				printDebugMessage("ERR: Preamble");
			 	return null;
			}

			// Allocate packet. It is half of buffer size without preamble and checksum.
			byte[] packet = new byte[(buffer.length -3 -2) / 2];
			byte checksum = 0;
			byte complement = 0;

			for(int i = 3; i < buffer.length-2; i=i+2) {
				// Check byte complement.
				complement = (byte) (0xff - (buffer[i] & 0xff));
				if (buffer[i+1] != complement) {
				 	// Wrong byte complement.
				 	int bytePos = (i-3)/2;
					printDebugMessage("ERR: Byte " + bytePos);
				 	return null;
				}
				// Save byte value.
				packet[(i-3)/2] = buffer[i];
				checksum += buffer[i];
			}

			// Check checksum.
			checksum &= 0xff;
			if (checksum != buffer[buffer.length-2]) {
				// Wrong checksum.
				printDebugMessage("ERR: Packet CRC");
				return null;
			}

			// Check checksum complement.
			complement = (byte) (0xff - (checksum & 0xff));
			if (complement != buffer[buffer.length-1]) {
				// Wrong checksum complement.
				printDebugMessage("ERR: CRC Compl.");
				return null;
			}

			// Everything is OK. Return packet.
			return packet;
		} else {
			// Buffer is null, so nothing was received. Is RCX turned on?
			printDebugMessage("ERR: No answer");
		}
		return null;
	}

	/* Helper methods */
	public void setDebugMode(boolean flag) {
		debugMode = flag;
	}
	private void printDebugMessage(String message) {
		if (debugMode) {
			LCD.drawString(message, 0, 7);
		}
	}
	private void clearDebugMessage() {
		printDebugMessage("                ");
	}
	public void showReceiveBuffer() {
		if (debugMode) {
			byte[] buffer = new byte[16];
			getData(RX_BUFFER_LEN, buffer, 16);
			LCD.drawString(buffer[0]  + " " + buffer[1]  + " " + buffer[2]  + " " + buffer[3],  0, 3);
			LCD.drawString(buffer[4]  + " " + buffer[5]  + " " + buffer[6]  + " " + buffer[7],  0, 4);
			LCD.drawString(buffer[8]  + " " + buffer[9]  + " " + buffer[10] + " " + buffer[11], 0, 5);
			LCD.drawString(buffer[12] + " " + buffer[13] + " " + buffer[14] + " " + buffer[15], 0, 6);
		}
	}
}
