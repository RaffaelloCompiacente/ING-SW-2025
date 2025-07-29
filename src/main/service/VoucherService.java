package service;

import model.*;
import repository.*;
import java.time.LocalDate;
import java.util.*;

public class VoucherService{
    private final VoucherRepository VOUCHER_REPOSITORY;
    private final PromotionRepository PROMOTION_REPOSITORY;
    private final CustomerRepository CUSTOMER_REPOSITORY;


    public VoucherService(VoucherRepository voucherRepository,PromotionRepository promotionRepository,CustomerRepository customerRepository){
        this.VOUCHER_REPOSITORY=voucherRepository;
        this.PROMOTION_REPOSITORY=promotionRepository;
        this.CUSTOMER_REPOSITORY= customerRepository;
    }

    public List<Voucher> getApplicableVoucher(Client client,Ticket ticket){
        List<Voucher> all= voucherRepository.findValidByCustomer(customer);
        List<Voucher> applicable= new ArrayList<>();
        for(Voucher v: all){
            if(v.isApplicableTo(ticket)){
                applicable.add(v);
            }
        }
        return applicable;
    }

    public Ticket applyVoucher(Client client,Ticket ticket){
        Optional<Voucher> maybeVoucher= voucherRepository.findByCode(voucherCode);
        if(maybeVoucher.isPresent()){
            Voucher voucher=maybeVoucher.get();
            if(v.isValid() && v.isApplicableTo(ticket)) {
                double newPrice=Math.max(0,ticket.getPrice()-voucher.getValue());
                Ticket updateTicket=new Ticket(ticket.getTrain(),newPrice,ticket.getTicketID);
                voucher.markAsUsed();
                voucherRepository.markAsUsed(voucher.getVoucherCode());
                return updateTicket;
            }
        }
        return ticket;
    }

    public List<Voucher> getValidVouchers(Client client){
        return voucherRepository.findValidByCustomer(client);
    }

    public List<Voucher> getAllVouchers(Client client){
        return voucherRepository.findAllByCustomer(client);
    }

    public void markVoucherAsUsed(String voucherCode){
        voucherRepository.markAsUsed(voucherCode);
    }

    public void generateVoucherAfterPurchase(Client client,Promotion promo,Ticket ticket) {
        double value=promo.getStrategy().applyDiscount(ticket.getPrice(),promo.getBenefit());
        String code=generateVoucherCodes(promo.getCode(),promo.getBenefit(),promo.getValidTrainType());
        Voucher voucher = new Voucher(value,code,promo.getValidTrainType(),true);
        voucherRepository.save(voucher);
    }

    /**
     * Emette un voucher senza scadenza (es. per treni annullati)
     */
    public void issueCompensationVoucher(Client client, double value) {
        Voucher voucher = new Voucher(value, "COMP", null, false); // null = valido per tutti i treni
        voucherRepository.save(voucher);
    }

    private String generateVoucherCodes(String promoCode,double percentage,Train.TrainType type){
        int scount=(int) (percentage*100);
        String scountStr=String.valueOf(scount);
        String trainTypeStr=(type != null)? type.name():"ALL";
        return promoCode+scountStr+trainTypeStr;
    }
}

}