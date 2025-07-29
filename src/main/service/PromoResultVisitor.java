package service;

public interface PromoResultVisitor{
    void visit(DiscounterResult result);
    void visit(VoucherResult result);
    // possibile implementazione futura void visit(PointResult result);
}