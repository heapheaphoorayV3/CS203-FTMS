# Application Load Balancer (ALB) across two AZs
resource "aws_lb" "main_alb" {
  name               = "ftms-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = [data.aws_subnet.public_subnet_1.id, data.aws_subnet.public_subnet_2.id]

  tags = {
    Name = "ftms-alb"
  }
}

# ALB Listener for HTTP on Port 80
resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.main_alb.arn
  port              = 8080
  protocol          = "HTTP"

  # default_action {
  #   type             = "forward"
  #   target_group_arn = aws_lb_target_group.main_tg.arn
  # }

  # Add these if you want to verify requests are coming from CloudFront
  # default_action {
  #   type = "fixed-response"
  #   fixed_response {
  #     content_type = "text/plain"
  #     message_body = "OK"
  #     status_code  = "200"
  #   }
  # }
  default_action {
    type = "fixed-response"
    fixed_response {
      content_type = "text/plain"
      message_body = "Invalid request"
      status_code  = "403"
    }
  }
}

# ALB Target Group for ECS Tasks
resource "aws_lb_target_group" "main_tg" {
  name        = "ftms-alb-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.existing_vpc.id
  target_type = "ip"  # Use "ip" for ECS tasks on Fargate

health_check {
    enabled             = true
    healthy_threshold   = 3          # Increased from 2
    interval            = 60
    matcher            = "200-299"
    path               = "/health/simple"
    port               = 8080
    protocol           = "HTTP"
    timeout            = 30          # Increased from 5
    unhealthy_threshold = 3          # Increased from 2
}

  tags = {
    Name = "ftms-alb-tg"
  }
}

output "alb_dns_name" {
  value = aws_lb.main_alb.dns_name
}

resource "aws_security_group_rule" "alb_to_ecs" {
  type                     = "ingress"
  from_port               = 8080
  to_port                 = 8080
  protocol                = "tcp"
  source_security_group_id = aws_security_group.alb_sg.id
  security_group_id       = aws_security_group.ecs_sg.id  # Your ECS tasks security group
}

# Add listener rule to check for CloudFront custom header
resource "aws_lb_listener_rule" "cloudfront_check" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 1

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.main_tg.arn
  }

  condition {
    http_header {
      http_header_name = "X-Custom-Header"
      values           = [var.custom_header_value]
    }
  }
}

# Add CORS preflight handling
resource "aws_lb_listener_rule" "cors_preflight" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 2

  action {
    type = "fixed-response"
    fixed_response {
      content_type = "text/plain"
      message_body = "OK"
      status_code  = "200"
    }
  }

  condition {
    http_request_method {
      values = ["OPTIONS"]
    }
  }
}