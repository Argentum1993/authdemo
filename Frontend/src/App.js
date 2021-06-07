import React, { useState, useEffect } from "react";
import {Switch, Route, Link} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AuthService from "./services/auth.service";
import PrivateRoute from "./components/PrivateRoute";

import Login from "./components/Login";
import Register from "./components/Register";
import Users from "./components/Users";


const App = () => {
  const [currentUser, setCurrentUser] = useState(undefined);

  useEffect(() => {
    const user = AuthService.getCurrentUser();

    if (user) {
      setCurrentUser(user);
    }
  });

  const logOut = () => {
    AuthService.logout();
  };

  return (
      <div>
        <nav className="navbar navbar-expand navbar-dark bg-dark">
          <Link to="/" className="navbar-brand">
            Auth Demo
          </Link>
          {currentUser ? (
          <div className="navbar-nav mr-auto">
            <li className="nav-item">
              <Link to="/users" className="nav-link">
                Users
              </Link>
            </li>
                <li className="nav-item">
                  <Link to="/login" className="nav-link" onClick={logOut}>
                    LogOut
                  </Link>
                </li>
              </div>
          ) : (
              <div className="navbar-nav ml-auto">
                <li className="nav-item">
                  <Link to="/login" className="nav-link">
                    Login
                  </Link>
                </li>

                <li className="nav-item">
                  <Link to="/register" className="nav-link">
                    Sign Up
                  </Link>
                </li>
              </div>
          )}
        </nav>

        <div className="container mt-3">
          <Switch>
            <PrivateRoute exact path={["/", "/users"]}>
               <Users/>
            </PrivateRoute>
            <Route exact path="/login" component={Login} />
            <Route exact path="/register" component={Register} />
          </Switch>
        </div>
      </div>
  );
};

export default App;