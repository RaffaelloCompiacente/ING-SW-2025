package src.service;

import src.model.Train;
import repository.PromotionRepository;
import java.util.ArrayList;
import java.util.List;

public class PromotionService{
    private List<Promotion> promotions;

    public PromotionService(PromotionRepository promoRepo){
        this.promotions= promoRepo.findAllActive();
    }

    public void reloadPromotions(PromotionRepository promoRepo){
        this.promotions=promoRepo.findAllActive();
    }//Utile per amministratori in caso di utilizzo di file json e non di DB

    public List<Promotion> findApplicablePromotions(Train train){
        List<Promotion> result= new ArrayList<>();
        for(Promotion promo: promotions){
            if(promo.getCondition().isApplicabile(train)){
                result.add(promo);
            }
        }
    }

    public double calculateDiscountedPrice(Train train,double basePrice){
        double finalPrice= basePrice;
        for(Promotion promo:findApplicablePromotions(train)){
            finalPrice=promo.getStrategy().apply(finalPrice);
        }
        return finalPrice;
    }

}