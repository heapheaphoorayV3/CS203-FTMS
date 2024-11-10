variable "vpc_id" {
  description = "The ID of the existing VPC"
  type        = string
}

variable "public_subnet_1_id" {
  description = "The ID of the public subnet in the first availability zone"
  type        = string
}

variable "public_subnet_2_id" {
  description = "The ID of the public subnet in the second availability zone"
  type        = string
}

variable "aws_account_id" {
  description = "The AWS account ID"
  type        = string
  
}

variable "aws_region" {
  description = "The AWS region"
  type        = string
}

variable "jwt_secret_key" {
  description = "The secret key for JWT token generation"
  type        = string
  
}

variable "db_url" {
  description = "The URL of the database"
  type        = string
  
}

variable "db_username" {
  description = "The username for the database"
  type        = string
  
}

variable "db_password" {
  description = "The password for the database"
  type        = string
  
}

variable "gmail_addr" {
  description = "The email address for sending emails"
  type        = string
  
}

variable "gmail_app_pw" {
  description = "The app password for sending emails"
  type        = string
  
}

variable "frontend_source" {
  description = "The source URL for the frontend"
  type        = string
  
}

variable "custom_header_value" {
  description = "The value for the custom header"
  type        = string
  sensitive   = true  # This ensures the value isn't shown in logs
}

# variable "tfstate_bucket_name" {
#   description = "The name of the S3 bucket for storing Terraform state"
#   type        = string
  
# }

# variable "tfstate_bucket_key" {
#   description = "The key for storing Terraform state in the S3 bucket"
#   type        = string
  
# }

# variable "tfstate_dynamodb_table" {
#   description = "The name of the DynamoDB table for state locking"
#   type        = string
  
# }