package pro.vyasenenko.logger

import org.slf4j.event.Level
import pro.vyasenenko.logger.ConsoleColor.RESET

/**
 * Default console log.
 *
 * Before need install KLogger in Ktor app:
 *
 *      install(KLogger)
 *
 *      Or use interface [pro.vyasenenko.logger.KLoggable]
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
inline fun <T> T.log(level: Level = KLogger.level, function: T.() -> String): T {
    val text = function()
    KLogger.logger?.apply {
        when (level) {
            Level.INFO -> info(text)
            Level.DEBUG -> debug(text)
            Level.TRACE -> trace(text)
            Level.WARN -> warn(text)
            Level.ERROR -> error(text)
        }
    } ?: println(text)
    return this
}

/**
 * Colored console log.
 *
 * Before need install KLogger in Ktor app:
 *
 *      install(KLogger)
 *
 *      Or use interface [pro.vyasenenko.logger.KLoggable]
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
 *
 *      val response = response.logc { "${it.GREEN}${this.message}" }
 */
inline fun <T> T.logc(level: Level = KLogger.level, function: T.(ConsoleColor) -> String): T {
    val text = this.function(ConsoleColor) + RESET
    KLogger.logger?.apply {
        when (level) {
            Level.INFO -> info(text)
            Level.DEBUG -> debug(text)
            Level.TRACE -> trace(text)
            Level.WARN -> warn(text)
            Level.ERROR -> error(text)
        }
    } ?: println(text)
    return this
}

/**
 * Error console log.
 */
fun <T : Throwable> T.log(level: Level = KLogger.level, function: T.() -> String): T {
    val text = function()
    KLogger.logger?.let {
        when (level) {
            Level.INFO -> it.info(text, this)
            Level.DEBUG -> it.debug(text, this)
            Level.TRACE -> it.trace(text, this)
            Level.WARN -> it.warn(text, this)
            Level.ERROR -> it.error(text, this)
        }
    }?: println(text)
    return this
}

/**
 * Console log by string.
 *
 * Before need install KLogger in Ktor app:
 *
 *      install(KLogger)
 *
 *      Or use interface [pro.vyasenenko.logger.KLoggable]
 *
 * Samples:
 *
 *      "Log message".log()
 *
 *      "Log message".log(Level.DEBUG)
 */
fun String.log(level: Level = KLogger.level) {
    val text = this

    KLogger.logger?.apply {
        when (level) {
            Level.INFO -> info(text)
            Level.DEBUG -> debug(text)
            Level.TRACE -> trace(text)
            Level.WARN -> warn(text)
            Level.ERROR -> error(text)
        }
    } ?: println(text)
}
