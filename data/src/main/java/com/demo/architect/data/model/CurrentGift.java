package com.demo.architect.data.model;

public class CurrentGift {
    private int giftID;
    private int numberTotal;
    private int numberUsed;

    public CurrentGift(int giftID, int numberTotal, int numberUsed) {
        this.giftID = giftID;
        this.numberTotal = numberTotal;
        this.numberUsed = numberUsed;
    }

    public int getGiftID() {
        return giftID;
    }

    public int getNumberTotal() {
        return numberTotal;
    }

    public int getNumberUsed() {
        return numberUsed;
    }

    public void setNumberUsed(int numberUsed) {
        this.numberUsed = numberUsed;
    }

}
