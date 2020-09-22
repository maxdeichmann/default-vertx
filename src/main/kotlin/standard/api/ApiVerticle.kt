package standard.api

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.JsonArray
import io.vertx.micrometer.PrometheusScrapingHandler
import io.vertx.micrometer.backends.BackendRegistries
import mu.KotlinLogging
import standard.dto.SearchDto
import standard.model.Portfolio
import standard.persistence.PersistenceService
import java.util.*

class ApiVerticle(
        val persistenceService: PersistenceService
) : AbstractVerticle() {

    private val logger = KotlinLogging.logger {}

    override fun start(startFuture: Future<Void>) {

        val port = 8080

        val server = vertx.createHttpServer()
        val router = Router.router(vertx)

        val metricsHandler = MetricsHandler(BackendRegistries.getDefaultNow())

        router.get("/test")
                .handler(metricsHandler)
                .handler(::handleTest)

        server.requestHandler(router)

        server.listen(port, "0.0.0.0") {
            if (it.succeeded()) {
                logger.info { "Started API at port $port" }
                startFuture.complete()
            } else {
                logger.error { "Could not start API" }
                startFuture.fail(it.cause())
            }
        }
    }

    private fun handleTest(ctx: RoutingContext) {

        val searchDto = SearchDto(UUID.randomUUID(), UUID.randomUUID(), "string")

        vertx.eventBus().send<List<Portfolio>>("portfolio", searchDto, DeliveryOptions().setCodecName("SearchDtoCodec"), Handler { arPortfolios ->
            if (arPortfolios.succeeded()) {
                val array = arPortfolios.result().body().map { it.toJson() }
                val res: JsonArray = JsonArray(array)
                ctx.response().end(res.toString())
            } else {
                ctx.response().setStatusCode(500).end(arPortfolios.cause().message)
            }
        })
    }
}