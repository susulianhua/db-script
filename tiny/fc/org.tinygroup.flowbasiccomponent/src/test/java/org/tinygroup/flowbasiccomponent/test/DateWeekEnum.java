package org.tinygroup.flowbasiccomponent.test;


public enum DateWeekEnum {
    MONTH(1), WEEK(2), DAY(3);

    private int day;

    DateWeekEnum(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public DateWeekEnum getValue(int day) {
        for (DateWeekEnum c : DateWeekEnum.values()) {
            if (c.day == day) {
                return c;
            }
        }
        return null;
    }
}
