package pro.vyasenenko.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

interface KLoggable {

    val logger: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    /**
     * Default console log.
     *
     * Samples:
     *
     *      Any.log { this -> this.toString() }
     *
     *      Any.log { "$this" }
     *
     *      // back object with work
     *      val any = Any.log { "$this" }
     *
     *      // change log level
     *      val any = Any.log(Level.DEBUG) { "$this" }
     */
    fun <T> T.log(level: Level = KLogger.level, function: T.() -> String): T {
        function().log(level)
        return this
    }

    /**
     * Colored console log.
     *
     * Samples:
     *
     *      Any.logc { it.GREEN + "$this" }
     *
     *      Any.logc { "${it.GREEN}$this" }
     *
     *      // back object with work
     *      val any = Any.logc { "${it.GREEN}$this" }
     *
     *      // change log level
     *      val any = Any.logc(Level.DEBUG) { "${it.GREEN}$this" }
     */
    fun <T> T.logc(level: Level = KLogger.level, function: T.(ConsoleColor) -> String): T {
        (this.function(ConsoleColor) + ConsoleColor.RESET).log(level)
        return this
    }

    /**
     * Console log by string.
     *
     *      "Log message".log()
     *
     *      "Log message".log(Level.DEBUG)
     */
    fun String.log(level: Level = KLogger.level): String {
        val text = this
        logger.apply {
            when (level) {
                Level.INFO -> info(text)
                Level.DEBUG -> debug(text)
                Level.TRACE -> trace(text)
                Level.WARN -> warn(text)
                Level.ERROR -> error(text)
            }
        }
        return this
    }
}