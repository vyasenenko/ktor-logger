package pro.vyasenenko.logger

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.time.delay
import org.junit.Test
import org.slf4j.event.Level
import java.time.Duration
import java.util.*
import kotlin.test.assertEquals


/**
 * Testable app.
 */
fun Application.main() {
//    install(KLogger)
    install(KLogger) {
        defaultLevel = Level.INFO
    }
    install(DefaultHeaders)
    install(CallLogging)

    val timer = Timer()

    try {
        throw RuntimeException("Example error")
    } catch (e : Throwable) {
        e.log { "Error" }
    }

    routing {
        get("/") {

            timer.run()
            val message = call.request.headers["message"].log { "Message from header: $this" }
            val color = call.request.headers["color"].logc { "${it.BLUE}Color from header: $this" }

            delay(Duration.ofSeconds(1L))

            timer.stop()
            call.respondText("Message from header: $message, Color: $color")
        }
    }
}


class Timer : KLoggable {

    private var start: Long? = null
    private var stop: Long? = null

    fun run() {
        start = Date().log { "Run in $this" }.time
    }

    fun stop() {
        stop = Date().time
        logc { "${it.RED}Stop timer. ${it.RESET}Time work ${stop!! - start!!} milliseconds" }
    }
}


class Samples {
    val expectedMessage = "Hello world"
    val expectedColorMessage = "May be green?"

    @Test
    fun `ktor example use`() {
        withTestApplication(Application::main) {
            with(handleRequest(HttpMethod.Get, "/") {

                addHeader("message", expectedMessage)
                addHeader("color", expectedColorMessage)
            }) {

                assertEquals(HttpStatusCode.OK, response.status())

                assertEquals("Message from header: $expectedMessage, Color: $expectedColorMessage", response.content)
            }
        }
    }
}