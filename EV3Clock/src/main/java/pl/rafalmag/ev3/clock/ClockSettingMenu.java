package pl.rafalmag.ev3.clock;

public enum ClockSettingMenu {
	HOUR_PLUS("hour +"),
	HOUR_MINUS("hour -"),
	MINUTE_PLUS("minute +"),
	MINUTE_MINUS("minute -"),
	OK("OK");

	private final String name;

	ClockSettingMenu(String name) {
		this.name = name;
	}

	public static String[] getNames() {
		ClockSettingMenu[] values = ClockSettingMenu.values();
		String[] names = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			names[i] = values[i].name;
		}
		return names;
	}
}
