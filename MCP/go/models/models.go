package models

import (
	"context"
	"github.com/mark3labs/mcp-go/mcp"
)

type Tool struct {
	Definition mcp.Tool
	Handler    func(ctx context.Context, req mcp.CallToolRequest) (*mcp.CallToolResult, error)
}

// Specialty represents the Specialty schema from the OpenAPI specification
type Specialty struct {
	Id int `json:"id"` // The ID of the specialty.
	Name string `json:"name"` // The name of the specialty.
}

// User represents the User schema from the OpenAPI specification
type User struct {
	Password string `json:"password,omitempty"` // The password
	Roles []Role `json:"roles,omitempty"` // The roles of an user
	Username string `json:"username"` // The username
	Enabled bool `json:"enabled,omitempty"` // Indicates if the user is enabled
}

// ValidationMessage represents the ValidationMessage schema from the OpenAPI specification
type ValidationMessage struct {
	Message string `json:"message"` // The validation message.
}

// PetFields represents the PetFields schema from the OpenAPI specification
type PetFields struct {
	Birthdate string `json:"birthDate"` // The date of birth of the pet.
	Name string `json:"name"` // The name of the pet.
	TypeField PetType `json:"type"` // A pet type.
}

// OwnerFields represents the OwnerFields schema from the OpenAPI specification
type OwnerFields struct {
	Lastname string `json:"lastName"` // The last name of the pet owner.
	Telephone string `json:"telephone"` // The telephone number of the pet owner.
	Address string `json:"address"` // The postal address of the pet owner.
	City string `json:"city"` // The city of the pet owner.
	Firstname string `json:"firstName"` // The first name of the pet owner.
}

// Role represents the Role schema from the OpenAPI specification
type Role struct {
	Name string `json:"name"` // The role's name
}

// VisitFields represents the VisitFields schema from the OpenAPI specification
type VisitFields struct {
	Date string `json:"date,omitempty"` // The date of the visit.
	Description string `json:"description"` // The description for the visit.
}

// VetFields represents the VetFields schema from the OpenAPI specification
type VetFields struct {
	Firstname string `json:"firstName"` // The first name of the vet.
	Lastname string `json:"lastName"` // The last name of the vet.
	Specialties []Specialty `json:"specialties"` // The specialties of the vet.
}

// ProblemDetail represents the ProblemDetail schema from the OpenAPI specification
type ProblemDetail struct {
	Status int `json:"status"` // HTTP status code
	Timestamp string `json:"timestamp"` // The time the error occurred.
	Title string `json:"title"` // The short error title.
	TypeField string `json:"type"` // Full URL that originated the error response.
	Detail string `json:"detail"` // The long error message.
	Schemavalidationerrors []ValidationMessage `json:"schemaValidationErrors"` // Validation errors against the OpenAPI schema.
}

// PetTypeFields represents the PetTypeFields schema from the OpenAPI specification
type PetTypeFields struct {
	Name string `json:"name"` // The name of the pet type.
}

// Vet represents the Vet schema from the OpenAPI specification
type Vet struct {
	Lastname string `json:"lastName"` // The last name of the vet.
	Specialties []Specialty `json:"specialties"` // The specialties of the vet.
	Firstname string `json:"firstName"` // The first name of the vet.
	Id int `json:"id,omitempty"` // The ID of the vet.
}

// Owner represents the Owner schema from the OpenAPI specification
type Owner struct {
	Address string `json:"address"` // The postal address of the pet owner.
	City string `json:"city"` // The city of the pet owner.
	Firstname string `json:"firstName"` // The first name of the pet owner.
	Lastname string `json:"lastName"` // The last name of the pet owner.
	Telephone string `json:"telephone"` // The telephone number of the pet owner.
	Id int `json:"id,omitempty"` // The ID of the pet owner.
	Pets []Pet `json:"pets"` // The pets owned by this individual including any booked vet visits.
}

// Visit represents the Visit schema from the OpenAPI specification
type Visit struct {
	Date string `json:"date,omitempty"` // The date of the visit.
	Description string `json:"description"` // The description for the visit.
	Id int `json:"id"` // The ID of the visit.
	Petid int `json:"petId"` // The ID of the pet.
}

// Pet represents the Pet schema from the OpenAPI specification
type Pet struct {
	Birthdate string `json:"birthDate"` // The date of birth of the pet.
	Name string `json:"name"` // The name of the pet.
	TypeField PetType `json:"type"` // A pet type.
	Id int `json:"id"` // The ID of the pet.
	Ownerid int `json:"ownerId,omitempty"` // The ID of the pet's owner.
	Visits []Visit `json:"visits"` // Vet visit bookings for this pet.
}

// PetType represents the PetType schema from the OpenAPI specification
type PetType struct {
	Name string `json:"name"` // The name of the pet type.
	Id int `json:"id"` // The ID of the pet type.
}
