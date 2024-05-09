package org.financial.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class StockMarketAction {
    @Id
    @GeneratedValue
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
}
