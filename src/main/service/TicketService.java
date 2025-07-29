package service;

import model.Ticket,
import model.Train;
import model.Voucher;
import model.Promotion;
import model.Customer;
import repository.TicketRepository;
import repository.CustomerRepository;
import repository.CounterRepository;
import strategy.VoucherGenerationStrategy;

import java.time.*;
import java.io.*;
import java.util.*;

public class TicketService{
    private final PromotionService PROMOTION_SERVICE;
    private final VoucherService VOUCHER_SERVICE;
    private final TicketRepository TICKET_REPO;
    private final CustomerRepository CUSTOMER_REPO;
    private final CounterRepository COUNTER_REPO;

    private final Map<Train.TrainType,Integer> counters= new HashMap<>();
    private final Map<Train.TrainType,String> letterSuffixes= new HashMap<>();
    private LocalDate lastCounterResetDate=localDate.now();

    public TicketService(PromotionService promotionService,VoucherService voucherService,TicketRespository ticketRepo,CustomerRepository customerRepo,CounterRepository counterRepo){
        this.PROMOTION_SERVICE=promotionService;
        this.VOUCHER_SERVICE=voucherService;
        this.TICKET_REPO=ticketRepo;
        this.CUSTOMER_REPO=customerRepo;
        this.COUNTER_REPO=counterRepo;
        this.counters =COUNTER_REPO.loadCounters();
        this.letterSuffixes=COUNTER_REPO.loadSuffixes();
        this.lastCounterResetDate=COUNTER_REPO.loadLastResetDate();
    }

    private void persistCounters() {COUNTER_REPO.save(counters,letterSuffixes,lastCounterResetDate);}


    private synchronized String generateTicketID(Train train){
        LocalDateTime now=LocalDateTime.now();
        if(!lastCounterResetDate.equals(LocalDate.now())){
            counters.clear();
            letterSuffixes.clear();
            lastCounterResetDate=LocalDate.now();
        }
        String datePart=String.format("%02d%02d%02d",now.getMonthValue(),now.getDayOfMonth(),now.getYear()%100);
        String typeCode=getTrainTypeCode(train.getType());
        int count=counters.getOrDefault(train.getType(),0)+1;
        if(count>999) {
            count = 1;
            updateLetterSuffic(train.getType());
        }
        counters.put(train.getType(),count);
        String counterPart=String.format("%03d",count);
        String suffix=letterSuffixes.getOrDefault(train.getType(),"AAA");
        saveCountersToFile();
        return datePart+typeCode+counterPart+suffix;
    }

    private String getTrainTypeCode(Train.TrainType type){
        return switch(type) {
            case FRECCIAROSSA -> "FR";
            case FRECCIARGENTO -> "FA";
            case FRECCIABIANCA -> "FB";
            case ITALO -> "IT";
            case INTERCITY -> "IC";
            case EUROCITY -> "EC";
            case REGIONALE -> "RE";
            case REGIONALE_VELOCE -> "RV";
        };
    }

    private void updateLetterSuffix(Train.TrainType type) {
        String current= letterSuffixes.getOrDefault(type,"AAA"); //Prende il suffisso di lettere corrispondente al tipo di treno e se non c'è parte da AAA
        char[] chars=current.toCharArray();
        for(int i=2;i>=0;i--) {
            if(char[i]=="Z"){
                char[i]=="A";
            }else{
                char[i]++;
                break;
            }
        }
        letterSuffixes.put(type,new String(chars));
    }
    /*Aggiornamento circolare dei suffissi si parte dalla lettera più a destra
    AAA e la si incrementa -> AAB -> AAC etc etc fino ad AAZ quando questa raggiunge Z si
    resetta ad A e si passa alla lettera alla sua sinistra nel ciclo che viene incrementata
    quindi avremo ABA il ciclo dopo l'incremento viene interrorro e quindi si torna alla lettera
    più a destra e otteniamo ABB ->fino a ABZ che porta di nuovo all'incremento della centrale ACA
    fino a ZZZ con la stessa logica
    * */


    public double calculateDiscountedPriceWhitPromotions(Train train,double basePrice){
        return PROMOTION_SERVICE.calculateDiscountedPrice(train,basePrice);
    }

    public List<Promotion> getActivePromotionsForTrain(Train train){
        return promotionService.findApplicablePromotions(train);
    }

    public List<Voucher> getValidVoucherForCustomer(String customerId,Train train){
        return voucherService.findAllValidForCustomer(customerID,train);
    }

    public double calculatePriceWithVoucher(Train train,double currentPrice,Voucher selectedVoucher){
        if(selectedVoucher!=null && selectedVoucher.isValidFor(train)){
            return voucherService.applyVoucher(selectedVoucher,currentPrice)
        }
        return currentPrice;
    }


