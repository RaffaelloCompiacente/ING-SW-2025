package src.model;

import java.time.LocalDateTime;

public class Train {
    private final String TRAIN_ID;
    private final String DEPARTURE;
    private final String ARRIVAL;
    private final LocalDateTime DEPARTURE_TIME;
    private final LocalDateTime ARRIVAL_TIME;
    private final boolean RESERVABLE; //Distingue treni in cui l'acquisto garantisce il posto a sedere da quelli che no
    private int availableSeats;
    private TrainStatus status;
    private TrainType trainType;

    public enum TrainStatus{
        ON_TIME,DELAYED,CANCELLED
    }

    public enum TrainType{
        FRECCIAROSSA,
        FRECCIARGENTO,
        FRECCIABIANCA,
        ITALO,
        INTERCITY,
        EUROCITY,
        REGIONALE,
        REGIONALE_VELOCE
    }

    public Train (String trainId,String departure,String arrival,LocalDateTime departureTime,LocalDateTime arrivalTime,TrainType type,int seats){
        this.TRAIN_ID=trainId;
        this.DEPARTURE=departure;
        this.ARRIVAL=arrival;
        this.DEPARTURE_TIME=departureTime;
        this.ARRIVAL_TIME=arrivalTime;
        this.trainType=type;
        this.RESERVABLE=!isRegional(type);
        this.availableSeats=this.RESERVABLE ? seats:-1; //Se il treno è prenotabile come posti disponibili avremo quelli passati nel costruttore altrimenti è un informazione inutile e passiamo -1
        this.status=TrainStatus.ON_TIME;
    }

    private boolean isRegional(TrainType type){
        return type == TrainType.REGIONALE || type == TrainType.REGIONALE_VELOCE;
    }

    public boolean isReservable(){return RESERVABLE;}
    //Metodi getter
    public String getTrainID(){return TRAIN_ID;}
    public String getDeparture(){return DEPARTURE;}
    public String getArrival(){return ARRIVAL;}
    public LocalDateTime getDepartureTime(){return DEPARTURE_TIME;}
    public LocalDateTime getArrivalTime(){return ARRIVAL_TIME;}
    public int getAvailableSeats(){return availableSeats;}
    public TrainStatus getStatus(){return status;}
    public TrainType getType(){return trainType;}

    public boolean bookSeat(){
        if(!RESERVABLE){
            return true;
        }
        if(availableSeats>0){
            availableSeats--;
            return true;
        }
        return false;
    }

    public void updateStatus(TrainStatus newStatus){
        this.status=newStatus;
    }

    @Override
    public String toString(){
        return TRAIN_ID+" [ "+trainType+" ] "+" | "+DEPARTURE+" | "+ARRIVAL+" | "+DEPARTURE_TIME+" | Posti Disponibili: "+availableSeats+" | Stato Treno: "+status;
    }


}