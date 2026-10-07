import { Link, useNavigate } from "react-router-dom";
import { Activity, Lock, Mail, LoaderCircle } from "lucide-react";
import { useState } from "react";
import { login } from "../services/authService";

function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("admin@healthai.com");
  const [password, setPassword] = useState("admin123");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await login(email, password);
      navigate("/");
    } catch (err) {
      setError(err.message || "Unable to sign in. Check the backend and credentials.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-left">
        <div className="auth-brand">
          <div className="logo-icon"><Activity size={23} /></div>
          <span>HealthAI</span>
        </div>
        <div className="auth-content">
          <span className="auth-tag">HOSPITAL MANAGEMENT</span>
          <h1>Smarter healthcare.<br />Better management.</h1>
          <p>Manage patients, doctors, appointments, medical records and hospital operations from one powerful platform.</p>
        </div>
      </div>

      <div className="auth-right">
        <form className="auth-form reveal-item reveal-visible" onSubmit={handleLogin}>
          <h2>Welcome back</h2>
          <p>Sign in to your HealthAI account.</p>

          {error && <div className="auth-error">{error}</div>}

          <div className="form-group">
            <label>Email Address</label>
            <div className="input-with-icon">
              <Mail size={18} />
              <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Enter your email" required />
            </div>
          </div>

          <div className="form-group">
            <label>Password</label>
            <div className="input-with-icon">
              <Lock size={18} />
              <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Enter your password" required />
            </div>
          </div>

          <div className="form-options">
            <label className="checkbox-label"><input type="checkbox" /> Remember me</label>
            <span className="muted-link">Forgot password?</span>
          </div>

          <button className="primary-btn auth-btn" disabled={loading}>
            {loading ? <><LoaderCircle size={16} className="spin" /> Signing in...</> : "Sign In"}
          </button>

          <p className="auth-footer">Don't have an account? <Link to="/signup">Create account</Link></p>
        </form>
      </div>
    </div>
  );
}

export default Login;
