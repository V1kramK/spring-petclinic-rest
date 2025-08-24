/**
 * MCP Server function for Update a visit by ID
 */

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.function.Function;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

class Put_Visits_Visit_IdMCPTool {
    
    public static Function<MCPServer.MCPRequest, MCPServer.MCPToolResult> getPut_Visits_Visit_IdHandler(MCPServer.APIConfig config) {
        return (request) -> {
            try {
                Map<String, Object> args = request.getArguments();
                if (args == null) {
                    return new MCPServer.MCPToolResult("Invalid arguments object", true);
                }
                
                List<String> queryParams = new ArrayList<>();
        if (args.containsKey("date")) {
            queryParams.add("date=" + args.get("date"));
        }
        if (args.containsKey("description")) {
            queryParams.add("description=" + args.get("description"));
        }
        if (args.containsKey("visitId")) {
            queryParams.add("visitId=" + args.get("visitId"));
        }
                
                String queryString = queryParams.isEmpty() ? "" : "?" + String.join("&", queryParams);
                String url = config.getBaseUrl() + "/api/v2/put_visits_visit_id" + queryString;
                
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + config.getApiKey())
                    .header("Accept", "application/json")
                    .GET()
                    .build();
                
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() >= 400) {
                    return new MCPServer.MCPToolResult("API error: " + response.body(), true);
                }
                
                // Pretty print JSON
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.body());
                String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
                
                return new MCPServer.MCPToolResult(prettyJson);
                
            } catch (IOException | InterruptedException e) {
                return new MCPServer.MCPToolResult("Request failed: " + e.getMessage(), true);
            } catch (Exception e) {
                return new MCPServer.MCPToolResult("Unexpected error: " + e.getMessage(), true);
            }
        };
    }
    
    public static MCPServer.Tool createPut_Visits_Visit_IdTool(MCPServer.APIConfig config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> dateProperty = new HashMap<>();
        dateProperty.put("type", "string");
        dateProperty.put("required", false);
        dateProperty.put("description", "Input parameter: The date of the visit.");
        properties.put("date", dateProperty);
        Map<String, Object> descriptionProperty = new HashMap<>();
        descriptionProperty.put("type", "string");
        descriptionProperty.put("required", true);
        descriptionProperty.put("description", "Input parameter: The description for the visit.");
        properties.put("description", descriptionProperty);
        Map<String, Object> visitIdProperty = new HashMap<>();
        visitIdProperty.put("type", "string");
        visitIdProperty.put("required", true);
        visitIdProperty.put("description", "The ID of the visit.");
        properties.put("visitId", visitIdProperty);
        parameters.put("properties", properties);
        
        MCPServer.ToolDefinition definition = new MCPServer.ToolDefinition(
            "put_visits_visit_id",
            "Update a visit by ID",
            parameters
        );
        
        return new MCPServer.Tool(definition, getPut_Visits_Visit_IdHandler(config));
    }
    
}