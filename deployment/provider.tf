terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0.0"
    }
  }
  
  # Add backend configuration for state management
  backend "s3" {
    bucket         = "${TF_VAR_TFSTATE_BUCKET_NAME}"
    key            = "${TF_VAR_TFSTATE_BUCKET_KEY}"
    region         = "${TF_VAR_AWS_REGION}"
    encrypt        = true
    dynamodb_table = "${TF_VAR_TFSTATE_DYNAMODB_TABLE}"
  }
}

provider "aws" {
  region = var.aws_region
}