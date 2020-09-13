package standard.persistence

import io.vertx.codegen.annotations.ProxyGen
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import standard.dto.SearchDto
import standard.model.Portfolio

@ProxyGen
interface PersistenceService {
    fun getPortfolio(conditions: SearchDto, handler: Handler<AsyncResult<List<Portfolio>>>)
}