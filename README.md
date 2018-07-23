# ktor logger

Kotlin Wrapper 'org.slf4j:slf4j' 

Use examples
* Default logging
```kotlin
Any().log { this.toString() }
 
Any().log { "$this" }

// back object with work
val date = Date().log { "Time in milliseconds: ${this.time}" }


// change log level
val any = Any().log(Level.DEBUG) { "$this" }
```
Output
```$xslt
    INFO   Application - java.lang.Object@6e6d7a4
    INFO   Application - java.lang.Object@6e6d7a4
    INFO   Application - Time in milliseconds: 1532348742345
    DEBUG  Application - java.lang.Object@6e6d7a4
```
* Colored logging
```kotlin
val date = Date().logc { "{$it.BLUE}Time in milliseconds: ${this.time}" }
```
Output
```text
    INFO  Application - [0;34mTime in milliseconds: 1532348742344
```
