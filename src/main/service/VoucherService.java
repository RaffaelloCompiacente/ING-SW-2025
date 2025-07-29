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
        List<Voucher> all= VOUCHER_REPOSITORY.findValidByCustomer(client);
        List<Voucher> applicable= new ArrayList<>();
        for(Voucher v: all){
            if(v.isApplicableTo(ticket)){
                applicable.add(v);
            }
        }
        return applicable;
    }

    public Ticket applyVoucher(Client client,Ticket ticket){
        /*Da rivedere se diamo la scelta all'utente su quale voucher utilizzare dobbiamo passare come parametro il codice di un voucher
        che l'utente avrà scelto da una lista restituita da getApplicableVoucher e utilizzare quello quindi anche in findByCode immediatemente
        di seguito dobbiamo cambiare il parametro
        * */
        Optional<Voucher> maybeVoucher= VOUCHER_REPOSITORY.findByCode(client);
        if(maybeVoucher.isPresent()){
            Voucher voucher=maybeVoucher.get();
            if(v.isValid() && v.isApplicableTo(ticket)) {
                double newPrice=Math.max(0,ticket.getPrice()-voucher.getValue());
                Ticket updateTicket=new Ticket(ticket.getTrain(),newPrice,ticket.getTicketID());
                VOUCHER_REPOSITORY.markAsUsed(voucher.getVoucherCode(),client);
                return updateTicket;
            }
        }
        return ticket;
    }

    public List<Voucher> getValidVouchers(Client client){
        return VOUCHER_REPOSITORY.findValidByCustomer(client);
    }
    /*
    public List<Voucher> getAllVouchers(Client client){
        return voucherRepository.findAllByCustomer(client);
    }
    Metodo rindondandte un cliente a prescindere non dovrebbe visualizzare voucher che non sono più validi
    */

    public void markVoucherAsUsed(String voucherCode,Client client){
        VOUCHER_REPOSITORY.markAsUsed(voucherCode,client);
    }

    public void generateVoucherAfterPurchase(Client client,Promotion promo,double value) {
        String code=generateVoucherCodes(promo.getCode(),promo.getBenefit(),promo.getValidTrainType());
        Voucher voucher = new Voucher(value,code,promo.getValidTrainType(),true);
        VOUCHER_REPOSITORY.save(voucher,client);//forse bisogna aggiungere anche il client come parametro?
    }

    /**
     * Emette un voucher senza scadenza (es. per treni annullati)
     */
    public void issueCompensationVoucher(Client client, double value) {
        Voucher voucher = new Voucher(value, "COMP", null, false); // null = valido per tutti i treni
        VOUCHER_REPOSITORY.save(voucher);
    }

    private String generateVoucherCodes(String promoCode,double percentage,Train.TrainType type){
        int scount=(int) (percentage*100);
        String scountStr=String.valueOf(scount);
        String trainTypeStr=(type != null)? type.name():"ALL";
        return promoCode+scountStr+trainTypeStr;
    }
}

}