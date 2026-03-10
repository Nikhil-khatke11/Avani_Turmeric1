# Avani Turmeric Backend

E-commerce backend for Avani Turmeric business website.

## Technology Stack
- **Backend:** Java Servlets (Jakarta EE)
- **Database:** PostgreSQL (cloud) / Oracle (local development)
- **Server:** Tomcat/Jetty
- **Deployment:** Render.com

## Features
- Product management
- Shopping cart functionality
- Order processing
- Email notifications
- Customer data management

## API Endpoints

### Products
- `GET /getProductPrice?productId={id}` - Get product price

### Cart
- `POST /addToCart` - Add product to cart
- `GET /viewCart` - View cart items

### Orders
- `POST /placeOrder` - Place order with customer details

## Database Schema

### Tables
- `products` - Product information
- `cart` - Shopping cart items
- `customer_details` - Customer information
- `orders` - Order records

## Environment Variables Required

```
DB_TYPE=postgres
DATABASE_URL=<your-database-url>
DB_USER=<database-username>
DB_PASSWORD=<database-password>
FRONTEND_URL=https://avani-turmeric.netlify.app
```

## Local Development

1. Set up Oracle database locally
2. Run SQL schema from `database/postgresql-schema.sql`
3. Deploy WAR file to Tomcat

## Cloud Deployment (Render)

1. Connect GitHub repository
2. Add PostgreSQL database
3. Set environment variables
4. Deploy

## Frontend

Frontend is deployed separately on Netlify:
**Live URL:** https://avani-turmeric.netlify.app

## License

Private - Avani Turmerics Business
