/**
 * MCP Server function for Adds a pet to an owner
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

class Post_Owners_Owner_Id_PetsMCPTool {
    
    public static Function<MCPServer.MCPRequest, MCPServer.MCPToolResult> getPost_Owners_Owner_Id_PetsHandler(MCPServer.APIConfig config) {
        return (request) -> {
            try {
                Map<String, Object> args = request.getArguments();
                if (args == null) {
                    return new MCPServer.MCPToolResult("Invalid arguments object", true);
                }
                
                List<String> queryParams = new ArrayList<>();
        if (args.containsKey("birthDate")) {
            queryParams.add("birthDate=" + args.get("birthDate"));
        }
        if (args.containsKey("name")) {
            queryParams.add("name=" + args.get("name"));
        }
        if (args.containsKey("ownerId")) {
            queryParams.add("ownerId=" + args.get("ownerId"));
        }
        if (args.containsKey("type")) {
            queryParams.add("type=" + args.get("type"));
        }
                
                String queryString = queryParams.isEmpty() ? "" : "?" + String.join("&", queryParams);
                String url = config.getBaseUrl() + "/api/v2/post_owners_owner_id_pets" + queryString;
                
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
    
    public static MCPServer.Tool createPost_Owners_Owner_Id_PetsTool(MCPServer.APIConfig config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> birthDateProperty = new HashMap<>();
        birthDateProperty.put("type", "string");
        birthDateProperty.put("required", true);
        birthDateProperty.put("description", "Input parameter: The date of birth of the pet.");
        properties.put("birthDate", birthDateProperty);
        Map<String, Object> nameProperty = new HashMap<>();
        nameProperty.put("type", "string");
        nameProperty.put("required", true);
        nameProperty.put("description", "Input parameter: The name of the pet.");
        properties.put("name", nameProperty);
        Map<String, Object> ownerIdProperty = new HashMap<>();
        ownerIdProperty.put("type", "string");
        ownerIdProperty.put("required", true);
        ownerIdProperty.put("description", "The ID of the pet owner.");
        properties.put("ownerId", ownerIdProperty);
        Map<String, Object> typeProperty = new HashMap<>();
        typeProperty.put("type", "string");
        typeProperty.put("required", true);
        typeProperty.put("description", "Input parameter: A pet type.");
        properties.put("type", typeProperty);
        parameters.put("properties", properties);
        
        MCPServer.ToolDefinition definition = new MCPServer.ToolDefinition(
            "post_owners_owner_id_pets",
            "Adds a pet to an owner",
            parameters
        );
        
        return new MCPServer.Tool(definition, getPost_Owners_Owner_Id_PetsHandler(config));
    }
    
}