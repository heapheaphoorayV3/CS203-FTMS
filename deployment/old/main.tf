module "networking" {
  source = "./modules/networking"
  
  vpc_cidr        = var.vpc_cidr
  environment     = var.environment
  azs             = var.availability_zones
}

module "frontend" {
  source = "./modules/frontend"
  
  bucket_name     = var.frontend_bucket_name
  environment     = var.environment
}

module "backend" {
  source = "./modules/backend"
  
  environment     = var.environment
  vpc_id          = module.networking.vpc_id
  public_subnets  = module.networking.public_subnets
  private_subnets = module.networking.private_subnets
  app_port        = var.app_port
  container_image = var.container_image
  db_endpoint     = module.database.db_endpoint
}

module "database" {
  source = "./modules/database"
  
  environment     = var.environment
  vpc_id          = module.networking.vpc_id
  private_subnets = module.networking.private_subnets
  db_name         = var.db_name
  db_username     = var.db_username
  db_password     = var.db_password
  ecs_sg_id       = module.backend.ecs_security_group_id
}