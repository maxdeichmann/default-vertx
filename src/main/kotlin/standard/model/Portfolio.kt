package standard.model

import io.vertx.codegen.annotations.DataObject
import io.vertx.core.json.JsonObject
import standard.dto.EventBusBaseMessage
import java.util.*

@DataObject
data class Portfolio(
        val id: UUID,
        val userId: UUID,
        val trades: List<Trade>
) : EventBusBaseMessage() {
    constructor(json: JsonObject) : this(
            json.getString("id").let(UUID::fromString),
            json.getString("userId").let(UUID::fromString),
            json.getJsonArray("trades").map { Trade(it as JsonObject) }
    )
}
