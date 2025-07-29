package condition;

import model.Train;
import model Train.TrainType;
import model.Client;

public interface Condition{
    boolean isApplicabile(Train.TrainType trainType,Client client);
    /*
    * verifica che la condizione e di conseguenza la promozione sia valida o per il tipo di treno o per il cliente
    */
}