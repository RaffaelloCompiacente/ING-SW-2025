package model;

import service.PromoResultVistor;

public class VoucherResult implements PromoResult{
    private final double VOUCHER_VALUE;
    private final String ORIGIN_PROMO_CODE;

    public VoucherResult(double calulatedValue,String promoCode){
        this.VOUCHER_VALUE=calculatedValue;
        this.ORIGIN_PROMO_CODE=promoCode;
    }

    public double getVoucherValue(){return this.VOUCHER_VALUE;}
    public String getOriginPromoCode(){return this.ORIGIN_PROMO_CODE;}

    @Override
    public void accept(PromoResultVisitor visitor){
        visitor.visit(this);
    }
}