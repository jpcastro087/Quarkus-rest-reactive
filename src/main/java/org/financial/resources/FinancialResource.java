package org.financial.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.financial.domain.StockMarketAction;
import org.financial.request.StockMarketActionRequest;
import org.financial.response.ErrorResponse;
import org.financial.services.FinancialService;

import java.util.List;

@Path("/stock")
public class FinancialResource {
    @Inject
    private FinancialService financialService;


    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StockMarketAction> getStockMarketActions() {
        return financialService.getStockMarketActions();
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(StockMarketActionRequest request) {
        try {
            StockMarketAction StockMarketAction = financialService.createStockMarketAction(request);
            return Response.ok(StockMarketAction).build();
        } catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }

    }


}
