package standard.model

import com.fasterxml.jackson.annotation.JsonFormat
import standard.dto.EventBusBaseMessage
import io.vertx.core.json.JsonObject
import java.math.BigDecimal
import java.util.*

data class Trade(
        val id: UUID,
        val portfolioId: UUID,
        val instrumentId: String,
        @field: JsonFormat(shape = JsonFormat.Shape.STRING)
        val size: BigDecimal,
        @field: JsonFormat(shape = JsonFormat.Shape.STRING)
        val price: BigDecimal,
        val exchange: String
) : EventBusBaseMessage() {
    constructor(json: JsonObject) : this(
            json.getString("id").let(UUID::fromString),
            json.getString("portfolioId").let(UUID::fromString),
            json.getString("instrumentId"),
            json.getString("size").let { BigDecimal(it) },
            json.getString("price").let { BigDecimal(it) },
            json.getString("exchange")
    )
}
