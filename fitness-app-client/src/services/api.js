import axios from "axios";

const API_URL = 'https://localhost:8080/api';

const api = axios.create({
    baseURL: API_URL
});

api.interceptors.request.use((config) =>{
    const userId = localStorage.getItem('userId');
    if(userId){
        config.headers['X-User-Id'] = userId; 
    }

    return config;
});

export const getActivities = () => api.get('/activities');
export const addActivity = (activity) => api.post('/activity', activity);
export const getActivityDetails = () => api.get('/recommendations/activity/${activityId}');

