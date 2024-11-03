# Use Node.js for building the React app
FROM node:18 as build

# Set the working directory
WORKDIR /app

# Copy the package.json and install dependencies
# COPY package.json package-lock.json ./
# RUN npm install

# # Copy the rest of the application code
# COPY . .

# # Build the React app for production
# RUN npm run build

# # # Use Nginx to serve the frontend
# FROM nginx:alpine

# # # Copy the build output to Nginx's default public directory
# COPY --from=build /app/build /usr/share/nginx/html

# # Expose port 80
# EXPOSE 3000

# # No CMD required, Nginx runs by default
# CMD ["npm", "start"]

COPY package*.json ./

RUN npm install

COPY . .

# Build the React app
RUN npm run build

# Deploy the React app using the serve package
RUN npm install -g serve

# Expose the port the app runs on
EXPOSE 3000

# Command to run the app
CMD ["serve", "-s", "build", "-l", "3000"]