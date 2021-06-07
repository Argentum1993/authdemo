import axios from "axios";
import authHeader from "./auth-header";

//const API_URL = "http://w8qy3.mocklab.io/";

const API_URL_PROD = "/authdemo/v1/auth";

const register = (name, email, password) => {
    return axios.post(API_URL_PROD + "/sign-up", {
        name,
        email,
        password,
    });
};

const login = (username, password) => {
    return axios
        .post(API_URL_PROD + "/login", {
            username,
            password,
        })
        .then((response) => {
            if (response.data.accessToken) {
                localStorage.setItem("user", JSON.stringify(response.data));
            }
            return response.data;
        });
};

const logout = () => {
    localStorage.removeItem("user");
    axios.post(API_URL_PROD + "/logout", {},{ headers: authHeader() }).then((response) => {
        return response.data;
    });
    // eslint-disable-next-line no-restricted-globals
    location.reload()
};

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem("user"));
};

export default {
    register,
    login,
    logout,
    getCurrentUser,
};