# Use the official Node.js 20 image as the base image
FROM node:20

WORKDIR /app

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