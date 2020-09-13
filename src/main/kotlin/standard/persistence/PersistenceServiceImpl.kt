package standard.persistence

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import standard.dto.SearchDto
import standard.model.Portfolio
import standard.model.Trade
import java.math.BigDecimal
import java.util.*

class PersistenceServiceImpl(val vertx: Vertx) : PersistenceService {

    override fun getPortfolio(conditions: SearchDto, handler: Handler<AsyncResult<List<Portfolio>>>) {

        val portfolios = (1..10).map {
            val trades = (1..400).map { Trade(UUID.randomUUID(), UUID.randomUUID(), "BASF", BigDecimal.TEN, BigDecimal.ONE, "LSX") }
            Portfolio(UUID.randomUUID(), UUID.randomUUID(), trades)
        }

        handler.handle(Future.succeededFuture(portfolios))
    }
}