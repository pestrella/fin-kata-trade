package fin.trade;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleTradeAnalyserTest {
    @Test
    public void zeroAverage() {
        TradeAnalyser analyser = new SimpleTradeAnalyser();
        assertThat(analyser.averageVolume("product_1"), is(0));
    }

    @Test(expected = RuntimeException.class)
    public void noLastPrice() {
        new SimpleTradeAnalyser().latestPrice("product_1");
    }
}
