package org.financial.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.financial.domain.StockMarketAction;

@ApplicationScoped
public class FinancialRepository implements PanacheRepositoryBase<StockMarketAction,Long> {
}
