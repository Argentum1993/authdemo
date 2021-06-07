import axios from "axios";
import authHeader from "./auth-header";

//const API_URL = "http://w8qy3.mocklab.io/";

const API_URL_PROD = "/authdemo/v1/users"



const getAllUsers = () => {
    return axios.get(API_URL_PROD, { headers: authHeader() });
};

const deleteUsers = (idUsers) => {
    return axios.post(API_URL_PROD + "/delete", idUsers, { headers: authHeader() })
}

const blockUsers = (idUsers) => {
    return axios.post(API_URL_PROD + "/ban", idUsers, { headers: authHeader() })
}

const unBlockUsers = (idUsers) => {
    return axios.post(API_URL_PROD + "/unban", idUsers, { headers: authHeader() })
}

export default {
    unBlockUsers,
    blockUsers,
    deleteUsers,
    getAllUsers
};