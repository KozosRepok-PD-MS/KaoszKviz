
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
import { FaBars } from "react-icons/fa";
import "./Layout.css";


const Layout = () => {
    const { auth, setAuth } = useContext(AuthContext);
    const navigate: NavigateFunction = useNavigate();

    const logoutFn = async () => {
    
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.USER, "logout", logoutResponseFn);
        } catch (error) {
            const ERR: ApiError = error as ApiError;

            console.log(ERR.getMessage);
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

    function handleMenuState() {
        let menu: HTMLElement | null = document.getElementById("menu");

        if (menu == null) {return;}
        
        if (menu.classList.contains("visible")) {
            menu.classList.remove("visible");
        } else {
            menu.classList.add("visible");
        }

    }
    
    const linkStyle = {
        textDecoration: "none",
        color: '#CBF7ED'
    };
    
    document.getElementById("menu")?.classList.remove("visible");

    return (
        <div>
            <header>Káoszkvíz</header>
            <nav>
                <div onClick={handleMenuState} className="menuToggle">
                <FaBars size="30px"/>
                </div>
                <ul className="menu" id="menu" >
                    <li className="">
                        <Link className="" to="/" style={linkStyle}>Játék</Link>
                    </li>
                    {
                        auth?.isAuthenticated ?
                            <li className="">
                                <Link className="" to="/users" style={linkStyle}>Felhasználók</Link>
                            </li>
                        :   ""
                    }
                    {
                        auth?.isAuthenticated ?
                        <li className="">
                                <Link className="" to="/myquizes" style={linkStyle}>Kvízeim</Link>
                            </li>
                        :   ""
                    }
                    {
                        auth?.isAuthenticated ?
                            <li className="">
                                <Link className="" to="/profile" style={linkStyle}>Profilom</Link>
                            </li>
                        :   ""
                    }
                    {
                        !auth?.isAuthenticated && !auth?.user ?
                            <li className="">
                                <Link className="" to="/login" style={linkStyle}>Belépés</Link>
                            </li>
                        :   
                            <li className="" onClick={logoutFn}>
                                Kijelentkezés
                            </li>
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