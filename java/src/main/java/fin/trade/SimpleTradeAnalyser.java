package fin.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/* O(n) read performance */
public class SimpleTradeAnalyser implements TradeAnalyser {
    private ConcurrentHashMap<String, List<Trade>> trades;

    public SimpleTradeAnalyser() {
        this.trades = new ConcurrentHashMap<>();
    }

    public void update(final Trade trade) {
        trades.compute(trade.getProduct(), (product, trades) -> {
            List<Trade> copy = (trades == null) ? new ArrayList<>() : new ArrayList<>(trades);
            copy.add(trade);
            return copy;
        });
    }

    public double latestPrice(String product) {
        Optional<Trade> trade = this.trades.getOrDefault(product, new ArrayList<>()).stream()
                .reduce((prev, next) -> next.getTimestamp() >= prev.getTimestamp() ? next : prev);

        if (!trade.isPresent())
            throw new RuntimeException("Latest price not known for product: " + product);

        return trade.get().getPrice();
    }

    public int averageVolume(String product) {
        List<Trade> trades = this.trades.getOrDefault(product, new ArrayList<>());
        int total = trades.stream()
                .mapToInt(Trade::getVolume)
                .sum();

        return (trades.size() != 0) ? total / trades.size() : 0;
    }
}
