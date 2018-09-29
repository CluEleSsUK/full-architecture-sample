import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.apache.kafka.clients.producer.ProducerConfig

class AppConfiguration : Configuration() {
  lateinit var bootstrapServers: String
}

class DefaultApplication: Application<AppConfiguration>() {

  override fun getName(): String {
    return "application"
  }

  override fun initialize(bootstrap: Bootstrap<AppConfiguration>) {
    bootstrap.objectMapper
        .registerModule(KotlinModule())
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
  }

  override fun run(configuration: AppConfiguration, environment: Environment) {
    val combinedKafkaProps = AnalyticsWriter.localKafkaProps(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to configuration.bootstrapServers)
    environment
        .jersey()
        .register(AnalyticsResource(AnalyticsWriter.defaultInstance(combinedKafkaProps)))
  }
}

fun main(args: Array<String>) {
  DefaultApplication().run(*args)
}
