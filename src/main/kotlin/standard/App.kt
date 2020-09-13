/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package standard

import io.vertx.core.Vertx
import standard.api.ApiVerticle
import standard.persistence.PersistenceServiceImpl
import standard.persistence.PersistenceServiceVertxEBProxy
import standard.persistence.PersistenceVerticle

class App {


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var vertx = Vertx.vertx()

            vertx.deployVerticle(PersistenceVerticle())
            vertx.deployVerticle(ApiVerticle(PersistenceServiceVertxEBProxy(vertx, "standard.persistence-service")))

        }
    }

}
