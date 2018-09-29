import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult
import util.JsonSerializer
import java.time.Instant

data class AnalyticsEvent(val clientId: String, val action: String, val time: Instant)

class AnalyticsWriter(private val producer: KafkaSender<String, AnalyticsEvent>) {

  private val analyticsTopic = "analytics"

  fun write(clientId: String, action: String): Flux<SenderResult<String>> {

    val record = Mono.just(AnalyticsEvent(clientId, action, Instant.now()))
        .map { ProducerRecord(analyticsTopic, clientId, it) }
        .map { SenderRecord.create(it, clientId) }

    return producer.send(record)
  }

  companion object {

    fun localKafkaProps(vararg extraParams: Pair<String, Any>): Map<String, Any> {
      return mapOf(
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
          ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
          *extraParams
      )
    }

    private fun kafkaSender(props: Map<String, Any>): KafkaSender<String, AnalyticsEvent> {
      return KafkaSender.create(SenderOptions.create<String, AnalyticsEvent>(props))
    }

    fun defaultInstance(kafkaProps: Map<String, Any> = localKafkaProps()): AnalyticsWriter {
      return AnalyticsWriter(kafkaSender(kafkaProps))
    }
  }

}