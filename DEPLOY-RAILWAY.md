# Burlington Arcade — Railway Deployment Guide

Deploy the **complete app** (frontend + Spring Boot backend + database) to Railway in ~5 minutes.

Railway gives you **$5 free credit/month** — enough to run this app continuously.

---

## What You're Deploying

Single Railway service hosting:
- Spring Boot backend (port from `$PORT` env)
- Frontend bundled inside the JAR (`/static/index.html`)
- H2 file database (or upgrade to Railway PostgreSQL)
- JWT authentication, signup/login, rate limiting, account lockout

Everything served from **one URL** like `https://burlington-arcade.up.railway.app`.

---

## Method 1: Deploy from GitHub (Recommended)

### Step 1 — Push to GitHub
```bash
cd burlington-arcade
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/burlington-arcade.git
git push -u origin main
```

### Step 2 — Create Railway project
1. Go to **https://railway.com**
2. Click **Start a New Project** → **Deploy from GitHub repo**
3. Authorize Railway to access your GitHub
4. Select the `burlington-arcade` repository
5. Railway will detect the Java/Maven project automatically (via `nixpacks.toml`)

### Step 3 — Add environment variables
In Railway's dashboard, go to **Variables** tab and add:

| Variable                | Value                                          | Notes                                 |
|-------------------------|------------------------------------------------|---------------------------------------|
| `JWT_SECRET`            | *(run `openssl rand -base64 48`)*              | **Required** — strong random key      |
| `JWT_EXPIRATION`        | `3600000`                                      | 1 hour (optional)                     |
| `JWT_REFRESH_EXPIRATION`| `86400000`                                     | 24 hours (optional)                   |
| `H2_CONSOLE_ENABLED`    | `false`                                        | Keep H2 console off in production     |

To generate a strong JWT secret:
```bash
openssl rand -base64 48
```

### Step 4 — Generate a public domain
1. In Railway dashboard, click your service → **Settings** → **Networking**
2. Click **Generate Domain**
3. You'll get a URL like `burlington-arcade-production.up.railway.app`

### Step 5 — Deploy
Railway auto-deploys after every git push. The first build takes ~3 minutes.
Once `Active`, visit the domain — your app is live.

---

## Method 2: Railway CLI

```bash
# Install CLI
npm install -g @railway/cli

# Login
railway login

# Initialize project
cd burlington-arcade
railway init

# Set secrets
railway variables set JWT_SECRET=$(openssl rand -base64 48)
railway variables set H2_CONSOLE_ENABLED=false

# Deploy
railway up

# Open in browser
railway open
```

---

## Optional — Add Railway PostgreSQL (Production)

H2 file mode works but data is lost on container restart unless you mount a volume. For permanent storage, attach Railway's PostgreSQL plugin:

### Step 1 — Add PostgreSQL service
1. In your Railway project, click **+ New** → **Database** → **Add PostgreSQL**
2. Railway provisions a Postgres instance and exposes connection details

### Step 2 — Wire backend to Postgres
Add these env vars to your **backend service** (not the database):

| Variable      | Value                                              |
|---------------|----------------------------------------------------|
| `DATABASE_URL`| `${{Postgres.DATABASE_URL}}` (Railway reference)   |
| `DB_DRIVER`   | `org.postgresql.Driver`                            |
| `DB_DIALECT`  | `org.hibernate.dialect.PostgreSQLDialect`          |
| `DB_USERNAME` | `${{Postgres.PGUSER}}`                             |
| `DB_PASSWORD` | `${{Postgres.PGPASSWORD}}`                         |

> Railway auto-injects these via the **${{ServiceName.VARIABLE}}** reference syntax.

The PostgreSQL JDBC driver is already in the `pom.xml`. Hibernate will create the `users` table on first startup.

### Step 3 — Redeploy
Railway redeploys automatically when env vars change.

---

## Optional — Add Persistent Volume for H2 (without Postgres)

If you want to keep H2 but persist data across restarts:

1. In Railway dashboard → service → **Volumes**
2. Click **+ New Volume**
3. Mount path: `/app/data`
4. Add env var: `SPRING_DATASOURCE_URL=jdbc:h2:file:/app/data/burlington-arcade;DB_CLOSE_ON_EXIT=FALSE`

---

## Verify Deployment

Once live, test the backend:

```bash
# Replace with your Railway URL
RAILWAY_URL=https://burlington-arcade-production.up.railway.app

# 1. Frontend loads
curl -I $RAILWAY_URL/

# 2. Login with seeded user
curl -X POST $RAILWAY_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"salesrep","password":"Burlington@2024"}'

# 3. Signup a new user
curl -X POST $RAILWAY_URL/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"jane.test","email":"jane@test.com","password":"Test@Pass123","fullName":"Jane Test"}'
```

---

## Troubleshooting

**Build fails: "Could not find or load main class"**
→ Check `pom.xml` artifact name matches `arcade-sales-deck-1.0.0.jar` in the start command

**App returns 502 Bad Gateway**
→ The backend may still be starting (~30s on first deploy). Check Railway logs.

**"PORT environment variable not set"**
→ Already handled by `application.properties` (`server.port=${PORT:8080}`)

**CORS errors when calling API**
→ The frontend is served from the same domain as the API, so CORS isn't an issue. If you split them later, update `SecurityConfig.java` allowed origins.

**Out of memory during build**
→ Railway free tier has 512MB build RAM. Try adding `-Xmx256m` to `MAVEN_OPTS` env var.

**Logs show "data/ directory not found"**
→ Add a volume mount (see Persistent Volume section above) or switch to Postgres.

---

## Updating the App

```bash
git add .
git commit -m "Update"
git push
```
Railway auto-rebuilds and redeploys.

---

## Cost Estimate (Railway Free Tier)

- **$5 free credit/month** (renews monthly)
- This app uses ~$3–4/month at idle (always-on)
- PostgreSQL add-on: ~$1/month at small volume
- Total: comfortably within free tier

---

## Files in This Setup

| File              | Purpose                                |
|-------------------|----------------------------------------|
| `nixpacks.toml`   | Railway buildpack config (Java 21 + Maven) |
| `railway.json`    | Build & deploy commands                |
| `Procfile`        | Fallback start command                 |
| `application.properties` | Reads `$PORT`, `$JWT_SECRET` etc. from env |

---

Built with: Spring Boot · JWT · BCrypt · Railway · H2 / PostgreSQL
