package tools

import (
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"bytes"

	"github.com/spring-petclinic/mcp-server/config"
	"github.com/spring-petclinic/mcp-server/models"
	"github.com/mark3labs/mcp-go/mcp"
)

func UpdatepetHandler(cfg *config.APIConfig) func(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
	return func(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
		args, ok := request.Params.Arguments.(map[string]any)
		if !ok {
			return mcp.NewToolResultError("Invalid arguments object"), nil
		}
		petIdVal, ok := args["petId"]
		if !ok {
			return mcp.NewToolResultError("Missing required path parameter: petId"), nil
		}
		petId, ok := petIdVal.(string)
		if !ok {
			return mcp.NewToolResultError("Invalid path parameter: petId"), nil
		}
		// Create properly typed request body using the generated schema
		var requestBody models.Pet
		
		// Optimized: Single marshal/unmarshal with JSON tags handling field mapping
		if argsJSON, err := json.Marshal(args); err == nil {
			if err := json.Unmarshal(argsJSON, &requestBody); err != nil {
				return mcp.NewToolResultError(fmt.Sprintf("Failed to convert arguments to request type: %v", err)), nil
			}
		} else {
			return mcp.NewToolResultError(fmt.Sprintf("Failed to marshal arguments: %v", err)), nil
		}
		
		bodyBytes, err := json.Marshal(requestBody)
		if err != nil {
			return mcp.NewToolResultErrorFromErr("Failed to encode request body", err), nil
		}
		url := fmt.Sprintf("%s/pets/%s", cfg.BaseURL, petId)
		req, err := http.NewRequest("PUT", url, bytes.NewBuffer(bodyBytes))
		req.Header.Set("Content-Type", "application/json")
		if err != nil {
			return mcp.NewToolResultErrorFromErr("Failed to create request", err), nil
		}
		// No authentication required for this endpoint
		req.Header.Set("Accept", "application/json")

		resp, err := http.DefaultClient.Do(req)
		if err != nil {
			return mcp.NewToolResultErrorFromErr("Request failed", err), nil
		}
		defer resp.Body.Close()

		body, err := io.ReadAll(resp.Body)
		if err != nil {
			return mcp.NewToolResultErrorFromErr("Failed to read response body", err), nil
		}

		if resp.StatusCode >= 400 {
			return mcp.NewToolResultError(fmt.Sprintf("API error: %s", body)), nil
		}
		// Use properly typed response
		var result models.Pet
		if err := json.Unmarshal(body, &result); err != nil {
			// Fallback to raw text if unmarshaling fails
			return mcp.NewToolResultText(string(body)), nil
		}

		prettyJSON, err := json.MarshalIndent(result, "", "  ")
		if err != nil {
			return mcp.NewToolResultErrorFromErr("Failed to format JSON", err), nil
		}

		return mcp.NewToolResultText(string(prettyJSON)), nil
	}
}

func CreateUpdatepetTool(cfg *config.APIConfig) models.Tool {
	tool := mcp.NewTool("put_pets_petId",
		mcp.WithDescription("Update a pet by ID"),
		mcp.WithNumber("petId", mcp.Required(), mcp.Description("The ID of the pet.")),
		mcp.WithString("birthDate", mcp.Description("Input parameter: The date of birth of the pet.")),
		mcp.WithString("name", mcp.Description("Input parameter: The name of the pet.")),
		mcp.WithObject("type", mcp.Description("Input parameter: A pet type.")),
		mcp.WithNumber("id", mcp.Description("Input parameter: The ID of the pet.")),
		mcp.WithNumber("ownerId", mcp.Description("Input parameter: The ID of the pet's owner.")),
		mcp.WithArray("visits", mcp.Description("Input parameter: Vet visit bookings for this pet.")),
	)

	return models.Tool{
		Definition: tool,
		Handler:    UpdatepetHandler(cfg),
	}
}
