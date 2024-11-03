# Terraform and Provider Configuration
terraform {
    backend "s3" {}
    required_providers {
        aws = {
        source  = "hashicorp/aws"
        version = "~> 5.0.0"
        }
    }
}

provider "aws" {
  region     = "${var.aws_region}" 
  access_key = "${var.aws_access_key}"       # Replace with your actual access key
  secret_key = "${var.aws_secret_key }"    # Replace with your actual secret key
}