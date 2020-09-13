package standard.persistence

import io.vertx.core.AbstractVerticle
import io.vertx.serviceproxy.ServiceBinder

class PersistenceVerticle() : AbstractVerticle() {

    override fun start() {
        ServiceBinder(vertx)
                .setAddress("standard.persistence-service")
                .register(PersistenceService::class.java, PersistenceServiceImpl(vertx))
    }

}