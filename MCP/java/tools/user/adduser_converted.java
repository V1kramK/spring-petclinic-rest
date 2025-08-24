/**
 * MCP Server function for Create a user
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

class Post_UsersMCPTool {
    
    public static Function<MCPServer.MCPRequest, MCPServer.MCPToolResult> getPost_UsersHandler(MCPServer.APIConfig config) {
        return (request) -> {
            try {
                Map<String, Object> args = request.getArguments();
                if (args == null) {
                    return new MCPServer.MCPToolResult("Invalid arguments object", true);
                }
                
                List<String> queryParams = new ArrayList<>();
        if (args.containsKey("username")) {
            queryParams.add("username=" + args.get("username"));
        }
        if (args.containsKey("password")) {
            queryParams.add("password=" + args.get("password"));
        }
        if (args.containsKey("enabled")) {
            queryParams.add("enabled=" + args.get("enabled"));
        }
        if (args.containsKey("roles")) {
            queryParams.add("roles=" + args.get("roles"));
        }
                
                String queryString = queryParams.isEmpty() ? "" : "?" + String.join("&", queryParams);
                String url = config.getBaseUrl() + "/api/v2/post_users" + queryString;
                
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
    
    public static MCPServer.Tool createPost_UsersTool(MCPServer.APIConfig config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> usernameProperty = new HashMap<>();
        usernameProperty.put("type", "string");
        usernameProperty.put("required", true);
        usernameProperty.put("description", "Input parameter: The username");
        properties.put("username", usernameProperty);
        Map<String, Object> passwordProperty = new HashMap<>();
        passwordProperty.put("type", "string");
        passwordProperty.put("required", false);
        passwordProperty.put("description", "Input parameter: The password");
        properties.put("password", passwordProperty);
        Map<String, Object> enabledProperty = new HashMap<>();
        enabledProperty.put("type", "string");
        enabledProperty.put("required", false);
        enabledProperty.put("description", "Input parameter: Indicates if the user is enabled");
        properties.put("enabled", enabledProperty);
        Map<String, Object> rolesProperty = new HashMap<>();
        rolesProperty.put("type", "string");
        rolesProperty.put("required", false);
        rolesProperty.put("description", "Input parameter: The roles of an user");
        properties.put("roles", rolesProperty);
        parameters.put("properties", properties);
        
        MCPServer.ToolDefinition definition = new MCPServer.ToolDefinition(
            "post_users",
            "Create a user",
            parameters
        );
        
        return new MCPServer.Tool(definition, getPost_UsersHandler(config));
    }
    
}