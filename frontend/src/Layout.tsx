
import React, { useContext } from "react";
import "./App.css";
import { Outlet, Link, NavigateFunction, useNavigate } from "react-router-dom";
import Button from "./components/buttons/Button";
import ApiHandler, { ApiError } from "./helper/ApiHandler";
import { API_CONTROLLER } from "./config/ApiEndpoints";
import { AxiosResponse, HttpStatusCode } from "axios";
import { API_KEY_STRING, USER_DETAILS_STRING } from "./config/GlobalDatas";
import { AuthContext } from "./context/AuthContext";
import { Auth, AuthUser } from "./model/Auth";


const Layout = () => {
    const { auth, setAuth } = useContext(AuthContext);
    const navigate: NavigateFunction = useNavigate();

    const logoutFn = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.USER, "logout", logoutResponseFn);
        } catch (error) {
            const ERR: ApiError = error as ApiError;

            alert(ERR.getMessage);
        }
    }

    function logoutResponseFn(response: AxiosResponse<any, any>) {
        //if (response.status === HttpStatusCode.Ok) {
            setAuth(null);
            
            localStorage.removeItem(API_KEY_STRING);
            localStorage.removeItem(USER_DETAILS_STRING);
            
            navigate("/");
            console.log("sikeres kijelentkezés");
            
        /* } else {
            console.log("sikertelen kijelentkezés");
        } */
        
        //const { setAuth } = useContext(AuthContext);
        
    }
    

    return (
        <div>
            <header>Káoszkvíz</header>
            <nav>
                <ul className="">
                    <li className="">
                        <Link className="" to="/" style={{ textDecoration: 'none' }}>Játék</Link>
                    </li>
                    {
                        !auth?.isAuthenticated && !auth?.user ?
                            <li className="">
                                <Link className="" to="/login" style={{ textDecoration: 'none' }}>Belépés</Link>
                            </li>
                        :
                            <form onSubmit={logoutFn}>
                                <Button name="logout" title="Kijelentkezés" type="submit" />
                            </form>
                    }
                    {
                        auth?.isAuthenticated ?
                            <li className="">
                                <Link className="" to="/users" style={{ textDecoration: 'none' }}>Felhasználók</Link>
                            </li>
                        :   ""
                    }
                </ul>
            </nav>
            <article>
            {/* Ide kerül majd az útvonalak/linkek által meghatározott tartalom */}
                <Outlet />
            </article>
        </div>
    );
};

export default Layout;