package model;

import java.time.LocalDateTime;

public class Ticket{
    private final String TICKET_ID;//per la generazione dei ticket è meglio in mia opinione utilizzare un codice alfa numerico
    private final double price;
    private TicketStatus status;
    private final Train TICKET_TRAIN;

    public enum TicketStatus{
        PRENOTATO, //Prenotazione in attesa di pagamento
        ANNULLATO, //Biglietto prenotato ma senza finalizzazione del pagamento
        CONFERMATO //Prenotazion e finalizzato pagamento
    }

    public Ticket(Train train, double p, String ticketId ){
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
        this.price=p;

    }

    //Metodi getter
    public LocalDateTime getOrarioBiglietto(){return TICKET_TRAIN.getDepartureTime();}
    public double getPrice(){return price;}
    public String getTicketId(){return TICKET_ID;}
    public Train getTrain(){return TICKET_TRAIN;}
    public TicketStatus getStatus(){return status;}
    public void annulla(){this.status=TicketStatus.ANNULLATO;}
    public void conferma(){this.status=TicketStaus.CONFERMATO;}

    @Override
    public String toString(){
        return "Ticket[" + ticketId + "] per il treno " + ticketTrain.getTrainId() +
                " - Prezzo: " + price + " - Stato: " + status;
    }
}