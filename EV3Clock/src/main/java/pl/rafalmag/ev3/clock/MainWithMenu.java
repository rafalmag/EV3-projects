package pl.rafalmag.ev3.clock;

import java.util.concurrent.TimeUnit;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.internal.ev3.EV3LCDManager;
import lejos.internal.ev3.EV3LCDManager.LCDLayer;
import lejos.robotics.MirrorMotor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.LcdUtil;
import pl.rafalmag.ev3.Time;
import pl.rafalmag.systemtime.SystemTime;

@SuppressWarnings("restriction")
public class MainWithMenu {

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
		// similar to lejos.internal.ev3.EV3Wrapper.switchToLayer(String)
		for (LCDLayer layer : EV3LCDManager.getLocalLCDManager().getLayers()) {
			if (!layer.getName().equalsIgnoreCase("LCD")) {
				layer.setVisible(false);
			}
		}
	}

	private final MainMenuController mainMenuController;
	private final AnalogClock clock;
	private final ClockSettingMenuController clockSettingMenuController;

	public MainWithMenu(AnalogClock clock) {
		this.clock = clock;
		clockSettingMenuController = new ClockSettingMenuController(clock);
		mainMenuController = new MainMenuController(clock,
				clockSettingMenuController);
	}

	public void start() {
		addRunningLeds();
		addShutdownHook();
		clockSettingMenuController.clockSettingMenu();
		LCD.clear();
		mainMenuController.mainMenuLoop();
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

	private void stopApp() {
		clock.stop();
		Button.LEDPattern(0);
		LCD.clear();
		LcdUtil.displayLargeText(
				BrickFinder.getDefault().getTextLCD(Font.getLargeFont()),
				"Bye!");
		log.info("bye");
	}
}
