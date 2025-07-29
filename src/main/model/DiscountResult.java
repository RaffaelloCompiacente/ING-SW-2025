package model;

import service.PromoResultVisitor;

public class DiscountResult implements PromoResult{
    private final double DISCOUNTED_PRICE;
    private final Promotion ORIGIN_PROMO;

    public DiscountedResult(double newPrice,String promo){
        this.DISCOUNTED_PRICE=newPrice;
        this.ORIGIN_PROMO=promo;
    }
    /*Prende in input il valore generato dalla strategy
    * */
    public double getDiscountedPrice(){return this.DISCOUNTED_PRICE;}
    public Promotion getOriginPromo(){return this.ORIGIN_PROMO;}

    @Override
    public void accept(PromoResultVisitor visitor){
        visitor.visit(this);
    }
}