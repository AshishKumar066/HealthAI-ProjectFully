# HealthAI Hospital Frontend

React + Vite frontend for the HealthAI Hospital Management System.

## What is included

- Same HealthAI dashboard, hospital, doctor, patient, appointment, department, prescription, medical-record, billing, emergency and settings UI from the supplied frontend.
- Desktop sidebar rail: the page remains visible while the sidebar expands/collapses; the content width smoothly adjusts beside it.
- Mobile sidebar drawer with overlay.
- Smooth scroll/reveal animations for cards, forms and page sections.
- Focus animations for form controls.
- Spring Boot API client with JWT token support.
- Login and signup connected to `/api/auth/login` and `/api/auth/register`.
- Doctors and appointments pages connected to the supplied backend, with demo-data fallback if the backend is temporarily unavailable.

## Run

1. Make sure the Spring Boot backend is running on `http://localhost:8080`.
2. Open this folder in VS Code.
3. Install dependencies:

```bash
npm install
```

4. Copy `.env.example` to `.env` if you want to change the API URL.
5. Start the frontend:

```bash
npm run dev
```

Vite normally shows a URL such as `http://localhost:5173`.

## Backend URL

Default:

```text
http://localhost:8080/api
```

To change it:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## Demo login from the supplied Spring Boot DataSeeder

```text
Email: admin@healthai.com
Password: admin123
```

Other seeded accounts may be available in the backend project's `DataSeeder.java`.

## Important

The browser must be allowed by the Spring Boot CORS configuration. If the frontend runs on a different Vite port, add that exact origin to the backend's `app.cors.allowed-origins` property.
