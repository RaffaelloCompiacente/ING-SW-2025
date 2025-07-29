package condition;

import model.Train;
import model.Train.TrainType
import model.Client;

public WeekenCondition implements PromoCondition{

    @Override
    public boolean isApplicable(Train.TrainType trainType,Client client){
        DayOfWeek giorno=train.getDepartureTime().getDayOfWeek();
        return (giorno==DayOfWeek.SATURDAY || giorno==DayOfWeek.SUNDAY);
    }
}