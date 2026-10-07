import { useEffect, useState } from "react";
import { Outlet } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import ScrollReveal from "../components/ScrollReveal";

function MainLayout() {
  // Desktop: collapsed rail by default. Clicking the menu expands it and the
  // content area smoothly shrinks beside it. Mobile keeps the drawer overlay.
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [mobile, setMobile] = useState(() => window.innerWidth <= 900);

  useEffect(() => {
    const onResize = () => {
      const isMobile = window.innerWidth <= 900;
      setMobile(isMobile);
      if (!isMobile) setSidebarOpen(false);
    };

    window.addEventListener("resize", onResize);
    return () => window.removeEventListener("resize", onResize);
  }, []);

  const toggleSidebar = () => setSidebarOpen((open) => !open);

  return (
    <div className={`app-layout ${sidebarOpen ? "sidebar-expanded" : "sidebar-collapsed"}`}>
      <Sidebar
        isOpen={sidebarOpen}
        isMobile={mobile}
        closeSidebar={() => setSidebarOpen(false)}
      />

      <div className="main-area">
        <Navbar onMenuClick={toggleSidebar} sidebarOpen={sidebarOpen} />

        <main className="page-content">
          <ScrollReveal>
            <Outlet />
          </ScrollReveal>
        </main>
      </div>

      {mobile && sidebarOpen && (
        <button
          className="sidebar-overlay"
          onClick={() => setSidebarOpen(false)}
          aria-label="Close navigation"
        />
      )}
    </div>
  );
}

export default MainLayout;
