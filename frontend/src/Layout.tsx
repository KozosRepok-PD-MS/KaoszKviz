
import React from "react";
import "./App.css";
import { Outlet, Link } from "react-router-dom";


const Layout = () => {
    return (
        <div>
            <header>Káoszkvíz</header>
            <nav>
                <ul className="">
                    <li className="">
                        <Link className="" to="/" style={{ textDecoration: 'none' }}>Játék</Link>
                    </li>
                    <li className="">
                        <Link className="" to="/login" style={{ textDecoration: 'none' }}>Belépés</Link>
                    </li>
                    <li className="">
                        <Link className="" to="/register" style={{ textDecoration: 'none' }}>Regisztráció</Link>
                    </li>
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