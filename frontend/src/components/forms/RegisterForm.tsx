import React from "react"
import { useState } from "react";
import { UserRegisterFormType } from "../../model/User";
import ApiHandler, { ApiError } from "../../helper/ApiHandler";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import { AxiosResponse, HttpStatusCode } from "axios";
import { NavigateFunction, useNavigate } from "react-router-dom";
import LabeledInput, { LabeledInputProps } from "../inputs/LabeledInput";
import Button from "../buttons/Button";
import { Link } from "react-router-dom";


let lastType: NodeJS.Timeout;

const RegisterForm: React.FC = (props) => {
    const navigate: NavigateFunction = useNavigate();
    const[registerDatas, setRegisterDatas] = useState<UserRegisterFormType>( {
        username: "",
        email: "",
        password: "",
        repassword: ""
    });
    const[rePasswordMsg, setRePasswordMsg] = useState<string>("");

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        let newRegisterDatas: UserRegisterFormType = {
            ...registerDatas,
            [e.target.name]: e.target.value,
        };
        setRegisterDatas(newRegisterDatas);
        
        clearTimeout(lastType);
        lastType = setTimeout(() => {
            
            if (newRegisterDatas.password !== "" && newRegisterDatas.repassword !== "" && newRegisterDatas.password !== newRegisterDatas.repassword) {
                setRePasswordMsg("a két jelszó nem egyezik!");
                
            } else {
                setRePasswordMsg("");
            }
        }, 600);

    };

    function callbackFn(response: AxiosResponse<any, any>) {

        if (response.status === HttpStatusCode.Created) {
            console.log("sikeres regisztráció");
            
            navigate("/login");
        } else {
            console.log("sikertelen regisztráció");
            
        }
        
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.USER, "create", callbackFn, registerDatas);
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            
            console.log(ERR.getMessage)
        }
    };

    const INPUTS: LabeledInputProps[] = [
        {
            label: "Felhasználónév",
            inputProps: {
                name: "username",
                placeholder: "Róka Rudi",
                type: "text",
                onChangeFn: handleChange
            }
        },
        {
            label: "Email",
            inputProps: {
                name: "email",
                placeholder: "rudi@rokamail.hu",
                type: "email",
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
        },
        {
            label: "Jelszó megerősítése",
            inputProps: {
                name: "repassword",
                placeholder: "",
                type: "password",
                onChangeFn: handleChange,
                msg: rePasswordMsg
            }
        }
    ];

    return(
        <div className="registerForm">
            <form onSubmit={handleSubmit}>
                {INPUTS.map((input, index) => {
                    return(
                        <LabeledInput {...input} key={index}/>
                    )
                })}
                <Button name="registerButton" title="Regisztráció" type="submit"/>
            </form>
            <div>
                <Link to="/login">
                    Bejelentkezés
                </Link>
            </div>
    </div>
    )
}

export default RegisterForm;