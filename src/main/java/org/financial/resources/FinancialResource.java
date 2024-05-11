package org.financial.resources;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.financial.domain.StockMarketAction;
import org.financial.request.StockMarketActionRequest;
import org.financial.response.ErrorResponse;
import org.financial.response.QuoteResponse;
import org.financial.services.FinancialService;

import java.util.List;

@Path("/v1/stock")
@ApplicationScoped
public class FinancialResource {
    @Inject
    private FinancialService financialService;

    @POST
    @Path("reactive")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response>  createReactive(StockMarketActionRequest request) {
        Uni<Response> stockMarketAction = financialService.createStockMarketActionReactive(request);
        return stockMarketAction;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reactive/repository")
    public Uni<StockMarketAction> createReactiveRepository(StockMarketActionRequest request) {
        return financialService.createStockMarketActionReactiveFromRepository(request);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reactive")
    public Uni<List<PanacheEntityBase>> getStockMarketActionsReactive() {
        return financialService.findAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reactive/repository")
    public Uni<List<StockMarketAction>> getStockMarketActionsReactiveRepository() {
        return financialService.findAllRepository();
    }




}
