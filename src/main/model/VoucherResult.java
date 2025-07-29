package model;

import service.PromoResultVistor;

public class VoucherResult implements PromoResult{
    private final double VOUCHER_VALUE;
    private final Promotion ORIGIN_PROMO;

    public VoucherResult(double calulatedValue,Promotion promo){
        this.VOUCHER_VALUE=calculatedValue;
        this.ORIGIN_PROMO=promo;
    }

    public double getVoucherValue(){return this.VOUCHER_VALUE;}
    public Promotion getOriginPromo(){return this.ORIGIN_PROMO;}

    @Override
    public void accept(PromoResultVisitor visitor){
        visitor.visit(this);
    }
}