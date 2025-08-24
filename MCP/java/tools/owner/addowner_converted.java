/**
 * MCP Server function for Adds a pet owner
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

class Post_OwnersMCPTool {
    
    public static Function<MCPServer.MCPRequest, MCPServer.MCPToolResult> getPost_OwnersHandler(MCPServer.APIConfig config) {
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
        if (args.containsKey("telephone")) {
            queryParams.add("telephone=" + args.get("telephone"));
        }
        if (args.containsKey("address")) {
            queryParams.add("address=" + args.get("address"));
        }
        if (args.containsKey("city")) {
            queryParams.add("city=" + args.get("city"));
        }
                
                String queryString = queryParams.isEmpty() ? "" : "?" + String.join("&", queryParams);
                String url = config.getBaseUrl() + "/api/v2/post_owners" + queryString;
                
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
    
    public static MCPServer.Tool createPost_OwnersTool(MCPServer.APIConfig config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> firstNameProperty = new HashMap<>();
        firstNameProperty.put("type", "string");
        firstNameProperty.put("required", true);
        firstNameProperty.put("description", "Input parameter: The first name of the pet owner.");
        properties.put("firstName", firstNameProperty);
        Map<String, Object> lastNameProperty = new HashMap<>();
        lastNameProperty.put("type", "string");
        lastNameProperty.put("required", true);
        lastNameProperty.put("description", "Input parameter: The last name of the pet owner.");
        properties.put("lastName", lastNameProperty);
        Map<String, Object> telephoneProperty = new HashMap<>();
        telephoneProperty.put("type", "string");
        telephoneProperty.put("required", true);
        telephoneProperty.put("description", "Input parameter: The telephone number of the pet owner.");
        properties.put("telephone", telephoneProperty);
        Map<String, Object> addressProperty = new HashMap<>();
        addressProperty.put("type", "string");
        addressProperty.put("required", true);
        addressProperty.put("description", "Input parameter: The postal address of the pet owner.");
        properties.put("address", addressProperty);
        Map<String, Object> cityProperty = new HashMap<>();
        cityProperty.put("type", "string");
        cityProperty.put("required", true);
        cityProperty.put("description", "Input parameter: The city of the pet owner.");
        properties.put("city", cityProperty);
        parameters.put("properties", properties);
        
        MCPServer.ToolDefinition definition = new MCPServer.ToolDefinition(
            "post_owners",
            "Adds a pet owner",
            parameters
        );
        
        return new MCPServer.Tool(definition, getPost_OwnersHandler(config));
    }
    
}