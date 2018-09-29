package cluelessuk.eventretention

import org.springframework.stereotype.Component
import reactor.kafka.receiver.KafkaReceiver

@Component
class AnalyticsConsumer(private val receiver: KafkaReceiver<String, AnalyticsEvent>, val store: PermanentStore) {

  fun consumeAndPersist() {
    receiver.receive()
        .flatMap{ record -> store.persist(record.value()) }
        .flatMap{ receiver.doOnConsumer { consumer -> consumer.commitAsync() }}
        .subscribe(
            { _ -> logReceived() },
            this::sendToDeadLetterAndSeekPast
        )
  }

  private fun logReceived() {
    println("Message stored")
  }

  private fun sendToDeadLetterAndSeekPast(error: Throwable) {
    //TODO: send to dead letter queue
    error.printStackTrace()
    receiver.doOnConsumer { it.commitAsync() }.subscribe()
  }

}