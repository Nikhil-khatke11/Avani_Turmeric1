# Deployment Guide: Avani Turmeric Website
# Free Hosting: Render.com (Backend) + Netlify (Frontend)

## 🎯 Overview

This guide will help you deploy the Avani Turmeric website using completely free hosting.

**Frontend**: Netlify (Static files)  
**Backend**: Render.com (Java servlets)  
**Database**: Render PostgreSQL (Free tier)

---

## 📦 Part 1: Deploy Backend to Render.com

### Step 1: Create Render Account
1. Go to https://render.com
2. Sign up with GitHub (recommended) or email
3. Verify your email

### Step 2: Create PostgreSQL Database
1. Click "New +" → "PostgreSQL"
2. Set name: `avani-db`
3. Database: `avani_turmeric`
4. User: `avani_user`
5. Region: Choose closest to you
6. **Instance Type**: Free
7. Click "Create Database"
8. **Wait 2-3 minutes** for database to provision

### Step 3: Run Database Schema
1. Once database is ready, click on it
2. Go to "Connect" tab
3. Copy the "PSQL Command"
4. Open terminal and run the command
5. Copy contents of `database/postgresql-schema.sql`
6. Paste and execute in the psql terminal
7. Type `\q` to exit

### Step 4: Deploy Backend Application

**Option A: Deploy from local files**
1. Create a GitHub repository
2. Push your Avani_Turmeric1 project to GitHub
3. In Render, click "New +" → "Web Service"
4. Connect your GitHub repository
5. Configure:
   - **Name**: `avani-backend`
   - **Environment**: Java
   - **Build Command**: (if using Gradle) `./gradlew build` or (if Maven) `mvn clean package`
   - **Start Command**: Based on your JAR name
   - **Instance Type**: Free

**Option B: Deploy without GitHub**
1. Follow Render's manual deployment guide
2. Upload your WAR file

### Step 5: Set Environment Variables
In Render web service settings, add:
- `DB_TYPE` = `postgres`
- `DATABASE_URL` = (auto-filled from database)
- `DB_USER` = (auto-filled from database)
- `DB_PASSWORD` = (auto-filled from database)
- `FRONTEND_URL` = `https://avani-turmeric.netlify.app` (update after Netlify deployment)

### Step 6: Note Your Backend URL
- After deployment completes, Render will give you a URL
- Example: `https://avani-backend.onrender.com`
- **SAVE THIS URL** - you'll need it for frontend

---

## 🌐 Part 2: Deploy Frontend to Netlify

### Step 1: Update Frontend with Backend URL
1. Open `frontend-deploy/config.js`
2. Replace `https://avani-backend.onrender.com` with YOUR actual Render URL
3. Save the file

### Optional: Update other files
- Open `frontend-deploy/cart.html`
- Open `frontend-deploy/products.html`
- Verify they are loading `config.js`

### Step 2: Create Netlify Account
1. Go to https://netlify.com
2. Sign up (email or GitHub)
3. Verify email

### Step 3: Deploy Frontend
1. Click "Add new site" → "Deploy manually"
2. **Drag and drop** the entire `frontend-deploy` folder
3. Wait for deployment (~30 seconds)
4. Netlify will give you a URL like: `https://random-name-12345.netlify.app`

### Step 4: Set Custom Domain (Optional)
1. Click "Domain settings"
2. Click "Edit site name"
3. Change to: `avani-turmeric` or `avani-turmeric-shop`
4. Your site will be: `https://avani-turmeric.netlify.app`

### Step 5: Update Backend CORS
1. Go back to Render dashboard
2. Open your backend service
3. Update environment variable `FRONTEND_URL` to your Netlify URL
4. Save and redeploy

---

## ✅ Part 3: Testing

### Test Backend
1. Visit: `https://your-backend.onrender.com/getProductPrice?productId=1`
2. Should return: `300` or `300.0`

### Test Frontend
1. Visit your Netlify URL
2. Navigate through all pages
3. Try adding product to cart
4. Complete checkout flow
5. Check if order email is received

---

## 🔧 Troubleshooting

### Backend won't start
- Check build logs in Render
- Verify environment variables are set
- Check if database is running

### CORS errors in console
- Verify `FRONTEND_URL` matches your Netlify URL exactly
- Check CorsFilter is deployed
- Redeploy backend after changing env vars

### Database connection fails
- Verify database is running in Render
- Check environment variables
- Ensure schema was run successfully

### Cold start delay
- Free tier sleeps after 15 min inactivity
- First request takes ~30 seconds
- This is normal for free tier

---

## 💰 Costs

✅ **Netlify**: Free forever  
✅ **Render Web Service**: Free  
✅ **Render PostgreSQL**: Free (90 days, then $7/month OR migrate to another free DB)

**Note**: Render's free PostgreSQL expires after 90 days. Before expiry, you can:
1. Migrate to ElephantSQL (free tier)
2. Migrate to Supabase (free tier)
3. Export data and create new free Render database

---

## 📝 Important URLs to Save

- Frontend: `https://avani-turmeric.netlify.app`
- Backend: `https://avani-backend.onrender.com`
- Database: (in Render dashboard)

---

## 🚀 Future Updates

### Update Frontend:
1. Make changes in `frontend-deploy` folder
2. Drag and drop to Netlify (or use Netlify CLI)

### Update Backend:
1. Make changes in your code
2. Push to GitHub
3. Render auto-deploys (if connected to GitHub)

---

## Need Help?

- Render Docs: https://render.com/docs
- Netlify Docs: https://docs.netlify.com  
- Contact: Check your email for support

Good luck! 🌟
