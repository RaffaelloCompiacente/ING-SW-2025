package model;

public interface PromoResult{
    void accept(PromoResultVisitor visitor);
}