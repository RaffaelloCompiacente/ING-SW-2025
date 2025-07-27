òpackage condition;

import model.Client;
import model.Train;

public class FidelityProgramCondition implements PromoCondition{
    private final LoyaltyLevel MIN_LEVEL;

    public FidelityCondition(LoyaltyLevel minLevel){
        this.MIN_LEVEL=minLevel;
    }

    @Override
    public boolean isApplicable(Train train,Client client ){
        return client !=null && client.getLevel().ordinal()>=MIN_LEVEL;
    }
}