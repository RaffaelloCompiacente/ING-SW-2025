package model;

public class Promotion{
    private final String PROMO_CODE;
    private final String PROMO_DESCRIPTION;
    private final double PROMO_PERCENTAGE;//Rappresenta la percentuale di sconto applicabile al biglietto
    private final LocalDate START_PROMO;
    private final LocalDate END_PROMO;
    private final DiscountStrategy PROMO_STRATEGY;
    /*Le promozioni non sono tutte uguali alcune possono avere sconto percentuale fisso, altre sconto in euro fisso
     *altre possono essere promo condizionali ovvero valide solo per determinati tipi di treni o in determinati giorni
     della settimana etc.. etc.. Inserendo tutte queste logiche dentro promotion avremmo un mega if/switch difficile da estendere
     con strategy ogni promozione incapsula la sua logica di calcolo in una classe separata
     */


    public Promotion(String promoCode,String promoDescription,double promoPercentage,LocalDate startDate,LocalDate endDate;DiscountStrategy strategy){
        this.PROMO_CODE=promoCode;
        this.PROMO_DESCRIPTION=promoDescription;
        this.PROMO_PERCENTAGE=promoPercentage;
        this.START_PROMO=startDate;
        this.END_PROMO=endDate;
        this.PROMO_STRATEGY=strategy
    }

    public String getPROMO_CODE(){return PROMO_CODE;}
    public String getDescription(){return PROMO_DESCRIPTION;}
    public double getDiscountPercent(){return PROMO_PERCENTAGE;}
    public LocalDate getStartDate(){return START_PROMO;}
    public LocalDate getEndDate(){return END_POMO;}

    public boolean isValidToday(){
        LocalDate todau=LocalDate.now();
        return !today.isBefore(PROMO_START) && !today.isAfter(PROMO_END);
    }

    public double applyDiscount(double basePrice,Train train,Client client){
        return strategy.applyDiscount(basePrice,train,client);
    }

    public String toString(){
        return "[ "+PROMO_CODE+" ] "+description+" - Sconto: "+(PROMO_PERCENTAGE*100)+"%"+ " (Valida dal "+START_PROMO+" fino al "+END_PROMO+" )";
    }
}