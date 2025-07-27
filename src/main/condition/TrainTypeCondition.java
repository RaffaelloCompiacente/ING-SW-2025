package condition;

import model.Client;
import model.Train;
import model.Train.TrainType;

public TrainTypeCondition implements PromoCondition{
    private final TrainType VALID_TYPE;

    public TrainTypeCondition(TrainType validType){
        this.VALID_TYPE=validType;
    }

    @Override
    public boolean isApplicable(Train train,Client client){
        return train.getType()==VALID_TYPE;
    }
}