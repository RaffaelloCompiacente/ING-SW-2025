package strategy;

import model.Train;
import model.Client;

public FixedAmountDiscountStrategy implements DiscountStrategy{
    private final double DISCOUNT;

    public FixedAmountDiscountStrategy(double discount){
        this.DISCOUNT=discount;
    }

    @Override
    public double applyDiscount(double basePrice, Train train, Client client){
        return basePrice-DISCOUNT;
    }
}