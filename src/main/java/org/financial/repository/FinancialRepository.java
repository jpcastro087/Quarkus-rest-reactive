package org.financial.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.financial.domain.StockMarketAction;
@ApplicationScoped
public class FinancialRepository implements PanacheRepository<StockMarketAction> {
}
