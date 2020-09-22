package standard.api

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import java.time.Duration


class MetricsHandler(
        private val metricRegistry: MeterRegistry
) : (RoutingContext) -> Unit {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun invoke(context: RoutingContext) = measureTiming(context)

    private fun measureTiming(context: RoutingContext) {
        try {
            val method = context.request().method().name.toUpperCase()
            val path = getPath(context)

            val requestTags = Tags.of(
                    Tag.of("uri", path),
                    Tag.of("method", method)
            )
            measureRequestCount(requestTags)

            val start = System.nanoTime()
            context.addBodyEndHandler {
                val statusCode = context.response().statusCode
                val responseTags = Tags.of(
                        Tag.of("status", statusCode.toString()),
                        Tag.of("series", getSeries(statusCode)),
                        Tag.of("method", method),
                        Tag.of("uri", path)
                )
                val end = System.nanoTime()
                val duration = Duration.ofNanos(end - start)
                measureResponseTime(responseTags, duration)
            }
        } catch (e: Exception) {
            logger.error("Failed to monitor request: {} {}", e, context.request().method(), context.request().path())
        } finally {
            context.next()
        }
    }

    private fun measureRequestCount(tags: Tags) {
        metricRegistry.counter("http_server_requests_requestCount", tags).increment()
    }

    private fun measureResponseTime(tags: Tags, duration: Duration) {
        metricRegistry.timer("http_server_requests_responseTime", tags).record(duration)
    }

    companion object {
        private val SERIES = arrayOf(
                "invalid",
                "INFORMATIONAL",
                "SUCCESSFUL",
                "REDIRECTION",
                "CLIENT_ERROR",
                "SERVER_ERROR"
        )

        private fun getSeries(statusCode: Int) = SERIES[statusCode / 100]

        /**
         * keep in mind context.currentRoute() on a global handler has no path.
         * you'll need to call it on every route to get placeholder or you'll get the full uri (i.e. filled placeholders)
         */
        private fun getPath(context: RoutingContext): String =
                context.currentRoute().path
                        ?: context.request().path()
                        ?: "unmapped"
    }
}