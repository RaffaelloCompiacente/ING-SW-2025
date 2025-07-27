public strategy;

import model.Train;
import model.Client;
import model.Voucher

public VoucherGenerationStrategy implements DiscountStrategy{
    private final VoucherService VOUCHER_SERVICE;
    private final double PERCENTAGE_VOUCHER;

    public VoucherGenerationStrategy(VoucherService voucherService, double percentageVoucher){
        this.VOUCHER_SERVICE=voucherService;
        this.PERCENTAGE_VOUCHER=percentageVoucher;
    }

    @Override
    public double applyDiscount(double basePrice,Train train, Client client,String promoCode){
        Voucher buonoSconto= new Voucher(basePrice,PERCENTAGE_VOUCHER, train,promoCode);
        VOUCHER_SERVICE.add(cliente,buonoSconto);
        return basePrice;
    }
}