package ca.bc.gov.nrs.api.structs;

public record Payload(QueryType queryType, String sql) {
}
