package ca.bc.gov.nrs.api;

import ca.bc.gov.nrs.api.service.QueryExecutorService;
import ca.bc.gov.nrs.api.structs.Payload;
import ca.bc.gov.nrs.api.structs.QueryType;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Application {

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
  public Response executeSQL(@Valid Payload payload) throws SQLException {
    if (payload.queryType() == QueryType.READ) {
      return Response.ok(queryExecutorService.executeQuery(payload.sql())).build();
    }
    queryExecutorService.mutateState(payload.sql());
    return Response.ok().build();
  }
}
