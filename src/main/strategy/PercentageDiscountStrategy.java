package strategy;

import model.Train;
import model.Client;

public class PercentageDiscountStrategy implements DiscountStrategy{
    private final double DISCOUNT_PERCENTAGE;

    public PercentageDiscountStrategy(double discountPercentage){
        this.DISCOUNT_PERCENTAGE=discountPercentage;
    }

    @Override public double applyDiscount(double basePrice,Traim train, Client client){
        return basePrice*(1-DISCOUNT_PERCENTAGE);
    }


}