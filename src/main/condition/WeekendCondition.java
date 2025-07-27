package condition;

import model.Train;
import model.Client;

public WeekenCondition implements PromoCondition{

    @Override
    public boolean isApplicable(Train train,Client client){
        DayOfWeek giorno=train.getDepartureTime().getDayOfWeek();
        return (giorno==DayOfWeek.SATURDAY || giorno==DayOfWeek.SUNDAY);
    }
}