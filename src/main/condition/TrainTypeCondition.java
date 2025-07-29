package condition;

import model.Client;
import model.Train;
import model.Train.TrainType;

public TrainTypeCondition implements PromoCondition{
    private final Train.TrainType VALID_TYPE;

    public TrainTypeCondition(Train.TrainType validType){
        this.VALID_TYPE=validType;
    }

    @Override
    public boolean isApplicable(Train.TrainType trainType,Client client){
        return trainType==VALID_TYPE;
    }
}