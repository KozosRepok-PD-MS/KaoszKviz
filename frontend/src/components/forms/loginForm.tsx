import React from "react"
import { useState } from "react";
import { UserLoginFormType } from "../../model/User";
import ApiHandler, { ApiError } from "../../helper/ApiHandler";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import Input from "../Input";
import { AxiosHeaderValue, AxiosHeaders } from "axios";
import { API_KEY_STRING } from "../../config/GlobalDatas";

const LoginForm: React.FC = (props) => {
    const[loginDatas, setLoginDatas] = useState<UserLoginFormType>( {
        loginBase: "",
        password: ""
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setLoginDatas({
          ...loginDatas,
          [e.target.name]: e.target.value,
        });
    };

    function callbackFn(response: any) {        
        const headers: AxiosHeaders = response.headers;
        if (!headers.has(API_KEY_STRING)) { return; }

        let apiKey: AxiosHeaderValue = headers.get(API_KEY_STRING);
        localStorage.setItem(API_KEY_STRING, apiKey!.toString());
        
        window.location.href = "/";
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.USER, "login", callbackFn, loginDatas, undefined, new Map<string, string>([["Access-Control-Expose-Headers", "Content-Encoding"]]));
        
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            
            alert(ERR.getMessage);
        }
      };

    return(
        <form onSubmit={handleSubmit}>
            <Input label="Felhasználónév / email" name="loginBase" placeholder="rudi@rokamail.hu" type="text" onChangeFn={handleChange}/>
            <Input label="Jelszó" name="password" placeholder="" type="password" onChangeFn={handleChange}/>
            <button type="submit">Bejelentkezés</button>
        </form>
    )
}

export default LoginForm;