import React, { useContext } from "react"
import { useState } from "react";
import { UserLoginFormType } from "../../model/User";
import ApiHandler, { ApiError } from "../../helper/ApiHandler";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import LabeledInput, { LabeledInputProps } from "../inputs/LabeledInput";
import { AxiosHeaderValue, AxiosHeaders, AxiosResponse } from "axios";
import { API_KEY_STRING, USER_DETAILS_STRING } from "../../config/GlobalDatas";
import { NavigateFunction, useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import Button from "../buttons/Button";
import { AuthContext } from "../../context/AuthContext";
import { AuthUser } from "src/model/Auth";

const LoginForm: React.FC = (props) => {
    const navigate: NavigateFunction = useNavigate();
    const[loginDatas, setLoginDatas] = useState<UserLoginFormType>( {
        loginBase: "",
        password: ""
    });
    const { setAuth } = useContext(AuthContext);
    

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setLoginDatas({
          ...loginDatas,
          [e.target.name]: e.target.value,
        });
    };

    function callbackFn(response: AxiosResponse<any, any>) {
        const headers: AxiosHeaders = response.headers as AxiosHeaders;
        if (!headers.has(API_KEY_STRING)) { return; }

        let apiKey: AxiosHeaderValue = headers.get(API_KEY_STRING);
        let authUser: AuthUser = response.data as AuthUser;
        
        localStorage.setItem(API_KEY_STRING, apiKey!.toString());
        localStorage.setItem(USER_DETAILS_STRING, JSON.stringify(authUser));

        setAuth({
            isAuthenticated: true,
            user: authUser
        });
        
        navigate("/");
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.USER, "login", callbackFn, loginDatas);
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            
            console.log(ERR.getMessage)
        }
      };

    const INPUTS: LabeledInputProps[] = [
        {
            label: "Felhasználónév / email",
            inputProps: {
                name: "loginBase",
                placeholder: "rudi@rokamail.hu",
                type: "text",
                onChangeFn: handleChange
            }
        },
        {
            label: "Jelszó",
            inputProps: {
                name: "password",
                placeholder: "",
                type: "password",
                onChangeFn: handleChange
            }
        }
    ];

    const linkStyle = {
        textDecoration: "none",
        color: '#CBF7ED'
    };

    return(
        <div className="loginForm">
            <form onSubmit={handleSubmit}>
                {INPUTS.map((input, index) => {
                    return(
                        <LabeledInput {...input} key={index}/>
                    )
                })}
                <Button name="loginButton" title="Bejelentkezés" type="submit"/>
            </form>
            <div>
                <Link to="/register" style={linkStyle}>
                    Regisztáció
                </Link>
            </div>
        </div>
    )
}

export default LoginForm;