    public void validateTrainAvailability(Train train){
        LocalDateTime now=LocalDateTime.now();
        if(train.getDepartureTime().isBefore(now)){
            throw new IllegalStateException("Il treno è già partito");
        }
        if(train.getStatus()==Train.TrainStatus.CANCELED){
            throw new IllegalStateException("Il treno è stato cancellato");
        }
        if(Duration.between(now,train.getDepartureTime()).toMinutes()<10){
            throw new IllegalStateException("Non è possibile acquistare il biglietto meno di 10 minuti prima della partenza");
        }
    }


    public Ticket purchaseTicket(String customerID,Train train,double basePrice,Voucher chosenVoucher){
        validateTrainAvailability(train);
        if(train.isReservable() && train.getAvailableSeats()<=0){
            throw new IllegalStateException("Posti esauriti per il treno "+train.getTrainID());
        }
        double finalPrice=promotionService.calculatedDiscountedPrice(train,basePrice);
        if(chosenVoucher != null){
            if(!chosenVoucher.isValidFor(train)){
                throw new IllegalArgumentException("Il voucher selezionato non è valido per il tipo di treno");
            }
            /*Controllo ridondante perchè la scelta del Voucher avviene già su una lista filtrata per cliente e treno
             *  Manteniamo comunque come controllo di sicurezza per manomissioni e nel caso di cambi di struttura
             *  */
            finalPrice=voucherService.applyVoucher(chosenVoucher,finalPrice);
            voucherService.invalidateVoucher(customerID,chosenVoucher);
        }
        boolean booked =train.bookSeat();
        if(!booked){
            throw new IllegalStateException("Impossibile prenotare il biglietto");
        }
        String ticketID=generateTicketID();
        //Utilizzo del factory method per la creazione di un biglietto già valido.
        Ticket ticket = Ticket.createConfirmed(train,finalPrice,ticketID)
        ticketRepo.save(ticket);
        List<Promotion> appliedPromos= promotionService.findApplicablePromotions(train);
        for(Promotion promo: appliedPromos){
            if(promo.getStrategy() instanceof VoucherGenerationStrategy strat){
                Voucher newVoucher=strat.generateVoucher(train, basePrice);
                voucherService.assignVoucherToCustomer(customerID,newVoucher);
            }
        }//Se fra le promozioni applicabili c'è una basata su voucher alla finalizzazione del pagamento il voucher verrà creato
        return ticket;
    }

    public Ticket reserveTicket(String customerID,Train train,double basePrice){
        validateTrainAvailability(train);
        LocalDateTime now=LocalDateTime.now();
        long minutesUntilDeparture=Duration.between(now,train.getDepartureTime()).toMinutes();
        if(minutesUntilDeparture<(3*24*60)){
            throw new IllegalArgumentException("Impossibile effettuare la prenotazione, i biglietti possono essere prenotati fino a 3 giorni prima della partenza");
        }
        if(train.isReservable() && train.getAvailableSeats()<=0){
            throw new IllegalStateException("Posti esauriti per il treno.");
        }
        double finalPrice= promotionService.calculateDiscountedPrice(train,basePrice);
        boolean booked=trainbookSeat();
        if(!booked){
            throw new IllegalStateException("Impossibile prenotare il posto");
        }
        String ticketID=generateTicketID();
        //Utilizzo del factory method per la creazione di un biglietto prenotazione
        Ticket ticket= Ticket.createReservation(train,finalPrice,ticketID,now)
        ticketRepo.save(ticket);
        return ticket;
    }

    public Ticket finalizeReservedTicket(String reservationID,String customerID,Voucher voucher){
        Ticket ticket= ticketRepo.findReservationById(reserevationId);
        if(ticket==null || ticket.isPaid()){
            throw new IllegalArgumentException("Prenotazione non valida o già pagata");
        }
        LocalDateTime now= LocalDateTime.now();
        if(Duration.beetween(ticket.getReservedAt(),now).toHours()>48){
            ticketRepo.delete(ticket);
            ticket.getTrain().freeSeat();
            throw new IllegalStateException("Il periodo di prenotazione è scaduto a causa del mancato pagamento nei tempi previsti");
        }
        validateTrainAvailability(ticket.getTrain());

        double finalPrice = ticket.getPrice();
        if (voucher != null && voucher.isValidFor(ticket.getTrain())) {
            finalPrice = voucherService.applyVoucher(voucher, finalPrice);
            voucherService.invalidateVoucher(customerID, voucher);
        }

        ticket.markAsPaid(finalPrice);
        ticketRepo.update(ticket);

        List<Promotion> appliedPromos = promotionService.findApplicablePromotions(ticket.getTrain());
        for (Promotion promo : appliedPromos) {
            if (promo.getStrategy() instanceof VoucherGenerationStrategy strat) {
                Voucher newVoucher = strat.generateVoucher(ticket.getTrain(), finalPrice);
                voucherService.assignVoucherToCustomer(customerID, newVoucher);
            }
        }

        return ticket;
    }

    public List<Ticket> getCustomerTickets(String customerID) {
        return ticketRepo.findByCustomer(customerID);
    }

    }



