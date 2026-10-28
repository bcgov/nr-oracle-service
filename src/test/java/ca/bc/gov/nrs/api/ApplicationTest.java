package ca.bc.gov.nrs.api;

import ca.bc.gov.nrs.api.service.QueryExecutorService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class ApplicationTest {

  @InjectMock
  QueryExecutorService queryExecutorService;

  @Test
  void healthOkWhenDatabaseReachable() {
    when(queryExecutorService.healthCheck()).thenReturn(1);
    given().when().get("/health").then().statusCode(200);
  }

  @Test
  void healthFailsWhenDatabaseUnreachable() {
    when(queryExecutorService.healthCheck()).thenReturn(0);
    given().when().get("/health").then().statusCode(500);
  }

  @Test
  void rejectsMissingApiKey() throws Exception {
    given().contentType(ContentType.JSON)
        .body("{\"queryType\":\"READ\",\"sql\":\"select 1 from dual\"}")
        .when().post("/").then().statusCode(401);
    verify(queryExecutorService, never()).executeQuery(anyString());
  }

  @Test
  void rejectsWrongApiKey() throws Exception {
    given().contentType(ContentType.JSON).header("X-API-Key", "wrong")
        .body("{\"queryType\":\"READ\",\"sql\":\"select 1 from dual\"}")
        .when().post("/").then().statusCode(401);
    verify(queryExecutorService, never()).executeQuery(anyString());
  }

  @Test
  void readQueryReturnsRows() throws Exception {
    when(queryExecutorService.executeQuery("select 1 as ONE from dual"))
        .thenReturn(List.of(Map.of("ONE", 1)));
    given().contentType(ContentType.JSON).header("X-API-Key", "test-key")
        .body("{\"queryType\":\"READ\",\"sql\":\"select 1 as ONE from dual\"}")
        .when().post("/").then().statusCode(200).body("[0].ONE", equalTo(1));
  }

  @Test
  void mutateQueryUpdatesState() throws Exception {
    given().contentType(ContentType.JSON).header("X-API-Key", "test-key")
        .body("{\"queryType\":\"MUTATE\",\"sql\":\"update t set x = 1\"}")
        .when().post("/").then().statusCode(200);
    verify(queryExecutorService).mutateState("update t set x = 1");
  }
}
