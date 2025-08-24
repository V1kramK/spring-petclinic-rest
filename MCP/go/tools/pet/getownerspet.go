package tools

import (
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"

	"github.com/spring-petclinic/mcp-server/config"
	"github.com/spring-petclinic/mcp-server/models"
	"github.com/mark3labs/mcp-go/mcp"
)

func GetownerspetHandler(cfg *config.APIConfig) func(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
	return func(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
		args, ok := request.Params.Arguments.(map[string]any)
		if !ok {
			return mcp.NewToolResultError("Invalid arguments object"), nil
		}
		ownerIdVal, ok := args["ownerId"]
		if !ok {
			return mcp.NewToolResultError("Missing required path parameter: ownerId"), nil
		}
		ownerId, ok := ownerIdVal.(string)
		if !ok {
			return mcp.NewToolResultError("Invalid path parameter: ownerId"), nil
		}
		petIdVal, ok := args["petId"]
		if !ok {
			return mcp.NewToolResultError("Missing required path parameter: petId"), nil
		}
		petId, ok := petIdVal.(string)
		if !ok {
			return mcp.NewToolResultError("Invalid path parameter: petId"), nil
		}
		url := fmt.Sprintf("%s/owners/%s/pets/%s", cfg.BaseURL, ownerId, petId)
		req, err := http.NewRequest("GET", url, nil)
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

func CreateGetownerspetTool(cfg *config.APIConfig) models.Tool {
	tool := mcp.NewTool("get_owners_ownerId_pets_petId",
		mcp.WithDescription("Get a pet by ID"),
		mcp.WithNumber("ownerId", mcp.Required(), mcp.Description("The ID of the pet owner.")),
		mcp.WithNumber("petId", mcp.Required(), mcp.Description("The ID of the pet.")),
	)

	return models.Tool{
		Definition: tool,
		Handler:    GetownerspetHandler(cfg),
	}
}
