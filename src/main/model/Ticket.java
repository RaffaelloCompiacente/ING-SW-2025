package src.model;

import java.time.LocalDateTime;

public class Ticket{
    private final String TICKET_ID;//per la generazione dei ticket è meglio in mia opinione utilizzare un codice alfa numerico
    private final double PRICE;
    private TicketStatus status;
    private final Train TICKET_TRAIN;
    private LocalDateTime RESERVED_AT_TIME;

    public enum TicketStatus{
        PRENOTATO, //Prenotazione in attesa di pagamento
        ANNULLATO, //Biglietto prenotato ma senza finalizzazione del pagamento
        CONFERMATO //Prenotazion e finalizzato pagamento
    }

    //Costruttore privato -> si utilizza solo nei factory
    private Ticket(Train train, double p, String ticketId ){
        this.TICKET_TRAIN=train;
        this.TICKET_ID=ticketId;// Verrà generato lato server con una forma del tipo GGMMAATTNNNNXYZ
        /*Dove GG è il giorno di acquisto o meglio di finalizzazione del pagamento
          MM è il mese
          AA è l'anno
          TT è il tipo di treno
          NNNN un numero progressivo corrispondente a un contatore progressivo che va da 0001 a 9999, ogni tipo di treno ha un contatore separato
          XYZ sono tre combinazioni delle lettere dell'alfabeto per le turnazioni da AAA a ZZZ prima si incrementa l'ultima lettera una volta che
          l'ultima lettera diventa Z quindi da AAA->AAZ la prima letter aviene resettata e si incrementa la seconda ABA e coì finchè non si arriva a ZZZ.
        * */
        this.PRICE=p;

    }

    //Factory method per acquisto diretto
    public static Ticket createConfirmed(Train train,double price,String ticketID){
        Ticket t= new Ticket(train, price,ticketID);
        t.status=TicketStatus.CONFERMATO;
        return t;
    }

    //Factory method per la prenotazione
    public static Ticket createReservation(Train train,double price,String ticketID, LocalDateTime reservetion){
        if(!train.isReservable()){
            throw new IllegalArgumentException("Non è possibile prenotare un treno regionale");
        }
        Ticker t= new Ticker(train,price,ticketID);
        t.status=TicketStatus.PRENOTATO;
        t.reservedAt=reservedAt;
        return t;
    }

    //Metodi getter
    public LocalDateTime getOrarioBiglietto(){return TICKET_TRAIN.getDepartureTime();}
    public double getPrice(){return PRICE;}
    public String getTicketId(){return TICKET_ID;}
    public Train getTrain(){return TICKET_TRAIN;}
    public TicketStatus getStatus(){return status;}
    public void annulla(){this.status=TicketStatus.ANNULLATO;}
    public void conferma(){this.status=TicketStaus.CONFERMATO;}
    public LocalDateTime getReservedAt() {return RESERVED_AT_TIME;}


    public void markAsPaid(double amountPaid) {
        this.status = TicketStatus.CONFERMATO;
        // eventualmente puoi registrare anche l'importo finale, se diverso da quello iniziale
    }

    public void annulla(){
        this.status=TicketStatus.ANNULLATO;
    }

    @Override
    public String toString(){
        return "Ticket[" + TICKET_ID + "] per il treno " + TICKET_TRAIN.getTrainId() +
                " - Prezzo: " + PRICE + " - Stato: " + status;
    }
}