package pl.rafalmag.ev3.clock;

import java.util.concurrent.TimeUnit;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.internal.ev3.EV3LCDManager;
import lejos.internal.ev3.EV3LCDManager.LCDLayer;
import lejos.robotics.MirrorMotor;
import lejos.utility.TextMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.Time;
import pl.rafalmag.ev3.TimeUtil;
import pl.rafalmag.systemtime.SystemTime;

@SuppressWarnings("restriction")
public class MainWithMenu {

	private static final int TIMEOUT_MS = 50;
	private static final Logger log = LoggerFactory
			.getLogger(MainWithMenu.class);

	public static void main(String[] args) {
		log.info("Initializing...");
		LCD.clear();
		disableOtherLogLayers();
		LCD.clear();
		Button.setKeyClickVolume(1);
		SystemTime.initSysTime();
		ClockProperties clockProperties = ClockProperties.getInstance();
		AnalogClock clock = new AnalogClock(clockProperties, new TickPeriod(5,
				TimeUnit.SECONDS), new Time(0, 20),
				MirrorMotor
						.invertMotor(new EV3MediumRegulatedMotor(MotorPort.A)),
				MirrorMotor
						.invertMotor(new EV3LargeRegulatedMotor(MotorPort.B)));
		MainWithMenu mainWithMenu = new MainWithMenu(clock);
		log.info("Ready");
		Sound.beep();
		mainWithMenu.start();
	}

	private static void disableOtherLogLayers() {
		for (LCDLayer layer : EV3LCDManager.getLocalLCDManager().getLayers()) {
			if (!layer.getName().equalsIgnoreCase("LCD")) {
				layer.setVisible(false);
			}
		}
	}

	private final AnalogClock clock;

	public MainWithMenu(AnalogClock clock) {
		this.clock = clock;
	}

	public void start() {
		addRunningLeds();
		addShutdownHook();
		clockSettingMenu();
		LCD.clear();
		mainMenuLoop();
		LCD.clear();
		stopApp();
	}

	private void addRunningLeds() {
		clock.getClockRunning().addObserver(new ClockRunningObserver() {

			@Override
			public void update(ClockRunning clockRunning, Boolean running) {
				if (running) {
					Button.LEDPattern(1); // green
				} else {
					Button.LEDPattern(0); // blank
				}
			}
		});
	}

	private void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				stopApp();
			}
		}, "Shutdown hook"));
	}

	public void clockSettingMenu() {
		Time time = clock.getTime();
		LCD.drawString("Clock setting", 0, 0);
		TextMenu textMenu = new TextMenu(ClockSettingMenu.getNames(), 2);
		int lastSelected = 0;
		while (true) {
			textMenu.setTitle(time.toString());
			// blocking here
			lastSelected = textMenu.select(lastSelected);
			if (lastSelected < 0) { // esc
				clock.setTime(time);
				return;
			}
			ClockSettingMenu clockSettingMenu = ClockSettingMenu.values()[lastSelected];
			switch (clockSettingMenu) {
			case HOUR_MINUS:
				time = time.minusHour();
				break;
			case HOUR_PLUS:
				time = time.plusHour();
				break;
			case MINUTE_MINUS:
				time = time.minusMinute();
				break;
			case MINUTE_PLUS:
				time = time.plusMinute();
				break;
			case OK:
				clock.setTime(time);
				return;
			default:
				throw new IllegalStateException("Not supported enum value = "
						+ clockSettingMenu);
			}
		}
	}

	public void mainMenuLoop() {
		LCD.drawString("Clock setting", 0, 0);
		TextMenu textMenu = new TextMenu(MainMenu.getNames(), 2);
		int lastSelected = 0;
		while (true) {
			textMenu.setTitle(clock.getTime().toString());
			// blocking here
			lastSelected = textMenu.select(lastSelected);
			if (lastSelected < 0) { // esc
				return;
			}
			MainMenu mainMenu = MainMenu.values()[lastSelected];
			switch (mainMenu) {
			case AUTO:
				clock.autoSet(SystemTime.getDate());
				break;
			case BACKWARD:
				clock.fastBackward();
				waitTillEnterIsDown();
				clock.stop();
				break;
			case FORWARD:
				clock.fastForward();
				waitTillEnterIsDown();
				clock.stop();
				break;
			case TOGGLE_RUN:
				clock.toggleStart();
				displayTime();
				break;
			case HAND_SETTINGS:
				LCD.clear();
				clockSettingMenu();
				LCD.clear();
				break;
			default:
				throw new IllegalStateException("Not supported enum value = "
						+ mainMenu);
			}
		}
	}

	/**
	 * Displays time and waits for any button to be pressed.
	 */
	private void displayTime() {
		TextLCD timeLCD = BrickFinder.getDefault().getTextLCD(
				Font.getLargeFont());
		LCD.clear();
		do {
			String timeString = clock.getTime().toString();
			displayLargeText(timeLCD, timeString);
		} while (Button.waitForAnyPress(TIMEOUT_MS) == 0);
		timeLCD.clear();
	}

	private void displayLargeText(TextLCD textLCD, String text) {
		textLCD.clear();
		textLCD.drawString(text,
				(textLCD.getTextWidth() - text.length() + 1) / 2, 1);
	}

	private void waitTillEnterIsDown() {
		while (Button.ENTER.isDown()) {
			TimeUtil.sleep(TIMEOUT_MS);
		}
	}

	private void stopApp() {
		clock.stop();
		Button.LEDPattern(0);
		LCD.clear();
		displayLargeText(
				BrickFinder.getDefault().getTextLCD(Font.getLargeFont()),
				"Bye!");
		log.info("bye");
	}
}
