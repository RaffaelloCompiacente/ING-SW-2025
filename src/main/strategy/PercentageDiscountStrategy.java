package strategy;


public class PercentageDiscountStrategy implements DiscountStrategy{
    @Override
    public double applyDiscount(double basePrice,double benefit){
        return basePrice*(1-benefit);
    }


}