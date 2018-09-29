package cluelessuk.eventretention

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Mono
import java.time.Instant

@SpringBootApplication
class EventRetentionApplication

@Autowired
lateinit var consumer: AnalyticsConsumer

fun main(args: Array<String>) {
  runApplication<EventRetentionApplication>(*args)

  consumer.consumeAndPersist()
}

data class AnalyticsEvent(val clientId: String, val action: String, val time: Instant)

interface PermanentStore {
  fun persist(event: AnalyticsEvent): Mono<Void>
}