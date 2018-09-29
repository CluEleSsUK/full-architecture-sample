package cluelessuk.eventretention

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

@Configuration
class BeanConfig {

  @Bean
  fun kafkaReceiver(options: ReceiverOptions<String, AnalyticsEvent>): KafkaReceiver<String, AnalyticsEvent> {
    return KafkaReceiver.create(options)
  }

  @Bean
  fun receiverOptions(props: KafkaProperties): ReceiverOptions<String, AnalyticsEvent> {
    return ReceiverOptions.create<String, AnalyticsEvent>(props.buildConsumerProperties())
  }
}
