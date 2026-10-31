import { useEffect, useRef, useState } from "react";

import {
  MapPin,
  ChevronLeft,
  ChevronRight,
  CalendarPlus,
  Users,
  Stethoscope,
  Bed,
  Activity,
  ArrowUpRight,
} from "lucide-react";

import StatCard from "../components/StatCard";
import StatusBadge from "../components/StatusBadge";
import AIBot from "../components/AIBot/AIBot";

import { getDashboardStats } from "../services/dashboardService";
import { getHospitals } from "../services/hospitalService";


function Dashboard() {
  const [selectedCity, setSelectedCity] = useState("");
  const [hospitalRecords, setHospitalRecords] = useState([]);
  const [hospitalsLoading, setHospitalsLoading] = useState(true);
  const [hospitalsError, setHospitalsError] = useState("");
  const [dashboardStats, setDashboardStats] = useState(null);
  const [statsLoading, setStatsLoading] = useState(false);
  const [statsError, setStatsError] = useState("");

  const hospitalSliderRef = useRef(null);
  const heroRef = useRef(null);

  const cities = [...new Set(hospitalRecords.map((h) => h.city).filter(Boolean))].sort((a, b) => a.localeCompare(b));
  const hospitals = hospitalRecords.filter((hospital) => (hospital.city || "").toLowerCase() === selectedCity.toLowerCase());
  const selectedCityState = hospitals[0]?.city || selectedCity;

  useEffect(() => {
    let active = true;
    const loadHospitals = async () => {
      try {
        setHospitalsLoading(true);
        setHospitalsError("");
        const data = await getHospitals();
        const records = Array.isArray(data) ? data : Array.isArray(data?.content) ? data.content : [];
        if (!active) return;
        setHospitalRecords(records);
        setSelectedCity((current) => current && records.some((h) => (h.city || "").toLowerCase() === current.toLowerCase()) ? current : (records.find((h) => h.city)?.city || ""));
      } catch (error) {
        if (active) setHospitalsError(error?.message || "Unable to load hospitals.");
      } finally {
        if (active) setHospitalsLoading(false);
      }
    };
    loadHospitals();
    return () => { active = false; };
  }, []);


  useEffect(() => {
    const token = localStorage.getItem("healthai_token");

    if (!token) return;

    const loadDashboardStats = async () => {
      try {
        setStatsLoading(true);
        setStatsError("");

        const data = await getDashboardStats();
        console.log("Dashboard Stats:", data);
        setDashboardStats(data);
      } catch (error) {
        console.error("Dashboard stats error:", error);
        setStatsError(
          error?.message || "Unable to load dashboard statistics",
        );
      } finally {
        setStatsLoading(false);
      }
    };

    loadDashboardStats();
  }, []);

  const scrollHospitals = (direction) => {
    if (!hospitalSliderRef.current) return;

    hospitalSliderRef.current.scrollBy({
      left: direction === "left" ? -380 : 380,
      behavior: "smooth",
    });
  };

  useEffect(() => {
    const handleScroll = () => {
      if (!heroRef.current) return;

      const heroHeight = heroRef.current.offsetHeight;
      const scrollPosition = window.scrollY;
      const percentage =
        heroHeight > 0 ? (scrollPosition / heroHeight) * 100 : 0;

      document.body.classList.toggle(
        "dashboard-nav-transparent",
        scrollPosition > 40,
      );

      document.body.classList.toggle(
        "dashboard-nav-hide",
        percentage >= 75,
      );
    };

    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
      document.body.classList.remove("dashboard-nav-transparent");
      document.body.classList.remove("dashboard-nav-hide");
    };
  }, []);

  const totalPatients =
    dashboardStats?.totalPatients ??
    dashboardStats?.patients ??
    dashboardStats?.patientCount ??
    null;

  const totalDoctors =
    dashboardStats?.availableDoctors ??
    dashboardStats?.totalDoctors ??
    dashboardStats?.doctors ??
    dashboardStats?.doctorCount ??
    null;

  const totalAppointments =
    dashboardStats?.totalAppointments ??
    dashboardStats?.appointments ??
    dashboardStats?.appointmentCount ??
    null;

  const formatStatValue = (value) => {
    if (statsLoading) return "...";
    if (value === null || value === undefined) return "—";
    if (typeof value === "number") return value.toLocaleString();
    return value;
  };

  return (
    <div className="dashboard-page">

      <div className="page-3d-bg" aria-hidden="true">
        <div className="bg-glow bg-glow-one"></div>
        <div className="bg-glow bg-glow-two"></div>
        <div className="bg-glow bg-glow-three"></div>
        <div className="bg-floor"></div>

        {["one", "two", "three"].map((n) => (
          <div key={n} className={`bg-cube bg-cube-${n}`}>
            {["f", "b", "r", "l", "t", "d"].map((face) => (
              <span key={face} className={`cube-face cube-${face}`}></span>
            ))}
          </div>
        ))}
      </div>

      {/* HERO BANNER WITH BACKGROUND VIDEO */}
      <section ref={heroRef} className="dashboard-hero">
        <div className="hero-background">
          <video
            className="hero-video"
            autoPlay
            muted
            loop
            playsInline
            preload="metadata"
            aria-hidden="true"
          >
            <source
              src="/videos/healthai-intro.mp4"
              type="video/mp4"
            />
          </video>

          <div className="hero-video-shade"></div>
          <div className="hero-orb hero-orb-one"></div>
          <div className="hero-orb hero-orb-two"></div>
          <div className="hero-grid"></div>
          <div className="hero-pulse"></div>
        </div>

        <div className="hero-content">
          <div className="hero-copy">
            <span className="hero-label">
              <Activity size={15} />
              SMART HEALTHCARE PLATFORM
            </span>

            <h1>
              Healthcare that puts
              <span> you first.</span>
            </h1>

            <p>
              Find nearby hospitals, manage appointments, monitor hospital
              capacity and access essential healthcare information from one
              place.
            </p>

            <div className="hero-actions">
              <a href="/appointments" className="hero-primary-btn">
                <CalendarPlus size={18} />
                Book Appointment
              </a>

              <a href="/emergency" className="hero-secondary-btn">
                Explore Hospitals
                <ArrowUpRight size={17} />
              </a>
            </div>
          </div>


        </div>

        <div className="hero-scroll-indicator">
          <span>Scroll to explore</span>
          <div></div>
        </div>
      </section>

      {/* HEALTHAI GUIDE: keeps the original hero video untouched */}
      <section className="healthai-guide-section" aria-labelledby="healthai-guide-title">
        <div className="healthai-guide-copy">
          <span className="guide-eyebrow"><Activity size={15} /> YOUR HEALTH, SIMPLIFIED</span>
          <h2 id="healthai-guide-title">Care that moves <span>with you.</span></h2>
          <p className="guide-intro">Explore HealthAI in a few seconds. Hover over a guide card to see how each feature can help you take the next step with confidence.</p>
          <div className="guide-feature-list">
            <article className="guide-feature-card">
              <span className="guide-feature-icon"><MapPin size={19} /></span>
              <span><strong>Find nearby care</strong><small>Explore hospitals and available beds by city.</small></span>
              <ArrowUpRight className="guide-feature-arrow" size={17} />
            </article>
            <article className="guide-feature-card">
              <span className="guide-feature-icon"><CalendarPlus size={19} /></span>
              <span><strong>Plan appointments</strong><small>Keep your healthcare visits organised.</small></span>
              <ArrowUpRight className="guide-feature-arrow" size={17} />
            </article>
            <article className="guide-feature-card">
              <span className="guide-feature-icon"><Activity size={19} /></span>
              <span><strong>Stay informed</strong><small>Access health tools and important records in one place.</small></span>
              <ArrowUpRight className="guide-feature-arrow" size={17} />
            </article>
          </div>
        </div>
        {/* <div className="healthai-guide-media">
          <div className="guide-media-heading"><span className="guide-live-dot"></span><span>HEALTHAI • PRODUCT TOUR</span></div>
          <div className="hero-showcase-frame guide-video-frame">
            <video
              className="hero-showcase-video"
              src="/videos/Doctor.mp4"
              autoPlay
              muted
              loop
              playsInline
              preload="metadata"
              controls
              onError={(event) => { event.currentTarget.style.display = "none"; }}
              aria-label="HealthAI healthcare product tour"
            />
            <div className="hero-showcase-fallback">
              <Activity size={30} />
              <strong>HealthAI in action</strong>
              <span>Replace public/videos/Doctor.mp4 with your preferred healthcare showcase video</span>
            </div>
          </div>
          <p className="guide-video-caption">A clearer, more connected healthcare experience.</p>
        </div> */}
        <div className="healthai-guide-media">
          <video
            className="hero-showcase-video"
            src="/videos/Doctor.mp4"
            autoPlay
            muted
            loop
            playsInline
            preload="auto"
            aria-label="HealthAI animated healthcare guide"
          />
        </div>
      </section>

      {/* LOCATION */}
      <section className="location-section">
        <div className="location-content">
          <div>
            <span className="section-eyebrow">YOUR LOCATION</span>
            <h2>Healthcare near you</h2>
            <p>
              Select your city to discover hospitals and healthcare services
              around you.
            </p>
          </div>

          <div className="location-selector">
            <MapPin size={20} />

            <select
              value={selectedCity}
              onChange={(e) => setSelectedCity(e.target.value)}
            >
              <option value="">Select a city</option>
              {cities.map((cityName) => (
                <option key={cityName} value={cityName}>
                  {cityName}
                </option>
              ))}
            </select>

            <span className="location-state">{selectedCityState || "Hospitals from database"}</span>
          </div>
        </div>
      </section>

      {/* STATS */}
      <section className="dashboard-stats">
        <StatCard
          title="Total Patients"
          value={formatStatValue(totalPatients)}
          icon={<Users size={22} />}
          trend={
            statsError ? "Unavailable" : statsLoading ? "Loading" : "Live"
          }
        />

        <StatCard
          title="Available Doctors"
          value={formatStatValue(totalDoctors)}
          icon={<Stethoscope size={22} />}
          trend={
            statsError ? "Unavailable" : statsLoading ? "Loading" : "Live"
          }
        />

        <StatCard
          title="Appointments"
          value={formatStatValue(totalAppointments)}
          icon={<CalendarPlus size={22} />}
          trend={
            statsError ? "Unavailable" : statsLoading ? "Loading" : "Live"
          }
        />

        <StatCard
          title="Available Beds"
          value={hospitalsLoading ? "..." : hospitals.reduce(
            (total, hospital) => total + (Number(hospital.availableBeds) || 0),
            0,
          )}
          icon={<Bed size={22} />}
          trend={hospitalsError ? "Unavailable" : "Database"}
        />
      </section>

      {/* API ERROR MESSAGE */}
      {statsError && (
        <div
          style={{
            margin: "0 auto 20px",
            maxWidth: "1200px",
            padding: "12px 16px",
            borderRadius: "10px",
            background: "#fff4f4",
            border: "1px solid #ffd6d6",
            color: "#b42318",
            fontSize: "14px",
          }}
        >
          Dashboard statistics could not be loaded. Other public dashboard
          features are still available.
        </div>
      )}

      {/* NEAREST HOSPITALS */}
      <section className="hospital-section">
        <div className="section-heading-row">
          <div>
            <span className="section-eyebrow">NEARBY HEALTHCARE</span>
            <h2>Nearest Hospitals</h2>
            <p>
              Top hospitals around {selectedCity}, sorted by distance.
            </p>
          </div>

          <div className="hospital-slider-buttons">
            <button
              onClick={() => scrollHospitals("left")}
              aria-label="Previous hospitals"
            >
              <ChevronLeft size={21} />
            </button>

            <button
              onClick={() => scrollHospitals("right")}
              aria-label="Next hospitals"
            >
              <ChevronRight size={21} />
            </button>
          </div>
        </div>

        {hospitalsError && <div className="hospital-empty-state" role="alert">Hospitals could not be loaded from the backend. {hospitalsError}</div>}
        {hospitalsLoading && <div className="hospital-empty-state">Loading hospitals from the database…</div>}
        {!hospitalsLoading && !hospitalsError && hospitals.length === 0 && (
          <div className="hospital-empty-state">{selectedCity ? `No hospitals are registered in ${selectedCity} yet.` : "No hospitals are registered in the database yet."}</div>
        )}
        <div ref={hospitalSliderRef} className="hospital-slider">
          {hospitals.map((hospital) => (
            <article
              className="hospital-dashboard-card"
              key={hospital.id}
            >
              <div className="hospital-card-top">
                <div className="hospital-icon">
                  <Activity size={23} />
                </div>

                <span className="distance-badge">
                  {hospital.emergencyAvailable ? "Emergency" : "General care"}
                </span>
              </div>

              <div className="hospital-card-content">
                <div className="hospital-rating">{hospital.emergencyAvailable ? "Emergency services" : "Hospital"}</div>

                <h3>{hospital.name}</h3>

                <p>
                  <MapPin size={15} />
                  {hospital.address || hospital.city || "Address not provided"}
                </p>

                <div className="hospital-capacity">
                  <div>
                    <span>Available Beds</span>
                    <strong>{Number(hospital.availableBeds) || 0}</strong>
                  </div>

                  <div>
                    <span>Total Beds</span>
                    <strong>{Number(hospital.totalBeds) || 0}</strong>
                  </div>
                </div>

                <div className="hospital-card-bottom">
                  {hospital.emergencyAvailable ? (
                    <StatusBadge status="Active" />
                  ) : (
                    <span className="normal-status">Regular Care</span>
                  )}

                  <a href={hospital.id ? `/hospitals/${hospital.id}` : "/hospitals"} className="view-hospital-btn">
                    View Details
                    <ArrowUpRight size={15} />
                  </a>
                </div>
              </div>
            </article>
          ))}
        </div>
      </section>

      {/* LOWER DASHBOARD */}
      <section className="dashboard-lower-grid">
        <div className="panel">
          <div className="panel-header">
            <div>
              <h3>Today's Appointments</h3>
              <p>Upcoming patient appointments</p>
            </div>

            <a href="/appointments" className="panel-link">
              View all
            </a>
          </div>

          <div className="appointment-list">
            <p className="hospital-empty-state">Appointment records are available in the authenticated Appointments section. This public landing page does not display patient details.</p>
          </div>
        </div>

        <div className="panel">
          <div className="panel-header">
            <div>
              <h3>Hospital Capacity</h3>
              <p>Current capacity around {selectedCity}</p>
            </div>
          </div>

          <div className="capacity-list">
            {!hospitalsLoading && hospitals.length === 0 && <p className="hospital-empty-state">No capacity data available for this city.</p>}
            {hospitals.slice(0, 4).map((hospital) => {
              const totalBeds = Number(hospital.totalBeds) || 0;
              const availableBeds = Number(hospital.availableBeds) || 0;
              const percentage = totalBeds > 0 ? Math.round((availableBeds / totalBeds) * 100) : 0;

              return (
                <div className="capacity-item" key={hospital.id}>
                  <div className="capacity-info">
                    <strong>{hospital.name}</strong>
                    <span>{availableBeds} beds available</span>
                  </div>

                  <div className="capacity-progress">
                    <div style={{ width: `${percentage}%` }}></div>
                  </div>

                  <b>{percentage}%</b>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* EXISTING AI ASSISTANT */}
      <AIBot />
    </div>
  );
}

export default Dashboard;