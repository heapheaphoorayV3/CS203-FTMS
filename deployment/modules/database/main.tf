resource "aws_rds_cluster" "aurora" {
  cluster_identifier     = "${var.environment}-aurora-cluster"
  engine                = "aurora-mysql"
  engine_version        = "5.7.mysql_aurora.2.10.2"
  database_name         = var.db_name
  master_username       = var.db_username
  master_password       = var.db_password
  skip_final_snapshot   = true
  db_subnet_group_name  = aws_db_subnet_group.aurora.name
  vpc_security_group_ids = [aws_security_group.aurora.id]
}

resource "aws_rds_cluster_instance" "aurora" {
  count               = 2
  identifier          = "${var.environment}-aurora-instance-${count.index}"
  cluster_identifier  = aws_rds_cluster.aurora.id
  instance_class      = "db.t3.medium"
  engine              = aws_rds_cluster.aurora.engine
  engine_version      = aws_rds_cluster.aurora.engine_version
}

resource "aws_db_subnet_group" "aurora" {
  name       = "${var.environment}-aurora-subnet-group"
  subnet_ids = var.private_subnets
}

resource "aws_security_group" "aurora" {
  name        = "${var.environment}-aurora-sg"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [var.ecs_sg_id]
  }
}