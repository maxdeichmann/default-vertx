package standard.dto

import io.vertx.codegen.annotations.DataObject
import io.vertx.core.json.JsonObject
import io.vertx.core.shareddata.Shareable

@DataObject
open class EventBusBaseMessage() : Shareable {
    constructor(json: JsonObject) : this()
    fun toJson() = JsonObject.mapFrom(this)
}