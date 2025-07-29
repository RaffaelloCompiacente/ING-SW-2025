package service;

import model.Train;
import model.PromoResult;
import model.DicountResult,
import model.VoucherResult;
import model.Promotion;
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

   /* public List<Promotion> findApplicablePromotions(Train train){
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
    }*/
    public List<PromoResult> evaluatePromotions(Train train,Client client,double basePrice){
        List<PromoResult> results= new ArrayList<>();
        for(Promotion promo: promotions){
            double resultPromo= promo.applyPromotion(basePrice,train.getType(),client);
            if(resultPromo!=basePrice){
                results.add(buildPromoResult(promo,resultPromo));
            }
        }return results;
    }

    private PromoResut buildPromoResult(Promotion promo,double resultPromo){
        PromoStrategy strategy= promo.getStrategy();
        return switch(strategy){
            case VoucherGenerationStrategy v -> new VoucherResult(resultPromo,promo);
            case FixedAmountDiscountStrategy f -> new DiscountResult(resultPromo,promo);
            case PercentageDiscountStrategy p -> new DiscountResult(resultPromo,promo);
        }
    }

}