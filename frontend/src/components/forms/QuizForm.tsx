import React, { useContext } from "react"
import { useState } from "react";
import ApiHandler, { ApiError } from "../../helper/ApiHandler";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import LabeledInput, { LabeledInputProps } from "../inputs/LabeledInput";
import { AxiosResponse, HttpStatusCode } from "axios";
import { NavigateFunction, useNavigate } from "react-router-dom";
import Button from "../buttons/Button";
import { AuthContext } from "../../context/AuthContext";
import { QuizCreateFormType } from "src/model/Quiz";

const QuizForm: React.FC = (props) => {
    const navigate: NavigateFunction = useNavigate();
    const{auth} = useContext(AuthContext);
    const userId = auth?.user?.id;
    const[quizDatas, setQuizDatas] = useState<QuizCreateFormType>( {
        ownerId: userId,
        title: "",
        description: "",
        isPublic: true,
        shortAccessibleNAme: ""
    });
    const { setAuth } = useContext(AuthContext);
    

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.name === "isPublic") {
            setQuizDatas({
              ...quizDatas,
              [e.target.name]: e.target.checked,
            });
        } else {
            setQuizDatas({
              ...quizDatas,
              [e.target.name]: e.target.value,
            });
        }
    };

    function callbackFn(response: AxiosResponse<any, any>) {
        if (response.status === HttpStatusCode.Created) {
            console.log("kvíz létrehozva");
            
            navigate("/myquizes");
        } else {
            console.log("nem sikertült létrehozni a kvízt");
            
        }
    }
    
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        
        
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.QUIZ, "addQuiz", callbackFn, quizDatas);
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            
            alert(ERR.getMessage);
        }
      };

    const INPUTS: LabeledInputProps[] = [
        {
            label: "A kvíz címe",
            inputProps: {
                name: "quizTitle",
                placeholder: "",
                type: "text",
                onChangeFn: handleChange
            }
        },
        {
            label: "A kvíz leírása",
            inputProps: {
                name: "quizDescription",
                placeholder: "",
                type: "text",
                onChangeFn: handleChange
            }
        },
        {
            label: "Publikus kvíz?",
            inputProps: {
                name: "quizIsPublic",
                placeholder: "",
                type: "checkbox",
                onChangeFn: handleChange
            }
        },
        {
            label: "Rövid név (max 3 betű)",
            inputProps: {
                name: "quizShortAccessibleNAme",
                placeholder: "",
                type: "text",
                onChangeFn: handleChange
            }
        }
    ];

    return(
        <div className="quizForm">
            <form onSubmit={handleSubmit}>
                {INPUTS.map((input, index) => {
                    return(
                        <LabeledInput {...input} key={index}/>
                    )
                })}
                <Button name="createQuiz" title="Kvíz létrehozása" type="submit"/>
            </form>
        </div>
    )
}

export default QuizForm;
