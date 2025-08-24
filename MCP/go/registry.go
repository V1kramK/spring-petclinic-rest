package main

import (
	"github.com/spring-petclinic/mcp-server/config"
	"github.com/spring-petclinic/mcp-server/models"
	tools_pettypes "github.com/spring-petclinic/mcp-server/tools/pettypes"
	tools_pet "github.com/spring-petclinic/mcp-server/tools/pet"
	tools_specialty "github.com/spring-petclinic/mcp-server/tools/specialty"
	tools_visit "github.com/spring-petclinic/mcp-server/tools/visit"
	tools_user "github.com/spring-petclinic/mcp-server/tools/user"
	tools_vet "github.com/spring-petclinic/mcp-server/tools/vet"
	tools_failing "github.com/spring-petclinic/mcp-server/tools/failing"
	tools_owner "github.com/spring-petclinic/mcp-server/tools/owner"
)

func GetAll(cfg *config.APIConfig) []models.Tool {
	return []models.Tool{
		tools_pettypes.CreateDeletepettypeTool(cfg),
		tools_pettypes.CreateGetpettypeTool(cfg),
		tools_pettypes.CreateUpdatepettypeTool(cfg),
		tools_pet.CreateAddpettoownerTool(cfg),
		tools_pet.CreateGetownerspetTool(cfg),
		tools_pet.CreateUpdateownerspetTool(cfg),
		tools_specialty.CreateListspecialtiesTool(cfg),
		tools_specialty.CreateAddspecialtyTool(cfg),
		tools_visit.CreateListvisitsTool(cfg),
		tools_visit.CreateAddvisitTool(cfg),
		tools_pet.CreateListpetsTool(cfg),
		tools_pet.CreateDeletepetTool(cfg),
		tools_pet.CreateGetpetTool(cfg),
		tools_pet.CreateUpdatepetTool(cfg),
		tools_visit.CreateDeletevisitTool(cfg),
		tools_visit.CreateGetvisitTool(cfg),
		tools_visit.CreateUpdatevisitTool(cfg),
		tools_user.CreateAdduserTool(cfg),
		tools_vet.CreateListvetsTool(cfg),
		tools_vet.CreateAddvetTool(cfg),
		tools_pettypes.CreateListpettypesTool(cfg),
		tools_pettypes.CreateAddpettypeTool(cfg),
		tools_vet.CreateGetvetTool(cfg),
		tools_vet.CreateUpdatevetTool(cfg),
		tools_vet.CreateDeletevetTool(cfg),
		tools_failing.CreateFailingrequestTool(cfg),
		tools_owner.CreateDeleteownerTool(cfg),
		tools_owner.CreateGetownerTool(cfg),
		tools_owner.CreateUpdateownerTool(cfg),
		tools_visit.CreateAddvisittoownerTool(cfg),
		tools_specialty.CreateDeletespecialtyTool(cfg),
		tools_specialty.CreateGetspecialtyTool(cfg),
		tools_specialty.CreateUpdatespecialtyTool(cfg),
		tools_owner.CreateAddownerTool(cfg),
		tools_owner.CreateListownersTool(cfg),
	}
}
