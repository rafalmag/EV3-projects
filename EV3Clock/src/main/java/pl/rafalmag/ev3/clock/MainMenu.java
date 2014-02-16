package pl.rafalmag.ev3.clock;

public enum MainMenu {
	FORWARD("manual forward"),
	BACKWARD("manual backward"),
	AUTO("auto"),
	TOGGLE_RUN("toggle run"),
	HAND_SETTINGS("hand settings");
	// CUCKOO; // TODO on/off/manual, sound on/off
	// hand_settings

	private final String name;

	private MainMenu(String name) {
		this.name = name;
	}

	public static String[] getNames() {
		MainMenu[] values = MainMenu.values();
		String[] names = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			names[i] = values[i].name;
		}
		return names;
	}

}
