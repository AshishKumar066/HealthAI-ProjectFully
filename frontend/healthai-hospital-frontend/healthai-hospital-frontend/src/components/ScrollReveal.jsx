import { useEffect } from "react";

const SELECTORS = [
  ".page-content > *",
  ".page-content .panel",
  ".page-content .stat-card",
  ".page-content .doctor-card",
  ".page-content .department-card",
  ".page-content .appointment-summary > div",
  ".page-content .settings-grid > *",
  ".page-content .emergency-card",
  ".page-content .emergency-hero",
  ".page-content .dashboard-stats > *",
  ".page-content .hospital-dashboard-card",
  ".page-content .dashboard-lower-grid > *",
  ".page-content .form-grid > *",
];

function ScrollReveal({ children }) {
  useEffect(() => {
    const markItems = () => {
      const items = new Set();

      SELECTORS.forEach((selector) => {
        document.querySelectorAll(selector).forEach((element) => items.add(element));
      });

      items.forEach((element) => element.classList.add("reveal-item"));
      return [...items];
    };

    let items = markItems();

    if (!("IntersectionObserver" in window)) {
      items.forEach((item) => item.classList.add("reveal-visible"));
      return undefined;
    }

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("reveal-visible");
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.08, rootMargin: "0px 0px -40px 0px" },
    );

    items.forEach((item) => observer.observe(item));

    const mutationObserver = new MutationObserver(() => {
      const nextItems = markItems();
      nextItems.forEach((item) => {
        if (!item.classList.contains("reveal-visible")) observer.observe(item);
      });
      items = nextItems;
    });

    const page = document.querySelector(".page-content");
    if (page) mutationObserver.observe(page, { childList: true, subtree: true });

    return () => {
      observer.disconnect();
      mutationObserver.disconnect();
    };
  }, []);

  return children;
}

export default ScrollReveal;
