import { Plus, Search, MoreVertical, LoaderCircle, RefreshCw } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import PageHeader from "../components/PageHeader";
import StatusBadge from "../components/StatusBadge";
import { doctors as mockDoctors } from "../data/mockData";
import { getDoctors } from "../services/doctorService";

function normalizeDoctor(doctor) {
  const department = typeof doctor.department === "string" ? doctor.department : doctor.department?.name;
  const availability = doctor.availability || "AVAILABLE";
  return {
    id: doctor.id,
    name: doctor.fullName || doctor.name || "Doctor",
    specialization: doctor.specialization || "General Physician",
    department: department || "General",
    experience: doctor.experience || (doctor.experienceYears != null ? `${doctor.experienceYears} Years` : "—"),
    status: doctor.status || availability.replaceAll("_", " "),
  };
}

function Doctors() {
  const [doctors, setDoctors] = useState([]);
  const [query, setQuery] = useState("");
  const [department, setDepartment] = useState("All Departments");
  const [loading, setLoading] = useState(true);
  const [apiError, setApiError] = useState("");

  const loadDoctors = async () => {
    setLoading(true);
    setApiError("");
    try {
      const data = await getDoctors({ q: query || undefined });
      setDoctors(Array.isArray(data) ? data.map(normalizeDoctor) : []);
    } catch (error) {
      setDoctors(mockDoctors);
      setApiError("Live backend unavailable — showing demo data. Start Spring Boot to use live doctors.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(loadDoctors, 250);
    return () => clearTimeout(timer);
  }, [query]);

  const filteredDoctors = useMemo(() => {
    if (department === "All Departments") return doctors;
    return doctors.filter((doctor) => doctor.department === department);
  }, [doctors, department]);

  return (
    <div>
      <PageHeader title="Doctors" subtitle="Manage doctors, specializations and availability." action={<button className="primary-btn"><Plus size={18} />Add Doctor</button>} />

      {apiError && <div className="api-notice"><span>{apiError}</span><button onClick={loadDoctors}><RefreshCw size={14} /> Retry</button></div>}

      <section className="panel">
        <div className="toolbar">
          <div className="search-input"><Search size={18} /><input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search doctor..." /></div>
          <select className="filter-select" value={department} onChange={(e) => setDepartment(e.target.value)}>
            <option>All Departments</option><option>Cardiology</option><option>Neurology</option><option>Orthopedics</option><option>Dermatology</option>
          </select>
        </div>

        {loading ? (
          <div className="loading-state"><LoaderCircle className="spin" size={24} /> Loading doctors...</div>
        ) : (
          <div className="doctor-grid">
            {filteredDoctors.map((doctor) => (
              <div className="doctor-card" key={doctor.id}>
                <div className="doctor-card-top">
                  <div className="doctor-avatar">{doctor.name.replace(/^Dr\.\s*/, "").split(" ").map((word) => word[0]).join("")}</div>
                  <button className="more-btn" aria-label="Doctor actions"><MoreVertical size={18} /></button>
                </div>
                <h3>{doctor.name}</h3>
                <p>{doctor.specialization}</p>
                <div className="doctor-info"><span>Department</span><strong>{doctor.department}</strong></div>
                <div className="doctor-info"><span>Experience</span><strong>{doctor.experience}</strong></div>
                <div className="doctor-footer"><StatusBadge status={doctor.status} /><button className="outline-btn">View Profile</button></div>
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

export default Doctors;
