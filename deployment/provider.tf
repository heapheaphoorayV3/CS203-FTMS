terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0.0"
    }
  }
  
  # Add backend configuration for state management
  backend "s3" {
    bucket         = var.tfstate_bucket_name
    key            = var.tfstate_bucket_key
    region         = var.aws_region
    encrypt        = true
    dynamodb_table = var.tfstate_dynamodb_table
  }
}

provider "aws" {
  region = var.aws_region
}