Logback Groovy - Sift
=====================
Extends Logback's Groovy DSL so can configure AppenderFactory for SiftingAppender, e.g.:

```groovy
import ch.qos.logback.classic.sift.SiftingAppender
import ch.qos.logback.classic.sift.MDCBasedDiscriminator
import ch.qos.logback.classic.PatternLayout

appender("SIFT", SiftingAppender) {
  discriminator(MDCBasedDiscriminator) {
    key = "userid"
    defaultValue = "unknown"
  }
  sift {
    appender("FILE-${userid}", FileAppender) {
      file = "${userid}.log"
      append = false
      layout(PatternLayout) {
        pattern = "%d [%thread] %level %mdc %logger{35} - %msg%n"
      }
    }
  }
}

root(DEBUG, ["SIFT"])
```

Usage instruction
-----------------
1. Build the jar for this project.
2. Put it where you put `logback-classic-$VERSION.jar`.
3. Done!
