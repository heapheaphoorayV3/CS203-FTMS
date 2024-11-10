resource "aws_cloudwatch_log_group" "ftms_logs" {
  name              = "/ecs/ftms-backend"
  retention_in_days = 30  # Adjust retention period as needed

  tags = {
    Name = "ftms-logs"
  }
}

# Add CloudWatch permissions to the task execution role
resource "aws_iam_role_policy_attachment" "ecs_task_execution_cloudwatch" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchLogsFullAccess"
}