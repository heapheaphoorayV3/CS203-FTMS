resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecsTaskExecutionRole"
  assume_role_policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": {
          "Service": "ecs-tasks.amazonaws.com"
        },
        "Action": "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# ECS Cluster
resource "aws_ecs_cluster" "main" {
  name = "ftms-cluster"

  tags = {
    Name = "ftms-cluster"
  }
}

# ECS Task Definition for ftms backend (Minimal CPU and Memory)
resource "aws_ecs_task_definition" "web" {
  family                   = "ftms-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 2048   # Smallest possible CPU allocation
  memory                   = 4096   # Smallest possible memory allocation

  container_definitions = jsonencode([
    {
      name  = "ftms-backend"
      image = "${var.aws_account_id}.dkr.ecr.${var.aws_region}.amazonaws.com/ftms-backend:latest"  # Full ECR URI
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
          protocol      = "tcp"
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = "/ecs/ftms-backend"
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
      environment = [
        {
          name  = "JWT_SECRET_KEY"
          value = var.jwt_secret_key  # You'll need to define this variable
        },
        {
          name  = "DB_URL"
          value = var.db_url
        },
        {
          name  = "DB_USERNAME"
          value = var.db_username
        },
        {
          name  = "DB_PASSWORD"
          value = var.db_password
        },
        {
          name  = "GMAIL_ADDR"
          value = var.gmail_addr
        },
        {
          name  = "GMAIL_APP_PW"
          value = var.gmail_app_pw
        },
        {
          name  = "FRONTEND_SOURCE"
          value = var.frontend_source
        }
      ]
    }
  ])

  tags = {
    Name = "ftms-task"
  }

  execution_role_arn = aws_iam_role.ecs_task_execution_role.arn
}

# ECS Service to Run the Backend Container
resource "aws_ecs_service" "web" {
  name            = "ftms-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.web.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [data.aws_subnet.public_subnet_1.id, data.aws_subnet.public_subnet_2.id]
    security_groups  = [aws_security_group.ecs_sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.main_tg.arn
    container_name   = "ftms-backend"
    container_port   = 8080
  }

  # Add force_new_deployment to ensure updates
  force_new_deployment = true

  # Add lifecycle rule to ensure proper cleanup
  lifecycle {
    create_before_destroy = true
  }

  health_check_grace_period_seconds = 60

  tags = {
    Name = "ftms-service"
  }
}