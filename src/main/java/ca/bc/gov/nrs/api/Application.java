package ca.bc.gov.nrs.api;

import ca.bc.gov.nrs.api.service.QueryExecutorService;
import ca.bc.gov.nrs.api.structs.Payload;
import ca.bc.gov.nrs.api.structs.QueryType;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.sql.SQLException;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Application {

  @ConfigProperty(name = "api.key")
  String apiKey;
  private final QueryExecutorService queryExecutorService;

  @Inject
  public Application(QueryExecutorService queryExecutorService) {
    this.queryExecutorService = queryExecutorService;
  }

  @GET
  public Response healthCheck() {
    var result = queryExecutorService.healthCheck();
    if (result == 0) {
      return Response.serverError().build();
    }
    return Response.ok().build();
  }

  @POST
  public Response executeSQL(@HeaderParam("X-API-Key") String xApiKey, @QueryParam("uploadToS3") String uploadToS3,
      @Valid Payload payload) throws SQLException {
    if (!apiKey.equals(xApiKey)) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    if (payload.queryType() == QueryType.READ) {
      var result = queryExecutorService.executeQuery(payload.sql());
      if (uploadToS3 != null) {
        // TODO add upload to S3
        // queryExecutorService.uploadToS3(result);
      }
      return Response.ok(result).build();
    }
    queryExecutorService.mutateState(payload.sql());
    return Response.ok().build();
  }

}
