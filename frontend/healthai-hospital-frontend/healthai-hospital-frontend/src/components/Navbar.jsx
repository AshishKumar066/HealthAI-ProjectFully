
import { useNavigate } from "react-router-dom";
import {
  Menu,
  Search,
  Bell,
  ChevronDown,
  ChevronRight,
  ChevronLeft,
  HeartPulse,
  Siren,
} from "lucide-react";

function Navbar({
  onMenuClick,
  sidebarMode = "hidden",
  overlay = false,
}) {
  const navigate = useNavigate();

  const MenuIcon =
    sidebarMode === "hidden"
      ? Menu
      : sidebarMode === "rail"
        ? ChevronRight
        : ChevronLeft;

  return (
    <header
      className={`top-navbar ${overlay ? "top-navbar-overlay" : ""
        }`}
    >
      <div className="navbar-left">
        <button
          className={`menu-toggle ${sidebarMode !== "hidden"
              ? "menu-toggle-active"
              : ""
            }`}
          onClick={onMenuClick}
          aria-label="Toggle sidebar"
          title={
            sidebarMode === "hidden"
              ? "Open sidebar"
              : sidebarMode === "rail"
                ? "Expand sidebar"
                : "Collapse sidebar"
          }
        >
          <MenuIcon size={23} />
        </button>

        <button
          type="button"
          className="navbar-brand"
          onClick={() => navigate("/")}
          aria-label="HealthAI home"
        >
          <span className="navbar-brand-icon">
            <HeartPulse size={19} />
          </span>

          <span className="navbar-brand-text">
            <strong>HealthAI</strong>
            <span>Smart Healthcare</span>
          </span>
        </button>
      </div>

      <label className="navbar-search">
        <Search size={18} />
        <input
          type="search"
          placeholder="Search patients, doctors, appointments..."
        />
      </label>

      <div className="navbar-actions">
        <button
          className="emergency-navbar-btn"
          onClick={() => navigate("/emergency")}
        >
          <Siren size={18} />
          <span>Emergency</span>
        </button>

        <button
          className="navbar-icon-btn"
          aria-label="Notifications"
          type="button"
        >
          <Bell size={20} />
          <span className="notification-dot" />
        </button>

        <button
          className="navbar-profile"
          onClick={() => navigate("/login")}
          aria-label="Open login"
          type="button"
        >
          <span className="profile-avatar">AU</span>

          <span className="profile-info">
            <strong>Admin User</strong>
            <span>Administrator</span>
          </span>

          <ChevronDown size={16} />
        </button>
      </div>
    </header>
  );
}

export default Navbar;
