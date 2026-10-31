import { apiRequest } from "./api";

export const getDashboardStats = async () => {
    return await apiRequest("/dashboard/stats");
};
