import com.codahale.metrics.annotation.Timed
import reactor.kafka.sender.SenderResult
import javax.ws.rs.*
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

data class AnalyticsRequest(val action: String)

@Path("/analytics")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class AnalyticsResource(private val writer: AnalyticsWriter) {

  @POST
  @Timed
  @Path("{clientId}")
  fun logEvent(@PathParam("clientId") clientId: String, request: AnalyticsRequest, @Suspended asyncResponse: AsyncResponse) {
    writer.write(clientId, request.action).subscribe(
        { result -> handleResponse(result, asyncResponse) },
        { _ -> asyncResponse.resume(Response.serverError()) }
    )
  }

  private fun handleResponse(result: SenderResult<String>, asyncResponse: AsyncResponse) {
    if (result.exception() != null) {
      asyncResponse.resume(Response.serverError())
    } else {
      asyncResponse.resume(Response.noContent())
    }
  }
}
