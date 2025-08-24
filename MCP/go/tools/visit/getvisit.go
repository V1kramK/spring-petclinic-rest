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

func GetvisitHandler(cfg *config.APIConfig) func(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
	return func(ctx context.Context, request mcp.CallToolRequest) (*mcp.CallToolResult, error) {
		args, ok := request.Params.Arguments.(map[string]any)
		if !ok {
			return mcp.NewToolResultError("Invalid arguments object"), nil
		}
		visitIdVal, ok := args["visitId"]
		if !ok {
			return mcp.NewToolResultError("Missing required path parameter: visitId"), nil
		}
		visitId, ok := visitIdVal.(string)
		if !ok {
			return mcp.NewToolResultError("Invalid path parameter: visitId"), nil
		}
		url := fmt.Sprintf("%s/visits/%s", cfg.BaseURL, visitId)
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
		var result models.Visit
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

func CreateGetvisitTool(cfg *config.APIConfig) models.Tool {
	tool := mcp.NewTool("get_visits_visitId",
		mcp.WithDescription("Get a visit by ID"),
		mcp.WithNumber("visitId", mcp.Required(), mcp.Description("The ID of the visit.")),
	)

	return models.Tool{
		Definition: tool,
		Handler:    GetvisitHandler(cfg),
	}
}
