package org.financial.domain;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class StockMarketAction extends PanacheEntity {
    private Long id;
    private String symbol;
    private double closePrice;
    private double absoluteChange;
    private double percentageChange;
    private double highPrice;
    private double lowPrice;
    private double openPrice;
    private double previousClosePrice;
    private long timestamp;


  /*  public static Uni<StockMarketAction> addProduct(StockMarketAction stockMarketAction) {


        return Panache
                .withTransaction(stockMarketAction::persist)
                .replaceWith(stockMarketAction)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(t -> new IllegalStateException(t));
    }*/
}
