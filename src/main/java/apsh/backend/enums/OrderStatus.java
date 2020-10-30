package apsh.backend.enums;

public enum OrderStatus {
    // 正在生产：订单正在生产中，且并未延期
    ON_GOING(0),
    // 准时交付：订单在交付日期前完成交付
    DELIVERED_ON_TIME(1),
    // 延期生产：订单增在生产中，且已经延期
    DELAY_PRODUCTION(2),
    // 延期交付：订单在交付日期后完成交付
    DELIVERED_DELAY(3);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public static OrderStatus valueOf(int value) {
        switch (value) {
            case 0:
                return ON_GOING;
            case 1:
                return DELIVERED_ON_TIME;
            case 2:
                return DELAY_PRODUCTION;
            case 3:
                return DELIVERED_DELAY;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
