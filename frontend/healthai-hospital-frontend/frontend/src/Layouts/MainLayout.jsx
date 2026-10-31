
import { useEffect, useState } from "react";
import { Outlet, useLocation } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import ScrollReveal from "../components/ScrollReveal";

function MainLayout() {
  const [sidebarMode, setSidebarMode] = useState("hidden");
  const [mobile, setMobile] = useState(
    () => window.innerWidth <= 900
  );

  const location = useLocation();
  const isDashboard = location.pathname === "/";

  useEffect(() => {
    const onResize = () => {
      setMobile(window.innerWidth <= 900);
      setSidebarMode("hidden");
    };

    window.addEventListener("resize", onResize);

    return () => {
      window.removeEventListener("resize", onResize);
    };
  }, []);

  const toggleSidebar = () => {
    setSidebarMode((current) => {
      if (mobile) {
        return current === "hidden" ? "expanded" : "hidden";
      }

      if (current === "hidden") return "rail";
      if (current === "rail") return "expanded";

      return "rail";
    });
  };

  const hideSidebar = () => {
    setSidebarMode("hidden");
  };

  return (
    <div
      className={`app-layout
        ${sidebarMode === "expanded" ? "sidebar-expanded" : ""}
        ${sidebarMode === "rail" ? "sidebar-compact" : ""}
        ${sidebarMode === "hidden" ? "sidebar-hidden-layout" : ""}
        ${isDashboard ? "dashboard-layout" : ""}
      `}
    >
      <Sidebar
        mode={sidebarMode}
        isMobile={mobile}
        closeSidebar={hideSidebar}
      />

      {mobile && sidebarMode !== "hidden" && (
        <button
          className="sidebar-overlay"
          onClick={hideSidebar}
          aria-label="Close navigation overlay"
        />
      )}

      <div className="main-area">
        <Navbar
          onMenuClick={toggleSidebar}
          sidebarMode={sidebarMode}
          overlay={isDashboard}
        />

        <main className="page-content">
          <ScrollReveal>
            <Outlet />
          </ScrollReveal>
        </main>
      </div>
    </div>
  );
}

export default MainLayout;
