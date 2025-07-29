package strategy;


public FixedAmountDiscountStrategy implements DiscountStrategy{

    @Override
    public double applyDiscount(double basePrice,double benefit){
        return basePrice-benefit;
    }
}