package src.model;

import java.time.LocalDate;


public class Voucher{
    private final String VOUCHER_CODE;
    private final double VOUCHER_VALUE;
    private final Train.TrainType TRAIN_TYPE_VALID;//se None il voucher è valido per tutti i treni
    private final LocalDate EXPIRY_DATE;

    public Voucher(double price,double percentage,Train train,String promoCode){
        this.VOUCHER_VALUE=price*percentage;
        this.TRAIN_TYPE_VALID=train.getType();
        this.EXPIRY_DATE=LocalDate.now().plusMonths(1); //Tutti i voucher scadono un mese dopo la loro generazione
        this.VOUCHER_CODE= generateVoucherCodes(promoCode,percentage);
    }

    public String generateVoucherCodes(String promoCode,double percentage){
        int scount=(int) (percentage*100);
        String scountStr=String.valueOf(scount);
        String trainTypeStr=(TRAIN_TYPE_VALID != null)? TRAIN_TYPE_VALID.name():"ALL";
        String codice=promoCode.concat(scountStr).concat(trainTypeStr);
        return codice;
    }

    public String getVoucherCode(){return VOUCHER_CODE;}
    public double getValue(){return VOUCHER_VALUE;}
    public Train.TrainType getTrainTypeValid(){return TRAIN_TYPE_VALID;}
    public LocalDate getExpiryDate(){return EXPIRY_DATE;}

    public boolean isValidFor(Train train){
        LocalDate today=LocalDate.now();
        boolean notExpired=!today.isAfter(EXPIRY_DATE);
        boolean trainMatches=(TRAIN_TYPE_VALID==null || train.getType()==TRAIN_TYPE_VALID);
        return notExpired && trainMatches;
    }

    @Override
    public String toString(){
        return "[Voucher " + VOUCHER_CODE + "] Valore: " + VOUCHER_VALUE + "€, valido per: " +
                (TRAIN_TYPE_VALID != null ? TRAIN_TYPE_VALID.name() : "tutti i treni") +
                " - Scade il: " + EXPIRY_DATE;
    }
}

