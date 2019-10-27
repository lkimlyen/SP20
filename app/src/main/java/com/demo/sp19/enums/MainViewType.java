package com.demo.sp19.enums;

/**
 * Created by admin on 4/25/17.
 */

public enum MainViewType {
    ATTENDANCE(1),
    BILL(2);

    private int type;

    MainViewType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static MainViewType getTypeFromInt(int type) {
        for (MainViewType mainViewType : MainViewType.values()) {
            if (mainViewType.getType() == type) {
                return mainViewType;
            }
        }
        return null;
    }

}

