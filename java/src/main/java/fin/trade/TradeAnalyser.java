package fin.trade;

public interface TradeAnalyser {
    void update(Trade trade);
    double latestPrice(String product);
    int averageVolume(String product);
}
