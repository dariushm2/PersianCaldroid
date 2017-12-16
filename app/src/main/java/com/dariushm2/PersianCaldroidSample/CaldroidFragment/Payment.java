package com.dariushm2.PersianCaldroidSample.CaldroidFragment;


import java.io.Serializable;

/**
 * Created by DARIUSH on 7/26/2015.
 */

public class Payment implements Serializable {
    private char type;
    private String receiver;
    private int paymentNumber;
    private long amount;

    public Payment(){}

    public Payment(char type, String receiver, int paymentNumber, long amount) {
        this.type = type;
        this.receiver = receiver;
        this.paymentNumber = paymentNumber;
        this.amount = amount;

    }



    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(int paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

}
