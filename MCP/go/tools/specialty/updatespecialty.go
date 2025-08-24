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

func UpdatespecialtyHandler(cfg *config.APIConfig) func(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
	return func(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
		args, ok := request.Params.Arguments.(map[string]any)
		if !ok {
			return mcp.NewToolResultError("Invalid arguments object"), nil
		}
		specialtyIdVal, ok := args["specialtyId"]
		if !ok {
			return mcp.NewToolResultError("Missing required path parameter: specialtyId"), nil
		}
		specialtyId, ok := specialtyIdVal.(string)
		if !ok {
			return mcp.NewToolResultError("Invalid path parameter: specialtyId"), nil
		}
		// Create properly typed request body using the generated schema
		var requestBody models.Specialty
		
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
		url := fmt.Sprintf("%s/specialties/%s", cfg.BaseURL, specialtyId)
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
		var result models.Specialty
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

func CreateUpdatespecialtyTool(cfg *config.APIConfig) models.Tool {
	tool := mcp.NewTool("put_specialties_specialtyId",
		mcp.WithDescription("Update a specialty by ID"),
		mcp.WithNumber("specialtyId", mcp.Required(), mcp.Description("The ID of the specialty.")),
		mcp.WithNumber("id", mcp.Required(), mcp.Description("Input parameter: The ID of the specialty.")),
		mcp.WithString("name", mcp.Required(), mcp.Description("Input parameter: The name of the specialty.")),
	)

	return models.Tool{
		Definition: tool,
		Handler:    UpdatespecialtyHandler(cfg),
	}
}
