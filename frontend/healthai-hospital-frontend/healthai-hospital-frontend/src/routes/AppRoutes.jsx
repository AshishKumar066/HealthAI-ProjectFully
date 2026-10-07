import { Routes, Route } from "react-router-dom";

import MainLayout from "../Layouts/MainLayout";

import Dashboard from "../pages/Dashboard";
import Login from "../pages/Login";
import Signup from "../pages/Signup";
import Patients from "../pages/Patients";
import Doctors from "../pages/Doctors";
import Appointments from "../pages/Appointments";
import Departments from "../pages/Departments";
import Prescriptions from "../pages/Prescriptions";
import MedicalRecords from "../pages/MedicalRecords";
import Billing from "../pages/Billing";
import Emergency from "../pages/Emergency";
import Settings from "../pages/Settings";
import NotFound from "../pages/NotFound";

import ProtectedRoute from "../components/ProtectedRoute";

function AppRoutes() {
    return (
        <Routes>

            {/* =========================
                AUTHENTICATION
            ========================== */}

            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />

                // PUBLIC WEBSITE

            <Route element={<MainLayout />}>

                {/* Public Home */}
                <Route path="/" element={<Dashboard />} />

                {/* Public Information */}
                <Route path="/doctors" element={<Doctors />} />
                <Route path="/departments" element={<Departments />} />
                <Route path="/emergency" element={<Emergency />} />

            </Route>

                // PROTECTED / USER AREA
            
            <Route element={<ProtectedRoute />}>

                <Route element={<MainLayout />}>

                    <Route
                        path="/patients"
                        element={<Patients />}
                    />

                    <Route
                        path="/appointments"
                        element={<Appointments />}
                    />

                    <Route
                        path="/prescriptions"
                        element={<Prescriptions />}
                    />

                    <Route
                        path="/medical-records"
                        element={<MedicalRecords />}
                    />

                    <Route
                        path="/billing"
                        element={<Billing />}
                    />

                    <Route
                        path="/settings"
                        element={<Settings />}
                    />

                </Route>

            </Route>


            {/* =========================
                PAGE NOT FOUND
            ========================== */}

            <Route
                path="*"
                element={<NotFound />}
            />

        </Routes>
    );
}

export default AppRoutes;