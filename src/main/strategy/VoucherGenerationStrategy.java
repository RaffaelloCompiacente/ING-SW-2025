package strategy;


public class VoucherGenerationStrategy implements DiscountStrategy{

    @Override
    public double applyDiscount(double basePrice,double benefit){
        return basePrice*benefit;
    }
}