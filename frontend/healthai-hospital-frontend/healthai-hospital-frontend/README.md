# HealthAI Hospital Frontend (Lightweight Fixed Build)

This is the React/Vite frontend. `node_modules` is intentionally not included so the ZIP stays small.

## Requirements
- Node.js 18 or newer
- MySQL running with the `hospital_db` database
- The HealthAI Spring Boot backend running on `http://localhost:8080`

## Start the backend first
Run the backend project from STS, or from the backend folder with Windows CMD:

```bat
mvnw.cmd spring-boot:run
```

The backend configuration currently expects MySQL username `root` and password configured in `src/main/resources/application.properties`. Make sure these match your own local MySQL setup. Do not publish real database passwords.

Check backend status at `http://localhost:8080/api/health`. It should return `{"status":"UP"}`.

## Start the frontend
Open CMD in this folder and run:

```bat
npm install
npm run dev
```

Open `http://localhost:5173`.

## Login / registration
- Demo admin login (created by the backend seeder if that user does not already exist): `admin@healthai.com` / `admin123`.
- Public registration requires full name, email, password, and confirm password.
- Backend password rule: 6–8 characters containing uppercase, lowercase, number, and special character.
- After registering, check the **Spring Boot console** for the 6-digit OTP, enter it in the frontend, and then sign in. The backend's `app.email.dev-mode=true` prints OTPs to the console instead of emailing them.
- Newly registered accounts cannot log in until email verification succeeds.

If demo admin login returns 401, the database may already contain an older admin account with a different password. The seeder intentionally does not overwrite existing users. Verify the stored user/password state before resetting anything in the database.

## API URL
The `.env` file uses `VITE_API_BASE_URL=http://localhost:8080/api`. Change it only if your backend is running at another address/port, then restart Vite.
