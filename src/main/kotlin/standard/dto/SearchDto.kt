package standard.dto

import io.vertx.codegen.annotations.DataObject
import io.vertx.core.json.JsonObject
import java.util.*

@DataObject
data class SearchDto(val portfolioId: UUID?, val tradeId: UUID?, val instrumentId: String?) : EventBusBaseMessage() {
    constructor(json: JsonObject) : this(
            json.getString("portfolioId")?.let(UUID::fromString),
            json.getString("tradeId")?.let(UUID::fromString),
            json.getString("instrumentId")
    )
}