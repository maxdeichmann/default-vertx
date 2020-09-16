package standard.persistence

import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.serviceproxy.ServiceBinder
import standard.dto.SearchDto
import standard.model.Portfolio
import standard.model.Trade
import java.math.BigDecimal
import java.util.*

class PersistenceVerticle() : AbstractVerticle() {

    override fun start() {


        vertx.eventBus().consumer<SearchDto>("portfolio", Handler { message ->

            // Replying is same as publishing
            val portfolios = (1..10).map {
                val trades = (1..400).map { Trade(UUID.randomUUID(), UUID.randomUUID(), "BASF", BigDecimal.TEN, BigDecimal.ONE, "LSX") }
                Portfolio(UUID.randomUUID(), UUID.randomUUID(), trades)
            }


            message.reply(portfolios)
        })

    }

}