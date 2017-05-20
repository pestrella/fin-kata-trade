package fin.trade;

public class Trade {
    private final String product;
    private final double price;
    private final int volume;
    private final long timestamp;

    public Trade(String product, double price, int volume, long timestamp) {
        this.product = product;
        this.price = price;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
