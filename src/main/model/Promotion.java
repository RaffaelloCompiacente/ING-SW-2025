package model;

import java.time.LocalDate;

public class Promotion{
    private final String PROMO_CODE;
    private final String PROMO_DESCRIPTION;
    private final Train.TrainType TRAIN_TYPE;
    private final double PROMO_BENEFIT;//Rappresenta la percentuale di sconto applicabile al biglietto
    private final LocalDate START_PROMO;
    private final LocalDate END_PROMO;
    private final PromoCondition PROMO_CONDITION;
    private final DiscountStrategy PROMO_STRATEGY;
    /*Le promozioni non sono tutte uguali alcune possono avere sconto percentuale fisso, altre sconto in euro fisso
     *altre possono essere promo condizionali ovvero valide solo per determinati tipi di treni o in determinati giorni
     della settimana etc.. etc.. Inserendo tutte queste logiche dentro promotion avremmo un mega if/switch difficile da estendere
     con strategy ogni promozione incapsula la sua logica di calcolo in una classe separata
     */


    public Promotion(String promoCode,String promoDescription,double promoBenefit,LocalDate startDate,LocalDate endDate;DiscountStrategy strategy,PromoCondition condition,Train.TrainType trainType){
        this.PROMO_CODE=new String(promoCode);
        this.PROMO_DESCRIPTION=new String(promoDescription);
        this.PROMO_BENEFIT=promoBenefit;
        this.START_PROMO=startDate;
        this.END_PROMO=endDate;
        this.PROMO_CONDITION=condition;
        this.PROMO_STRATEGY=strategy;
        this.TRAIN_TYPE=trainType;
    }

    public String getPromoCode(){return PROMO_CODE;}
    public String getDescription(){return PROMO_DESCRIPTION;}
    public LocalDate getStartDate(){return START_PROMO;}
    public LocalDate getEndDate(){return END_PROMO;}
    public double getPromoBenefit(){return PROMO_BENEFIT;}
    public Train.TrainType getValidTrainType(){return TRAIN_TYPE;}

    public boolean isValidToday(){
        LocalDate today=LocalDate.now();
        return !today.isBefore(START_PROMO) && !today.isAfter(END_PROMO);
    }

    public double applyPromotion(double basePrice,Train.TrainType trainType,Client client){
        if(this.isValidToday() && this.PROMO_CONDITION.isApplicable(trainType,client)){
            return PROMO_STRATEGY.applyDiscount(basePrice,this.getPromoBenefit());
        }
        return basePrice;
    }

    public String toString(){
        return "[ "+PROMO_CODE+" ] "+PROMO_DESCRIPTION+" (Valida dal "+START_PROMO+" fino al "+END_PROMO+" )";
    }
}