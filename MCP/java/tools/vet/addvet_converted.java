/**
 * MCP Server function for Create a Vet
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

class Post_VetsMCPTool {
    
    public static Function<MCPServer.MCPRequest, MCPServer.MCPToolResult> getPost_VetsHandler(MCPServer.APIConfig config) {
        return (request) -> {
            try {
                Map<String, Object> args = request.getArguments();
                if (args == null) {
                    return new MCPServer.MCPToolResult("Invalid arguments object", true);
                }
                
                List<String> queryParams = new ArrayList<>();
        if (args.containsKey("firstName")) {
            queryParams.add("firstName=" + args.get("firstName"));
        }
        if (args.containsKey("lastName")) {
            queryParams.add("lastName=" + args.get("lastName"));
        }
        if (args.containsKey("id")) {
            queryParams.add("id=" + args.get("id"));
        }
        if (args.containsKey("specialties")) {
            queryParams.add("specialties=" + args.get("specialties"));
        }
                
                String queryString = queryParams.isEmpty() ? "" : "?" + String.join("&", queryParams);
                String url = config.getBaseUrl() + "/api/v2/post_vets" + queryString;
                
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
    
    public static MCPServer.Tool createPost_VetsTool(MCPServer.APIConfig config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> firstNameProperty = new HashMap<>();
        firstNameProperty.put("type", "string");
        firstNameProperty.put("required", true);
        firstNameProperty.put("description", "Input parameter: The first name of the vet.");
        properties.put("firstName", firstNameProperty);
        Map<String, Object> lastNameProperty = new HashMap<>();
        lastNameProperty.put("type", "string");
        lastNameProperty.put("required", true);
        lastNameProperty.put("description", "Input parameter: The last name of the vet.");
        properties.put("lastName", lastNameProperty);
        Map<String, Object> idProperty = new HashMap<>();
        idProperty.put("type", "string");
        idProperty.put("required", true);
        idProperty.put("description", "Input parameter: The ID of the vet.");
        properties.put("id", idProperty);
        Map<String, Object> specialtiesProperty = new HashMap<>();
        specialtiesProperty.put("type", "string");
        specialtiesProperty.put("required", true);
        specialtiesProperty.put("description", "Input parameter: The specialties of the vet.");
        properties.put("specialties", specialtiesProperty);
        parameters.put("properties", properties);
        
        MCPServer.ToolDefinition definition = new MCPServer.ToolDefinition(
            "post_vets",
            "Create a Vet",
            parameters
        );
        
        return new MCPServer.Tool(definition, getPost_VetsHandler(config));
    }
    
}