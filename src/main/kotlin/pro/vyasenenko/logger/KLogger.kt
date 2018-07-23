package pro.vyasenenko.logger

import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.log
import io.ktor.pipeline.PipelinePhase
import io.ktor.util.AttributeKey
import org.slf4j.Logger
import org.slf4j.event.Level

/**
 * User friendly logger.
 */
class KLogger(val log: Logger, val defaultLevel: Level) {

    /**
     * Configuration for [KLogger] feature
     */
    class Configuration {

        /**
         * Default Logging level for [KLogger], default is [Level.TRACE]
         */
        var defaultLevel: Level = Level.TRACE
    }

    /**
     * Installable feature for [KLogger].
     */
    companion object Feature : ApplicationFeature<Application, Configuration, KLogger> {
        override val key: AttributeKey<KLogger> = AttributeKey("KLogger")
        override fun install(pipeline: Application, configure: Configuration.() -> Unit): KLogger {
            val loggingPhase = PipelinePhase("Logger")
            val configuration = Configuration().apply(configure)
            val feature = KLogger(pipeline.log, configuration.defaultLevel)
            pipeline.insertPhaseBefore(ApplicationCallPipeline.Infrastructure, loggingPhase)
            pipeline.intercept(loggingPhase) {
                proceed()
            }
            kLogger = feature
            return feature
        }

        private var kLogger: KLogger? = null

        val logger: Logger?
            get() = kLogger?.log

        val level: Level
            get() = kLogger?.defaultLevel ?: Level.TRACE
    }
}