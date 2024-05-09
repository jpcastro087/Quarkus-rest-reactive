package org.financial.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.financial.client.FinnHubClient;
import org.financial.domain.StockMarketAction;
import org.financial.repository.FinancialRepository;
import org.financial.request.StockMarketActionRequest;
import org.financial.response.QuoteResponse;

import java.util.List;

@ApplicationScoped
public class FinancialService {

    @Inject
    @ConfigProperty(name = "financial.token")
    String token;

    @RestClient
    private FinnHubClient finnHubClient;

    @Inject
    private FinancialRepository financialRepository;

    @Transactional
    public StockMarketAction createStockMarketAction(StockMarketActionRequest request) {
        QuoteResponse quote = finnHubClient.getQuote(request.getSymbol(), token);
        if (!this.isValidResponse(quote)) {
            throw new NotFoundException("The symbol '" + request.getSymbol() + "' appears to not exist");
        }
        StockMarketAction stockMarketAction = this.parseToStockMarketAction(quote, request.getSymbol());
        financialRepository.persist(stockMarketAction);
        return stockMarketAction;
    }


    public List<StockMarketAction> getStockMarketActions() {
        List<StockMarketAction> stocks = financialRepository.findAll().stream().toList();
        return stocks;
    }


    private StockMarketAction parseToStockMarketAction(QuoteResponse quoteResponse, String symbol) {
        StockMarketAction stockMarketAction = new StockMarketAction();
        stockMarketAction.setSymbol(symbol.toUpperCase());
        stockMarketAction.setClosePrice(quoteResponse.getClosePrice());
        stockMarketAction.setAbsoluteChange(quoteResponse.getAbsoluteChange());
        stockMarketAction.setPercentageChange(quoteResponse.getPercentageChange());
        stockMarketAction.setHighPrice(quoteResponse.getHighPrice());
        stockMarketAction.setLowPrice(quoteResponse.getLowPrice());
        stockMarketAction.setOpenPrice(quoteResponse.getOpenPrice());
        stockMarketAction.setPreviousClosePrice(quoteResponse.getPreviousClosePrice());
        stockMarketAction.setTimestamp(quoteResponse.getTimestamp());
        return stockMarketAction;
    }

    private boolean isValidResponse(QuoteResponse quoteResponse) {
        return quoteResponse.getClosePrice() > 0 && quoteResponse.getHighPrice() > 0 &&
                quoteResponse.getLowPrice() > 0 && quoteResponse.getOpenPrice() > 0 &&
                quoteResponse.getPreviousClosePrice() > 0 && quoteResponse.getTimestamp() > 0;
    }


}
