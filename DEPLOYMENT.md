# Deployment Guide - Employee Task Tracker

This guide will help you deploy the Employee Task Tracker application to the cloud for public access.

## Option 1: Deploy to Render (Recommended - Free Tier Available)

### Prerequisites
- GitHub account
- Render account (sign up at https://render.com)

### Steps:

1. **Push your code to GitHub:**
   ```bash
   git add .
   git commit -m "Add cloud deployment configuration"
   git push origin main
   ```

2. **Create a MySQL Database on Render:**
   - Go to https://dashboard.render.com
   - Click "New +" → "PostgreSQL" (Note: Render doesn't offer MySQL on free tier)
   - **Alternative**: Use a free MySQL database from:
     - **Railway** (https://railway.app) - offers MySQL
     - **PlanetScale** (https://planetscale.com) - free MySQL database
     - **Aiven** (https://aiven.io) - free MySQL tier

3. **Deploy the Application:**
   - In Render Dashboard, click "New +" → "Web Service"
   - Connect your GitHub repository
   - Configure:
     - **Name**: employee-task-tracker
     - **Environment**: Java
     - **Build Command**: `mvn clean package`
     - **Start Command**: `java -jar target/dependency/webapp-runner.jar --port $PORT target/*.war`
   - Add Environment Variables:
     - `DB_URL`: Your MySQL connection string
     - `DB_USER`: Your database username
     - `DB_PASS`: Your database password
   - Click "Create Web Service"

4. **Initialize Database:**
   - Once deployed, connect to your cloud MySQL database
   - Run the schema from `src/main/resources/schema.sql`

5. **Access Your App:**
   - Render will provide a public URL like: `https://employee-task-tracker-xxxx.onrender.com`

---

## Option 2: Deploy to Railway (Easiest - MySQL Included)

### Steps:

1. **Push code to GitHub** (if not already done)

2. **Deploy on Railway:**
   - Go to https://railway.app
   - Sign in with GitHub
   - Click "New Project" → "Deploy from GitHub repo"
   - Select your repository
   - Railway will auto-detect it's a Maven project

3. **Add MySQL Database:**
   - In your Railway project, click "New" → "Database" → "Add MySQL"
   - Railway will automatically create environment variables

4. **Configure Environment Variables:**
   - Go to your web service settings → Variables
   - Add:
     - `DB_URL`: `jdbc:mysql://${{MYSQL_PRIVATE_URL}}/employee_task_tracker?useSSL=false&serverTimezone=UTC`
     - `DB_USER`: `${{MYSQLUSER}}`
     - `DB_PASS`: `${{MYSQLPASSWORD}}`

5. **Initialize Database:**
   - Click on MySQL service → "Connect"
   - Run the SQL from `src/main/resources/schema.sql`

6. **Access Your App:**
   - Railway provides a public URL in the deployment settings

---

## Option 3: Deploy to Heroku

### Steps:

1. **Install Heroku CLI:**
   - Download from https://devcenter.heroku.com/articles/heroku-cli

2. **Login and Create App:**
   ```bash
   heroku login
   heroku create employee-task-tracker-app
   ```

3. **Add MySQL Database:**
   ```bash
   heroku addons:create jawsdb:kitefin
   ```

4. **Get Database Credentials:**
   ```bash
   heroku config:get JAWSDB_URL
   ```
   - Parse the URL and set environment variables:
   ```bash
   heroku config:set DB_URL=jdbc:mysql://[host]:[port]/[database]
   heroku config:set DB_USER=[username]
   heroku config:set DB_PASS=[password]
   ```

5. **Deploy:**
   ```bash
   git push heroku main
   ```

6. **Initialize Database:**
   - Connect to JawsDB MySQL and run schema.sql

7. **Open Your App:**
   ```bash
   heroku open
   ```

---

## Quick Start with Railway (Recommended for Beginners)

Railway is the easiest option as it:
- ✅ Automatically detects your project type
- ✅ Provides MySQL database with one click
- ✅ Auto-configures environment variables
- ✅ Offers free tier ($5 credit/month)
- ✅ Provides HTTPS by default

**Steps:**
1. Push code to GitHub
2. Go to railway.app and connect GitHub
3. Select your repository
4. Add MySQL database
5. Run schema.sql in the database
6. Get your public URL!

---

## Important Notes

- **Database Schema**: Don't forget to run `src/main/resources/schema.sql` on your cloud database
- **Environment Variables**: Make sure DB_URL, DB_USER, and DB_PASS are set correctly
- **Free Tier Limitations**: 
  - Render: May sleep after inactivity (takes ~30s to wake up)
  - Railway: $5 credit/month (usually enough for small apps)
  - Heroku: No longer offers free tier
- **HTTPS**: All platforms provide HTTPS by default
- **Custom Domain**: You can add your own domain in platform settings

---

## Troubleshooting

### App won't start:
- Check logs in your platform dashboard
- Verify environment variables are set correctly
- Ensure database is accessible

### Database connection errors:
- Verify DB_URL format is correct for MySQL
- Check if database user has proper permissions
- Ensure schema.sql has been executed

### Build failures:
- Check Java version (should be 11+)
- Verify Maven can download dependencies
- Review build logs for specific errors

---

## Support

For issues, check:
- Platform-specific documentation
- Application logs in dashboard
- Database connection settings
