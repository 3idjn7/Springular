# Stage 1: Serve the application from Nginx
FROM nginx:alpine

# Copy the custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy the build output to replace the default nginx contents.
COPY dist/angular-frontend /usr/share/nginx/html

# Expose port 4200
EXPOSE 4200

# Start Nginx and keep it running
CMD ["nginx", "-g", "daemon off;"]
