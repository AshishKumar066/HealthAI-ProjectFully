import { Link, useNavigate } from "react-router-dom";
import { Activity, User, Mail, Lock, LoaderCircle, ShieldCheck, ArrowLeft } from "lucide-react";
import { useState } from "react";
import { signup, verifyEmail, resendOtp } from "../services/authService";

function Signup() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ fullName: "", email: "", password: "", confirmPassword: "" });
  const [otp, setOtp] = useState("");
  const [step, setStep] = useState("register");
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");
  const [loading, setLoading] = useState(false);

  const update = (key, value) => setForm((current) => ({ ...current, [key]: value }));

  const handleSignup = async (e) => {
    e.preventDefault();
    setError("");
    setNotice("");
    if (form.password !== form.confirmPassword) {
      setError("Passwords do not match.");
      return;
    }
    if (form.password.length < 6 || form.password.length > 8 ||
        !/[A-Z]/.test(form.password) || !/[a-z]/.test(form.password) ||
        !/[0-9]/.test(form.password) || !/[^A-Za-z0-9]/.test(form.password)) {
      setError("Password must be 6–8 characters and include uppercase, lowercase, number and special character.");
      return;
    }
    setLoading(true);
    try {
      const result = await signup(form.fullName, form.email, form.password, form.confirmPassword);
      setStep("verify");
      setNotice(result?.message || "Account created. Enter the 6-digit OTP shown in the backend console.");
    } catch (err) {
      setError(err.message || "Unable to create the account.");
    } finally {
      setLoading(false);
    }
  };

  const handleVerify = async (e) => {
    e.preventDefault();
    setError("");
    setNotice("");
    if (!/^\d{6}$/.test(otp)) {
      setError("Enter the 6-digit OTP.");
      return;
    }
    setLoading(true);
    try {
      const result = await verifyEmail(form.email, otp);
      setNotice(result?.message || "Email verified. You can now sign in.");
      setTimeout(() => navigate("/login", { state: { email: form.email } }), 900);
    } catch (err) {
      setError(err.message || "Could not verify email. Check the OTP in the backend console.");
    } finally {
      setLoading(false);
    }
  };

  const handleResend = async () => {
    setError("");
    setNotice("");
    setLoading(true);
    try {
      const result = await resendOtp(form.email);
      setNotice(result?.message || "A new OTP was generated. Check the backend console.");
    } catch (err) {
      setError(err.message || "Could not resend OTP.");
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
        <form className="auth-form reveal-item reveal-visible" onSubmit={step === "register" ? handleSignup : handleVerify}>
          {step === "register" ? <>
            <h2>Create account</h2>
            <p>Create your HealthAI account. New registrations are patient accounts and require email verification.</p>
            {error && <div className="auth-error" role="alert">{error}</div>}
            <div className="form-group">
              <label>Full Name</label>
              <div className="input-with-icon"><User size={18} /><input value={form.fullName} onChange={(e) => update("fullName", e.target.value)} type="text" maxLength={100} autoComplete="name" placeholder="Enter your name" required /></div>
            </div>
            <div className="form-group">
              <label>Email Address</label>
              <div className="input-with-icon"><Mail size={18} /><input value={form.email} onChange={(e) => update("email", e.target.value)} type="email" autoComplete="email" placeholder="Enter your email" required /></div>
            </div>
            <div className="form-group">
              <label>Password</label>
              <div className="input-with-icon"><Lock size={18} /><input value={form.password} onChange={(e) => update("password", e.target.value)} type="password" minLength={6} maxLength={8} autoComplete="new-password" placeholder="6–8 characters" required /></div>
              <small className="auth-help">Use uppercase, lowercase, a number and a special character. Maximum 8 characters.</small>
            </div>
            <div className="form-group">
              <label>Confirm Password</label>
              <div className="input-with-icon"><Lock size={18} /><input value={form.confirmPassword} onChange={(e) => update("confirmPassword", e.target.value)} type="password" minLength={6} maxLength={8} autoComplete="new-password" placeholder="Re-enter your password" required /></div>
            </div>
            <button className="primary-btn auth-btn" disabled={loading}>
              {loading ? <><LoaderCircle size={16} className="spin" /> Creating...</> : "Create Account"}
            </button>
            <p className="auth-footer">Already have an account? <Link to="/login">Sign in</Link></p>
          </> : <>
            <h2>Verify your email</h2>
            <p>We created an account for <strong>{form.email}</strong>. For local development, the OTP is printed in your Spring Boot console.</p>
            {notice && <div className="auth-success" role="status">{notice}</div>}
            {error && <div className="auth-error" role="alert">{error}</div>}
            <div className="form-group">
              <label>6-digit verification code</label>
              <div className="input-with-icon"><ShieldCheck size={18} /><input value={otp} onChange={(e) => setOtp(e.target.value.replace(/\D/g, "").slice(0, 6))} type="text" inputMode="numeric" autoComplete="one-time-code" maxLength={6} placeholder="Enter OTP" required /></div>
            </div>
            <button className="primary-btn auth-btn" disabled={loading}>
              {loading ? <><LoaderCircle size={16} className="spin" /> Verifying...</> : "Verify Email"}
            </button>
            <button type="button" className="auth-secondary-btn" onClick={handleResend} disabled={loading}>Resend OTP</button>
            <p className="auth-footer"><button type="button" className="auth-back-btn" onClick={() => { setStep("register"); setError(""); setNotice(""); }}><ArrowLeft size={14} /> Back to registration</button></p>
          </>}
        </form>
      </div>
    </div>
  );
}

export default Signup;
