package org.financial.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuoteResponse {
    @JsonProperty("c")
    private double closePrice; // Precio de cierre

    @JsonProperty("d")
    private double absoluteChange; // Cambio absoluto

    @JsonProperty("dp")
    private double percentageChange; // Cambio porcentual

    @JsonProperty("h")
    private double highPrice; // Precio más alto

    @JsonProperty("l")
    private double lowPrice; // Precio más bajo

    @JsonProperty("o")
    private double openPrice; // Precio de apertura

    @JsonProperty("pc")
    private double previousClosePrice; // Precio de cierre previo

    @JsonProperty("t")
    private long timestamp; // Timestamp
}
