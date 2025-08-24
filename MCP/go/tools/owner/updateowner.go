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

func UpdateownerHandler(cfg *config.APIConfig) func(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
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
		// Create properly typed request body using the generated schema
		var requestBody models.OwnerFields
		
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
		url := fmt.Sprintf("%s/owners/%s", cfg.BaseURL, ownerId)
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
		var result models.Owner
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

func CreateUpdateownerTool(cfg *config.APIConfig) models.Tool {
	tool := mcp.NewTool("put_owners_ownerId",
		mcp.WithDescription("Update a pet owner's details"),
		mcp.WithNumber("ownerId", mcp.Required(), mcp.Description("The ID of the pet owner.")),
		mcp.WithString("address", mcp.Required(), mcp.Description("Input parameter: The postal address of the pet owner.")),
		mcp.WithString("city", mcp.Required(), mcp.Description("Input parameter: The city of the pet owner.")),
		mcp.WithString("firstName", mcp.Required(), mcp.Description("Input parameter: The first name of the pet owner.")),
		mcp.WithString("lastName", mcp.Required(), mcp.Description("Input parameter: The last name of the pet owner.")),
		mcp.WithString("telephone", mcp.Required(), mcp.Description("Input parameter: The telephone number of the pet owner.")),
	)

	return models.Tool{
		Definition: tool,
		Handler:    UpdateownerHandler(cfg),
	}
}
