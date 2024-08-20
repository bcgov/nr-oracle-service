package ca.bc.gov.nrs.api.service;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class QueryExecutorService {
  @Inject
  Logger logger;
  private final AgroalDataSource dataSource;

  @Inject
  public QueryExecutorService(AgroalDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public int healthCheck(){
    try (var connection = dataSource.getConnection()) {
      connection.isValid(2);
    }catch (Exception e){
      logger.error("Error during healthcheck: " + e.getMessage(), e);
      return 0;
    }
    return 1;
  }
  public List<Map<String, Object>> executeQuery(String query) throws SQLException {
    List<Map<String, Object>> results = new ArrayList<>();
    try (var connection = dataSource.getConnection()) {
      connection.setReadOnly(true);
      try (var statement = connection.prepareStatement(query)) {
        try(var result = statement.executeQuery()){
          var metaData = result.getMetaData();
          List<String> columnNames = new ArrayList<>();
          for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            columnNames.add(columnName);
          }
          while (result.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
              String columnName = columnNames.get(i);
              Object columnValue = result.getObject(i + 1);
              row.put(columnName, columnValue);
            }
            results.add(row);
          }
        }
      }
    } catch (Exception e) {
      logger.error("Error executing query: " + query, e);
      throw e;
    }
    return results;
  }

  public void mutateState(String query) throws SQLException {
    try (var connection = dataSource.getConnection()) {
      try (var statement = connection.prepareStatement(query)) {
        statement.executeUpdate();
        connection.commit();
      }
    } catch (Exception e) {
      logger.error("Error executing query: " + query, e);
      throw e;
    }
  }
}
