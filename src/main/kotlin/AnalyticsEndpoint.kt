import com.codahale.metrics.annotation.Timed
import io.dropwizard.Configuration
import org.hibernate.validator.constraints.NotEmpty
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

class AnalyticsConfiguration : Configuration() {
  @NotEmpty
  lateinit var message: String
}

data class AnalyticsRequest(val action: String)

@Path("/analytics")
@Consumes(MediaType.APPLICATION_JSON)
class AnalyticsResource(private val message: String) {

  @POST
  @Timed
  @Path("{clientId}")
  fun logEvent(@PathParam("clientId") clientId: String, request: AnalyticsRequest) {
    println("ClientId is $clientId and request is $request and message is $message")
  }
}
