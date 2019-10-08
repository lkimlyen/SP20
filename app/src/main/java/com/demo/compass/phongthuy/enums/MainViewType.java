package com.demo.compass.phongthuy.enums;

/**
 * Created by admin on 4/25/17.
 */

public enum MainViewType {
    BOOTH(1),
    IMAGE_REPORT(2),
    LIB(3),  CASE_LIB(4),
    POSM_SIGN_RECEIPT(5),
    POSM_OF_OUTLET(6),
    MENU_SIGN_RECEIPT(7),
    MENU_OF_OUTLET(8);

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

