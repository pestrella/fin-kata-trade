package fin.trade;

import java.util.concurrent.ConcurrentHashMap;

/* O(1) read performance */
public class FastTradeAnalyser implements TradeAnalyser {
    private ConcurrentHashMap<String, Statistic> statistics;

    public FastTradeAnalyser() {
        this.statistics = new ConcurrentHashMap<>();
    }

    @Override
    public void update(Trade trade) {
        statistics.compute(trade.getProduct(), (k, stat) -> {
            if (stat == null)
                return new Statistic(trade, 1, trade.getVolume());

            Trade last = trade.getTimestamp() >= stat.getLastTrade().getTimestamp()
                    ? trade
                    : stat.getLastTrade();

            return new Statistic(last, stat.getTradeCount() + 1, stat.getTotalVolume() + trade.getVolume());
        });

    }

    @Override
    public double latestPrice(String product) {
        Statistic stat = statistics.get(product);
        if (stat == null)
            throw new RuntimeException("Latest price not known for product: " + product);

        return stat.getLastTrade().getPrice();
    }

    @Override
    public int averageVolume(String product) {
        Statistic stat = statistics.get(product);
        if (stat == null)
            return 0;

        return stat.getTotalVolume() / stat.getTradeCount();
    }

    private class Statistic {
        final Trade lastTrade;
        final int tradeCount;
        final int totalVolume;

        private Statistic(Trade trade, int count, int volume) {
            this.lastTrade = trade;
            this.tradeCount = count;
            this.totalVolume = volume;
        }

        Trade getLastTrade() {
            return lastTrade;
        }

        int getTradeCount() {
            return tradeCount;
        }

        int getTotalVolume() {
            return totalVolume;
        }
    }
}
