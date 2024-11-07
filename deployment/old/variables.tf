variable "aws_region" {
  default = "us-east-1"
}

variable "aws_access_key" {}
variable "aws_secret_key" {}

variable "environment" {
  default = "dev"
}

variable "vpc_cidr" {
  default = "10.0.0.0/16"
}

variable "availability_zones" {
  type    = list(string)
  default = ["us-east-1a", "us-east-1b"]
}

variable "frontend_bucket_name" {}
variable "container_image" {}
variable "app_port" {
  default = 8080
}
variable "db_name" {}
variable "db_username" {}
variable "db_password" {}
