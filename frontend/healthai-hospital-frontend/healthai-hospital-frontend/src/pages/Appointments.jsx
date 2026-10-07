import { Plus, CalendarDays, LoaderCircle, RefreshCw } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import PageHeader from "../components/PageHeader";
import StatusBadge from "../components/StatusBadge";
import { appointments as mockAppointments } from "../data/mockData";
import { getAppointments } from "../services/appointmentService";

function normalizeAppointment(item) {
  const patient = item.patient?.fullName || item.patient?.name || item.patientName || "—";
  const doctor = item.doctor?.fullName || item.doctor?.name || item.doctorName || "—";
  const department = item.doctor?.department?.name || item.department || "—";
  return {
    id: item.id,
    patient,
    doctor,
    department,
    date: item.appointmentDate || item.date || "—",
    time: item.appointmentTime || item.time || "—",
    status: item.status || "SCHEDULED",
  };
}

function Appointments() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [apiError, setApiError] = useState("");

  const loadAppointments = async () => {
    setLoading(true);
    setApiError("");
    try {
      const data = await getAppointments();
      setItems(Array.isArray(data) ? data.map(normalizeAppointment) : []);
    } catch (error) {
      setItems(mockAppointments);
      setApiError("Live backend unavailable — showing demo appointments. Start Spring Boot to use live data.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadAppointments(); }, []);

  const summary = useMemo(() => ({
    today: items.filter((item) => String(item.date) === new Date().toISOString().slice(0, 10)).length,
    pending: items.filter((item) => String(item.status).toUpperCase() === "PENDING").length,
    completed: items.filter((item) => String(item.status).toUpperCase() === "COMPLETED").length,
  }), [items]);

  return (
    <div>
      <PageHeader title="Appointments" subtitle="Schedule and manage patient appointments." action={<button className="primary-btn"><Plus size={18} />New Appointment</button>} />
      {apiError && <div className="api-notice"><span>{apiError}</span><button onClick={loadAppointments}><RefreshCw size={14} /> Retry</button></div>}

      <div className="appointment-summary">
        <div><CalendarDays size={20} /><span>Today's Appointments</span><strong>{summary.today}</strong></div>
        <div><CalendarDays size={20} /><span>Pending</span><strong>{summary.pending}</strong></div>
        <div><CalendarDays size={20} /><span>Completed</span><strong>{summary.completed}</strong></div>
      </div>

      <section className="panel">
        {loading ? <div className="loading-state"><LoaderCircle className="spin" size={24} /> Loading appointments...</div> : (
          <div className="table-wrapper">
            <table>
              <thead><tr><th>Appointment ID</th><th>Patient</th><th>Doctor</th><th>Department</th><th>Date</th><th>Time</th><th>Status</th></tr></thead>
              <tbody>
                {items.map((appointment) => (
                  <tr key={appointment.id}><td><strong>#{appointment.id}</strong></td><td>{appointment.patient}</td><td>{appointment.doctor}</td><td>{appointment.department}</td><td>{appointment.date}</td><td>{appointment.time}</td><td><StatusBadge status={appointment.status} /></td></tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}

export default Appointments;
