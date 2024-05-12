package org.financial.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
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


    private WebClient webClient;
    @Inject
    private Vertx vertx;

    @Inject
    FinancialRepository financialRepository;

    @PostConstruct
    void initializate() {
        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setDefaultHost("finnhub.io");
        webClientOptions.setDefaultPort(443);
        webClientOptions.setSsl(true);
        webClientOptions.setTrustAll(true);
        this.webClient = WebClient.create(this.vertx, webClientOptions);
    }

    @Inject
    @ConfigProperty(name = "financial.token")
    String token;

    @RestClient
    private FinnHubClient finnHubClient;

/*    @Inject
    private FinancialRepository financialRepository;*/

/*    @Transactional
    public StockMarketAction createStockMarketAction(StockMarketActionRequest request) {
        QuoteResponse quote = finnHubClient.getQuote(request.getSymbol(), token);
        if (!this.isValidResponse(quote)) {
            throw new NotFoundException("The symbol '" + request.getSymbol() + "' appears to not exist");
        }
        StockMarketAction stockMarketAction = this.parseToStockMarketAction(quote, request.getSymbol());
        financialRepository.persist(stockMarketAction);
        return stockMarketAction;
    }*/


    public Uni<Response> createStockMarketActionReactive(StockMarketActionRequest request) {
        return webClient.get(443, "finnhub.io", "/api/v1/quote")
                .setQueryParam("symbol", request.getSymbol())
                .setQueryParam("token", "cojvlkpr01qq4pku97e0cojvlkpr01qq4pku97eg")
                .ssl(true)
                .send()
                .onFailure().invoke( response -> {
                            System.err.println(response.getMessage());
                        }
                ).onItem().transform(
                        response -> {
                            String symbol = request.getSymbol();
                            QuoteResponse quoteResponse = response.bodyAsJson(QuoteResponse.class);
                            StockMarketAction stockMarketAction = parseToStockMarketAction(quoteResponse, symbol);

                            return Panache.withTransaction(stockMarketAction::persist)
                                    .replaceWith(
                                            Response.ok(stockMarketAction).status(Response.Status.CREATED)::build
                                    );

                        }
                ).flatMap(responseUni -> responseUni);
    }

    @WithTransaction
    public Uni<StockMarketAction> createStockMarketActionReactiveFromRepository(StockMarketActionRequest request) {
        return webClient.get(443, "finnhub.io", "/api/v1/quote")
                .setQueryParam("symbol", "AAPL")
                .setQueryParam("token", "cojvlkpr01qq4pku97e0cojvlkpr01qq4pku97eg")
                .ssl(true)
                .send()
                .onFailure().invoke( response -> {
                            System.err.println(response.getMessage());
                        }
                ).onItem().transform(
                        response -> {
                            String symbol = request.getSymbol();
                            QuoteResponse quoteResponse = response.bodyAsJson(QuoteResponse.class);
                            StockMarketAction stockMarketAction = parseToStockMarketAction(quoteResponse, symbol);

                            return financialRepository.persist(stockMarketAction).onItem().transform(response2 -> {
                                return response2;
                            });

                        }
                ).flatMap(responseUni -> responseUni);
    }


    @WithSession
    public Uni<List<PanacheEntityBase>> findAll() {
        return StockMarketAction.listAll();
    }


    @WithSession
    public Uni<List<StockMarketAction>> findAllRepository() {
        return financialRepository.listAll();
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
