package org.financial.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.financial.response.QuoteResponse;

@RegisterRestClient
public interface FinnHubClient {

    @GET
    @Path("/v1/quote")
    QuoteResponse getQuote(@QueryParam("symbol") String symbol, @QueryParam("token") String token);
}