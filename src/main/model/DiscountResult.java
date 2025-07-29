package model;

import service.PromoResultVisitor;

public class DiscountResult implements PromoResult{
    private final double DISCOUNTED_PRICE;
    private final String ORIGIN_PROMO_CODE;

    public DiscountedResult(double newPrice,String promoCode){
        this.DISCOUNTED_PRICE=newPrice;
        this.ORIGIN_PROMO_CODE=promoCode;
    }
    /*Prende in input il valore generato dalla strategy
    * */
    public double getDiscountedPrice(){return this.DISCOUNTED_PRICE;}
    public String getOriginPromoCode(){return this.ORIGIN_PROMO_CODE;}

    @Override
    public void accept(PromoResultVisitor visitor){
        visitor.visit(this);
    }
}