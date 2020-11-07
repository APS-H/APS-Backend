package apsh.backend.enums;

import apsh.backend.po.Shift;
import apsh.backend.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.sql.Time;

public enum ShiftType {
    // 早班
    DAY_SHIFT("早班"),
    // 晚班
    NIGHT_SHIFT("晚班"),
    // 全天
    ALL_DAY_SHIFT("全天");

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
            case 2:
                return ALL_DAY_SHIFT;
            default:
                return null;
        }
    }

    public String value() {
        return this.value;
    }

    public Integer intValue() {
        if ("早班".equals(value)) {
            return 0;
        } else if ("晚班".equals(value)) {
            return 1;
        } else return 2;
    }

    public Shift getShift(String values) {
        if ("早班".equals(values)) {
            Shift s = new Shift();
            s.setName(values);
            s.setStartTime(new Time(0, 0, 0));
            s.setEndTime(new Time(24, 0, 0));
            return s;
        } else if ("晚班".equals(values)) {
            Shift s = new Shift();
            s.setName(values);
            s.setStartTime(new Time(19, 0, 0));
            s.setEndTime(new Time(7, 0, 0));
            return s;
        } else {
            Shift s = new Shift();
            s.setName(values);
            s.setStartTime(new Time(7, 0, 0));
            s.setEndTime(new Time(19, 0, 0));
            return s;
        }
    }
}
