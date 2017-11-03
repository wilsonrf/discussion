import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.*
// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}

def appenderList = ['STDOUT']

logger('org.mongodb', ERROR, appenderList, false)
logger('org.springframework.data.mongodb.core.MongoTemplate', DEBUG, appenderList, false)
logger('org.springframework.data.mongodb.repository.Query', DEBUG, appenderList, false)
logger('org.springframework.data.mongodb.repository.support.SpringDataMongodbQuery', INFO, appenderList, false)
logger('org.springframework.web', WARN, appenderList, false)
root(INFO, appenderList)
