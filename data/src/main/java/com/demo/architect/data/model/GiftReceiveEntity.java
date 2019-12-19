package com.demo.architect.data.model;

public class GiftReceiveEntity {
    private int ID;
    private int QuantitySuccess;
    private int QuantityFail;

    public GiftReceiveEntity(int ID, int quantitySuccess, int quantityFail) {
        this.ID = ID;
        QuantitySuccess = quantitySuccess;
        QuantityFail = quantityFail;
    }

    public int getID() {
        return ID;
    }

    public int getQuantitySuccess() {
        return QuantitySuccess;
    }

    public int getQuantityFail() {
        return QuantityFail;
    }
}
