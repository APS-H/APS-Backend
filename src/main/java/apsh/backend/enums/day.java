package apsh.backend.enums;

public enum day {
    Monday("星期一"),

    Tuesday("星期二"),

    Wednesday("星期三"),

    Thursday("星期四"),

    Friday("星期五"),

    Saturday("星期六"),

    Sunday("星期天");

    private final String value;

    day(String value) {
        this.value = value;
    }

    public static day valueOf(int value) {
        switch (value) {
            case 1:
                return Monday;
            case 2:
                return Tuesday;
            case 3:
                return Wednesday;
            case 4:
                return Thursday;
            case 5:
                return Friday;
            case 6:
                return Saturday;
            case 7:
                return Sunday;
            default:
                return null;
        }
    }

    public String value() {
        return this.value;
    }

    public Integer intValue() {
        switch (value) {
            case "星期一":
                return 1;
            case "星期二":
                return 2;
            case "星期三":
                return 3;
            case "星期四":
                return 4;
            case "星期五":
                return 5;
            case "星期六":
                return 6;
            case "星期天":
                return 7;
            default:
                return null;
        }
    }
}
