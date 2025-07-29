package model;

import java.time.LocalDate;


public class Voucher{
    private final String VOUCHER_CODE;
    private final double VOUCHER_VALUE;
    private final Train.TrainType TRAIN_TYPE_VALID;//se None il voucher è valido per tutti i treni
    private final boolean EXPIRES;
    private boolean used;
    private final LocalDate EXPIRY_DATE;

    public Voucher(double price,String voucherCode,Train.TrainType trainType,boolean expires){
        this.VOUCHER_VALUE=price;
        this.TRAIN_TYPE_VALID=trainType;
        this.EXPIRES=expires
        this.EXPIRY_DATE=expires ? LocalDate.now().plusMonths(1):null; //Tutti i voucher scadono un mese dopo la loro generazione
        this.VOUCHER_CODE=voucherCode ;
        this.used=false;
    }

    public String getVoucherCode(){return VOUCHER_CODE;}
    public double getValue(){return VOUCHER_VALUE;}
    public Train.TrainType getTrainTypeValid(){return TRAIN_TYPE_VALID;}
    public LocalDate getExpiryDate(){return EXPIRY_DATE;}
    public boolean isUsed() {return used;}
    public boolean hasExpiry(){return EXPIRES;}

    public boolean isValid(){
        return !used && (!expires || (expiryDate != null && LocalDate.now().isAfter(LocalDate.MIN) && LocalDate.now().isBefore(EXPIRY_DATE)));
    }

    public boolean isApplicableTo(Ticket ticket){
        Train.TrainType ticketTrainType=ticket.getTrain().getType();
        return TRAIN_TYPE_VALID==null || TRAIN_TYPE_VALID==ticketTrainType;
    }

    public void markAsUsed(){
        this.used=true;
    }

    @Override
    public String toString(){
        return "[Voucher " + VOUCHER_CODE + "] Valore: " + VOUCHER_VALUE + "€, valido per: " +
                (TRAIN_TYPE_VALID != null ? TRAIN_TYPE_VALID.name() : "tutti i treni") +
                " - Scade il: " + EXPIRY_DATE;
    }
}

