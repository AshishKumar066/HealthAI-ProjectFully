
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

import "./Navbar.css"

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
      {/* LEFT: Menu + Brand */}
      <div className="navbar-left">
        <button
          type="button"
          className={`menu-toggle ${sidebarMode !== "hidden"
            ? "menu-toggle-active"
            : ""
            }`}
          onClick={onMenuClick}
          aria-label="Toggle sidebar"
          title="Toggle sidebar"
        >
          <MenuIcon size={22} />
        </button>

        <button
          type="button"
          className="navbar-brand"
          onClick={() => navigate("/")}
          aria-label="HealthAI home"
        >
          <span className="navbar-brand-icon">
            <HeartPulse size={27} strokeWidth={2.2} />
          </span>

          <span className="navbar-brand-text">
            <strong>HealthAI</strong>
            <span>Smart Healthcare</span>
          </span>
        </button>
      </div>

      {/* CENTER: Search */}
      <div className="navbar-search">
        <Search size={19} className="search-icon" />

        <input
          type="search"
          placeholder="Search patients, doctors, appointments..."
          aria-label="Search patients, doctors, and appointments"
        />

        <kbd className="search-shortcut">⌘ K</kbd>
      </div>

      {/* RIGHT: Actions */}
      <div className="navbar-actions">
        <button
          type="button"
          className="emergency-navbar-btn"
          onClick={() => navigate("/emergency")}
        >
          <Siren size={19} />
          <span>Emergency</span>
        </button>

        <button
          type="button"
          className="navbar-icon-btn"
          aria-label="Notifications"
          title="Notifications"
        >
          <Bell size={21} />
          <span className="notification-dot" />
        </button>

        <button
          type="button"
          className="navbar-profile"
          onClick={() => navigate("/login")}
          aria-label="Open login"
        >
          <span className="profile-avatar">AU</span>

          <span className="profile-info">
            <strong>Admin User</strong>
            <span>Administrator</span>
          </span>

          <ChevronDown
            size={17}
            className="profile-chevron"
          />
        </button>
      </div>
    </header>
  );
}

export default Navbar;
