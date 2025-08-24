/**
 * MCP Server function for Update a pet type by ID
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

class Put_Pettypes_Pet_Type_IdMCPTool {
    
    public static Function<MCPServer.MCPRequest, MCPServer.MCPToolResult> getPut_Pettypes_Pet_Type_IdHandler(MCPServer.APIConfig config) {
        return (request) -> {
            try {
                Map<String, Object> args = request.getArguments();
                if (args == null) {
                    return new MCPServer.MCPToolResult("Invalid arguments object", true);
                }
                
                List<String> queryParams = new ArrayList<>();
        if (args.containsKey("name")) {
            queryParams.add("name=" + args.get("name"));
        }
        if (args.containsKey("petTypeId")) {
            queryParams.add("petTypeId=" + args.get("petTypeId"));
        }
        if (args.containsKey("id")) {
            queryParams.add("id=" + args.get("id"));
        }
                
                String queryString = queryParams.isEmpty() ? "" : "?" + String.join("&", queryParams);
                String url = config.getBaseUrl() + "/api/v2/put_pettypes_pet_type_id" + queryString;
                
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
    
    public static MCPServer.Tool createPut_Pettypes_Pet_Type_IdTool(MCPServer.APIConfig config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> nameProperty = new HashMap<>();
        nameProperty.put("type", "string");
        nameProperty.put("required", false);
        nameProperty.put("description", "Input parameter: The name of the pet type.");
        properties.put("name", nameProperty);
        Map<String, Object> petTypeIdProperty = new HashMap<>();
        petTypeIdProperty.put("type", "string");
        petTypeIdProperty.put("required", true);
        petTypeIdProperty.put("description", "The ID of the pet type.");
        properties.put("petTypeId", petTypeIdProperty);
        Map<String, Object> idProperty = new HashMap<>();
        idProperty.put("type", "string");
        idProperty.put("required", false);
        idProperty.put("description", "Input parameter: The ID of the pet type.");
        properties.put("id", idProperty);
        parameters.put("properties", properties);
        
        MCPServer.ToolDefinition definition = new MCPServer.ToolDefinition(
            "put_pettypes_pet_type_id",
            "Update a pet type by ID",
            parameters
        );
        
        return new MCPServer.Tool(definition, getPut_Pettypes_Pet_Type_IdHandler(config));
    }
    
}