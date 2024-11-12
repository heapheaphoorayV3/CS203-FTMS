data "aws_vpc" "existing_vpc" {
  id = var.vpc_id
}

data "aws_subnet" "public_subnet_1" {
  id = var.public_subnet_1_id
}

data "aws_subnet" "public_subnet_2" {
  id = var.public_subnet_2_id
}

data "aws_internet_gateway" "existing_igw" {
  filter {
    name   = "attachment.vpc-id"
    values = [data.aws_vpc.existing_vpc.id]
  }
}

# Retrieve Available Availability Zones
data "aws_availability_zones" "available" {
  state = "available"
}

# Get CloudFront managed prefix list
data "aws_ec2_managed_prefix_list" "cloudfront" {
  name = "com.amazonaws.global.cloudfront.origin-facing"
}

# Update the security group to only allow CloudFront IPs
resource "aws_security_group" "alb_sg" {
  name        = "ftms-alb-sg"
  description = "Security group for ALB"
  vpc_id      = data.aws_vpc.existing_vpc.id

  ingress {
    description     = "Allow traffic from CloudFront"
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    prefix_list_ids = [data.aws_ec2_managed_prefix_list.cloudfront.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "ftms-alb-sg"
  }
}

# Security Group for ECS Tasks (Allow traffic only from ALB)
resource "aws_security_group" "ecs_sg" {
  name        = "ecs-tasks-sg"
  description = "Allow inbound HTTP traffic only from ALB"
  vpc_id      = data.aws_vpc.existing_vpc.id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/24"]  # VPC CIDR for ALB health checks
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "ecs-tasks-sg"
  }
}
