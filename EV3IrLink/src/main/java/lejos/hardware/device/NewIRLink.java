package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;

import java.text.DecimalFormat;
import java.util.BitSet;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Supports for HiTechnic NXT IRLink Sensor (NIL1046) IRLink.
 *
 * @author Lawrie Griffiths
 * @author Szymon Madej
 */
public class NewIRLink extends IRLink {

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

    // *** Variables *** //
    private byte addressBit = 0; // PF: Address Bit (toggleable)
    private byte toggleBit = 0;  // PF: Toggle Bit (toggleable)
    private byte toggleBitArray[] = new byte[4];  // PF: Toggle Bit Array. One toggleBit for every Channel

    private BitSet bits = new BitSet(MAX_BITS);
    private int nextBit = 0;

    public NewIRLink(Port port) {
        super(port);
//        System.out.println("Custom NewIRLink");
    }

    // *** Constructor *** //
    public NewIRLink(I2CPort port) {
        super(port);
//        System.out.println("Custom NewIRLink");
    }

    // *** Methods *** //

	/*
     * IRLink: Power Function transmission mode methods.
	 */

    /**
     * Send commands to both outputs of PF IR receiver.
     * Use PF Extended Mode (no timeout on commands, toggle bit is verified on receiver).
     *
     * @param channel  - PF channel number (PF_CHANNEL1 - PF_CHANNEL4)
     * @param function - Command for Output A (Red) (PF_EM_COMMAND_BRAKEFLOAT_OUTPUT_A or PF_EM_COMMAND_INCREMENTSPEED_OUTPUT_A or PF_EM_COMMAND_DECREMENTSPEED_OUTPUT_A)
     *                 or command for Output B (Blue) (PF_EM_COMMAND_TOGGLEFORWARDFLOAT_OUTPUT_B)
     *                 or special value of PF_EM_COMMAND_SYNCTOGGLEBIT with no operation, for setting toggle bit in sync
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
     * @param aCmd    - Command for Output A (Red) (PF_CDM_COMMAND_FLOAT or PF_CDM_COMMAND_FORWARD or PF_CDM_COMMAND_BACKWARD or PF_CDM_COMMAND_BRAKE)
     * @param bCmd    - Command for Output B (Blue) (PF_CDM_COMMAND_FLOAT or PF_CDM_COMMAND_FORWARD or PF_CDM_COMMAND_BACKWARD or PF_CDM_COMMAND_BRAKE)
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
     * @param output  - Output selector (PF_SOM_OUTPUT_A (Red) or PF_SOM_OUTPUT_B (Blue))
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
     * @param aCmd    - Command for Output A (Red) (One of PF_PWM_COMMAND_*)
     * @param bCmd    - Command for Output B (Blue) (One of PF_PWM_COMMAND_*)
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

//    static DecimalFormat myFormatter = new DecimalFormat("0000,0000,0000,0000");
//
//    static public String format(int value) {
//        // String.format("%,016d" , Long.valueOf(Integer.toBinaryString(pfData)))
//        return myFormatter.format(Long.valueOf(Integer.toBinaryString(value)));
//    }

    /*
     * Encode and send bytes from submitted nibbles to the PF IR receiver.
     */
    private void sendPFCommand(int nibble1, int nibble2, int nibble3) {
        byte lrc = (byte) (0xF ^ nibble1 ^ nibble2 ^ nibble3);
        int pfData = (nibble1 << 12) | (nibble2 << 8) | (nibble3 << 4) | lrc;

//        System.out.println("pfData = " + format(pfData));

        clearBits();
        nextBit = 0;
        setBit(STOP_START_PAUSE); // Start bit
        for (int i = 15; i >= 0; i--) {
            setBit(((pfData >> i) & 1) == 0 ? LOW_BIT_PAUSE : HIGH_BIT_PAUSE);  // Data bits
        }
        setBit(STOP_START_PAUSE); // Stop bit
        byte[] pfCommand = new byte[16];

        for (int i = 0; i < MAX_BITS; i++) {
            boolean bit = bits.get(i);
            int byteIndex = i / 8;
            int bitVal = (bit ? 1 : 0);
            pfCommand[byteIndex] |= (bitVal << (7 - i % 8));
        }

        pfCommand[13] = TX_MAX_BUFFER_LEN;
        pfCommand[14] = TX_MODE_PF;
        pfCommand[15] = 1;

        // Start IR transmission from IRLink in PF mode using I2C sendData
        sendData(TX_BUFFER, pfCommand, TX_MAX_BUFFER_LEN + 3);

        // Wait 20 milliseconds for the transmission to complete
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
        }
    }

    private void setBit(int pause) {
        bits.set(nextBit++);
        nextBit += pause;
    }

    private void clearBits() {
        for (int i = 0; i < MAX_BITS; i++) bits.clear(i);
    }

}
