import React from 'react';
import { Route, Redirect } from 'react-router-dom';

import AuthService from "../services/auth.service";

const PrivateRoute = ({ children, ...rest }) => {
    const currentUser = AuthService.getCurrentUser();
    return (
        <Route
            {...rest}
            render={({location}) =>
                currentUser ? (
                    children
                ) : (
                    <Redirect
                        to={{
                            pathname: "/login",
                            state: {from: location}
                        }}
                    />
                )
            }
        />
    );
}
export default PrivateRoute;