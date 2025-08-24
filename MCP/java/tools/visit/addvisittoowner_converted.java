/**
 * MCP Server function for Adds a vet visit
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

class Post_Owners_Owner_Id_Pets_Pet_Id_VisitsMCPTool {
    
    public static Function<MCPServer.MCPRequest, MCPServer.MCPToolResult> getPost_Owners_Owner_Id_Pets_Pet_Id_VisitsHandler(MCPServer.APIConfig config) {
        return (request) -> {
            try {
                Map<String, Object> args = request.getArguments();
                if (args == null) {
                    return new MCPServer.MCPToolResult("Invalid arguments object", true);
                }
                
                List<String> queryParams = new ArrayList<>();
        if (args.containsKey("description")) {
            queryParams.add("description=" + args.get("description"));
        }
        if (args.containsKey("date")) {
            queryParams.add("date=" + args.get("date"));
        }
        if (args.containsKey("ownerId")) {
            queryParams.add("ownerId=" + args.get("ownerId"));
        }
        if (args.containsKey("petId")) {
            queryParams.add("petId=" + args.get("petId"));
        }
                
                String queryString = queryParams.isEmpty() ? "" : "?" + String.join("&", queryParams);
                String url = config.getBaseUrl() + "/api/v2/post_owners_owner_id_pets_pet_id_visits" + queryString;
                
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
    
    public static MCPServer.Tool createPost_Owners_Owner_Id_Pets_Pet_Id_VisitsTool(MCPServer.APIConfig config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> descriptionProperty = new HashMap<>();
        descriptionProperty.put("type", "string");
        descriptionProperty.put("required", true);
        descriptionProperty.put("description", "Input parameter: The description for the visit.");
        properties.put("description", descriptionProperty);
        Map<String, Object> dateProperty = new HashMap<>();
        dateProperty.put("type", "string");
        dateProperty.put("required", false);
        dateProperty.put("description", "Input parameter: The date of the visit.");
        properties.put("date", dateProperty);
        Map<String, Object> ownerIdProperty = new HashMap<>();
        ownerIdProperty.put("type", "string");
        ownerIdProperty.put("required", true);
        ownerIdProperty.put("description", "The ID of the pet owner.");
        properties.put("ownerId", ownerIdProperty);
        Map<String, Object> petIdProperty = new HashMap<>();
        petIdProperty.put("type", "string");
        petIdProperty.put("required", true);
        petIdProperty.put("description", "The ID of the pet.");
        properties.put("petId", petIdProperty);
        parameters.put("properties", properties);
        
        MCPServer.ToolDefinition definition = new MCPServer.ToolDefinition(
            "post_owners_owner_id_pets_pet_id_visits",
            "Adds a vet visit",
            parameters
        );
        
        return new MCPServer.Tool(definition, getPost_Owners_Owner_Id_Pets_Pet_Id_VisitsHandler(config));
    }
    
}