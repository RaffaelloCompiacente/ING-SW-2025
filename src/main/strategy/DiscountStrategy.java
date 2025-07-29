package strategy;

import model.Train;
import model.Client;

public interface DiscountStrategy{
    double applyDiscount(double basePrice,double benefit);
}