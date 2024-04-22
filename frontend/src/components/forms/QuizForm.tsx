import React, { useContext } from "react"
import { useState } from "react";
import ApiHandler, { ApiError } from "../../helper/ApiHandler";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import LabeledInput, { LabeledInputProps } from "../inputs/LabeledInput";
import { AxiosResponse, HttpStatusCode } from "axios";
import { NavigateFunction, useNavigate } from "react-router-dom";
import Button from "../buttons/Button";
import { AuthContext } from "../../context/AuthContext";
import { QuizCreateFormType, QuizModifyFormType } from "src/model/Quiz";

type QuizFormProps = {
    isnew: boolean;
    quizId: bigint;
}

const QuizForm: React.FC<QuizFormProps> = (props: QuizFormProps) => {
    const navigate: NavigateFunction = useNavigate();
    const{auth} = useContext(AuthContext);
    const userId = auth?.user?.id;
    const[newQuizDatas, setNewQuizDatas] = useState<QuizCreateFormType>( {
        ownerId: userId,
        title: "",
        description: "",
        isPublic: true,
        shortAccessibleNAme: ""
    });
    const[modifyQuizDatas, setModifyQuizDatas] = useState<QuizModifyFormType>( {
        id: props.quizId,
        title: "",
        description: "",
        isPublic: true,
        shortAccessibleNAme: ""
    });
    

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if(props.isnew){
            if (e.target.name === "isPublic") {
                setNewQuizDatas({
                  ...newQuizDatas,
                  [e.target.name]: e.target.checked,
                });
            } else {
                setNewQuizDatas({
                  ...newQuizDatas,
                  [e.target.name]: e.target.value,
                });
            }
        } else{
            if (e.target.name === "isPublic") {
                setModifyQuizDatas({
                  ...modifyQuizDatas,
                  [e.target.name]: e.target.checked,
                });
            } else {
                setModifyQuizDatas({
                  ...modifyQuizDatas,
                  [e.target.name]: e.target.value,
                });
            }
        }
    };

    function newCallbackFn(response: AxiosResponse<any, any>) {
        if (response.status === HttpStatusCode.Created) {
            console.log("kvíz létrehozva");
            
            navigate("/myquizes");
        } else {
            console.log("nem sikertült létrehozni a kvízt");
            
        }
    }
    
    function modifyCallbackFn(response: AxiosResponse<any, any>) {
        if (response.status === HttpStatusCode.Ok) {
            console.log("kvíz modosítva");
            
            navigate("/myquizes");
        } else {
            console.log("nem sikertült módosítani a kvízt");
            
        }
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        
        try {
            if(props.isnew){
                ApiHandler.executeApiCall(API_CONTROLLER.QUIZ, "addQuiz", newCallbackFn, newQuizDatas);
            } else{
                ApiHandler.executeApiCall(API_CONTROLLER.QUIZ, "updateQuiz", modifyCallbackFn, modifyQuizDatas);
            }
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
                {props.isnew ? 
                    <Button name="createQuiz" title="Kvíz létrehozása" type="submit"/> 
                    :
                    <Button name="createQuiz" title="Kvíz módosítása" type="submit"/>}
            </form>
        </div>
    )
}

export default QuizForm;
