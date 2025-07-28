package strategy;

import model.Customer;
import model.Promotion;
import model.Train;

public class VoucherGenerationStrategy implements DiscountStrategy{
    private final double percentage;

    public VoucherGenerationStrategy(double percentage){
        this.percentage=percentage;
    }

    @Override
    public double applyDiscount(double basePrice,Train train,Client client){
        return basePrice*percentage;
    }
}