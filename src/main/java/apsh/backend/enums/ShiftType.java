package apsh.backend.enums;

public enum ShiftType {
    // 早班
    DAY_SHIFT("早班"),
    // 晚班
    NIGHT_SHIFT("晚班");

    private final String value;

    ShiftType(String value) {
        this.value = value;
    }

    public static ShiftType valueOf(int value) {
        switch (value) {
            case 0:
                return DAY_SHIFT;
            case 1:
                return NIGHT_SHIFT;
            default:
                return null;
        }
    }

    public String value() {
        return this.value;
    }
}
