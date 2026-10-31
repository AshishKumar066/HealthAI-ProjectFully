
import { NavLink } from "react-router-dom";
import {
  LayoutDashboard,
  Users,
  Stethoscope,
  CalendarDays,
  Building2,
  Pill,
  FileText,
  CreditCard,
  Settings,
  X,
} from "lucide-react";

const mainMenu = [
  { name: "Dashboard", path: "/", icon: LayoutDashboard },
  { name: "Patients", path: "/patients", icon: Users },
  { name: "Doctors", path: "/doctors", icon: Stethoscope },
  { name: "Appointments", path: "/appointments", icon: CalendarDays },
  { name: "Departments", path: "/departments", icon: Building2 },
  { name: "Prescriptions", path: "/prescriptions", icon: Pill },
  { name: "Medical Records", path: "/medical-records", icon: FileText },
  { name: "Billing", path: "/billing", icon: CreditCard },
];

const systemMenu = [
  { name: "Settings", path: "/settings", icon: Settings },
];

function Sidebar({
  mode = "hidden",
  isMobile = false,
  closeSidebar,
}) {
  const hidden = mode === "hidden";
  const expanded = mode === "expanded";

  const renderMenu = (items) =>
    items.map(({ name, path, icon: Icon }) => (
      <NavLink
        key={path}
        to={path}
        title={mode === "rail" ? name : undefined}
        className={({ isActive }) =>
          `sidebar-link ${isActive ? "active" : ""}`
        }
        onClick={isMobile ? closeSidebar : undefined}
      >
        <Icon size={19} />
        <span className="sidebar-link-label">{name}</span>
      </NavLink>
    ));

  return (
    <aside
      aria-hidden={hidden}
      className={`sidebar ${hidden
          ? "sidebar-hidden"
          : expanded
            ? "sidebar-open"
            : "sidebar-rail"
        } ${isMobile ? "sidebar-mobile" : ""}`}
    >
      <div className="sidebar-header">
        <div className="sidebar-brand">
          <div className="sidebar-brand-text">
            <strong>HealthAI</strong>
            <span>Healthcare Platform</span>
          </div>
        </div>

        {expanded && (
          <button
            className="sidebar-close"
            onClick={closeSidebar}
            aria-label="Hide sidebar"
            title="Hide sidebar"
          >
            <X size={19} />
          </button>
        )}
      </div>

      <div className="sidebar-content">
        <span className="menu-label">MAIN MENU</span>

        <nav>{renderMenu(mainMenu)}</nav>

        <span className="menu-label system-label">
          SYSTEM
        </span>

        <nav>{renderMenu(systemMenu)}</nav>

        <div className="sidebar-status">
          <div className="status-live-dot" />

          <div>
            <strong>System Online</strong>
            <span>All services operational</span>
          </div>
        </div>
      </div>
    </aside>
  );
}

export default Sidebar;
