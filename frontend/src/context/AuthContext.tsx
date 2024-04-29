import React from "react";
import { createContext, useState } from "react";
import { Auth, AuthUser } from "../model/Auth";
import { useEffect } from "react";
import { USER_DETAILS_STRING } from "../config/GlobalDatas";
import { Dispatch } from "react";
import { SetStateAction } from "react";

type AuthContextProps = {
    children: React.ReactNode
};

type AuthContextType = {
    auth: Auth | null;
    setAuth: Dispatch<SetStateAction<Auth | null>>;
};

export const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export const AuthProvider = ({ children }: AuthContextProps) => {
    const [auth, setAuth] = useState<Auth | null>(null);
    const isReady = true;

    useEffect(() => {
        const user = localStorage.getItem(USER_DETAILS_STRING);
        //console.log(user);
        
        if (user &&user !== "") {
            setAuth({
                isAuthenticated: true,
                user: JSON.parse(user) as AuthUser
            });            
        }
    }, []);


    return(
        <AuthContext.Provider value={{auth: auth, setAuth: setAuth}} >
            {isReady ? children : null}
        </AuthContext.Provider>
    )
};


export const useAuth = () => React.useContext(AuthContext);