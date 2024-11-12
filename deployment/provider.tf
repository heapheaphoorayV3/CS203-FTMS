terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0.0"
    }
  }
  
  # Add backend configuration for state management
  backend "s3" {}
}

provider "aws" {
  region = var.aws_region
}