import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

class DefaultApplication: Application<AnalyticsConfiguration>() {

  override fun getName(): String {
    return "application"
  }

  override fun initialize(bootstrap: Bootstrap<AnalyticsConfiguration>) {
    bootstrap.objectMapper.registerModule(KotlinModule())
  }

  override fun run(configuration: AnalyticsConfiguration, environment: Environment) {
    environment
        .jersey()
        .register(AnalyticsResource(configuration.message))
  }
}

fun main(args: Array<String>) {
  DefaultApplication().run(*args)
}
