import { Link, useNavigate } from "react-router-dom";
import { Activity, User, Mail, Lock, LoaderCircle } from "lucide-react";
import { useState } from "react";
import { signup } from "../services/authService";

function Signup() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ fullName: "", email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const update = (key, value) => setForm((current) => ({ ...current, [key]: value }));

  const handleSignup = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await signup(form.fullName, form.email, form.password);
      navigate("/");
    } catch (err) {
      setError(err.message || "Unable to create the account.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-left">
        <div className="auth-brand"><div className="logo-icon"><Activity size={23} /></div><span>HealthAI</span></div>
        <div className="auth-content">
          <span className="auth-tag">HEALTHCARE PLATFORM</span>
          <h1>Build a better<br />healthcare experience.</h1>
          <p>Connect hospital operations, patient care and medical management in one modern platform.</p>
        </div>
      </div>

      <div className="auth-right">
        <form className="auth-form reveal-item reveal-visible" onSubmit={handleSignup}>
          <h2>Create account</h2>
          <p>Create your HealthAI account. New registrations are created as patient accounts by the backend.</p>
          {error && <div className="auth-error">{error}</div>}

          <div className="form-group">
            <label>Full Name</label>
            <div className="input-with-icon"><User size={18} /><input value={form.fullName} onChange={(e) => update("fullName", e.target.value)} type="text" placeholder="Enter your name" required /></div>
          </div>
          <div className="form-group">
            <label>Email Address</label>
            <div className="input-with-icon"><Mail size={18} /><input value={form.email} onChange={(e) => update("email", e.target.value)} type="email" placeholder="Enter your email" required /></div>
          </div>
          <div className="form-group">
            <label>Password</label>
            <div className="input-with-icon"><Lock size={18} /><input value={form.password} onChange={(e) => update("password", e.target.value)} type="password" minLength={6} placeholder="Create a password" required /></div>
          </div>

          <button className="primary-btn auth-btn" disabled={loading}>
            {loading ? <><LoaderCircle size={16} className="spin" /> Creating...</> : "Create Account"}
          </button>
          <p className="auth-footer">Already have an account? <Link to="/login">Sign in</Link></p>
        </form>
      </div>
    </div>
  );
}

export default Signup;
