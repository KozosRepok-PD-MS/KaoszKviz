import React from "react"
import { useState } from "react";
import ApiHandler, { ApiError } from "../../helper/ApiHandler";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import LabeledInput, { LabeledInputProps } from "../inputs/LabeledInput";
import { AxiosResponse, HttpStatusCode } from "axios";
import { NavigateFunction, useNavigate } from "react-router-dom";
import Button from "../buttons/Button";
import { QuestionCreateFormType, QuestionModifyFormType } from "../../model/Question";

type QuestionFormProps = {
    isnew: boolean;
    quizId: String;//!TODO nem string
    questionId: String;
}

const QuestionForm: React.FC<QuestionFormProps> = (props: QuestionFormProps) => {
    const[newQuestionDatas, setNewQuestionDatas] = useState<QuestionCreateFormType>( {
        quizId: props.quizId,
        question: "",
        questionType: "",
        timeToAnswer: 100,
    });
    const[updateQuestionDatas, setUpdateQuestionDatas] = useState<QuestionModifyFormType>( {
        id: props.questionId,
        question: "",
        timeToAnswer: 100,
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (props.isnew) {
            setNewQuestionDatas({
                ...newQuestionDatas,
                [e.target.name]: e.target.value,
            });
        } else {
            setUpdateQuestionDatas({
                ...updateQuestionDatas,
                [e.target.name]: e.target.value,
            });
        }
    }
     
    function newCallbackFn(response: AxiosResponse<any, any>) {
        if (response.status === HttpStatusCode.Ok) {
            console.log("kérdés létrehozva");
            window.location.reload();
        } else {
            console.log("nem sikertült létrehozni a kérdést");
        }
    }

    function modifyCallbackFn(response: AxiosResponse<any, any>) {
        if (response.status === HttpStatusCode.Ok) {
            console.log("kérdés modosítva");
            window.location.reload();
        } else {
            console.log("nem sikertült módosítani a kérdést");
            
        }
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        
        try {
            if (props.isnew) {
                ApiHandler.executeApiCall(API_CONTROLLER.QUESTION, "addQuestion", newCallbackFn, newQuestionDatas);
            } else {
                ApiHandler.executeApiCall(API_CONTROLLER.QUESTION, "updateQuestion", modifyCallbackFn, updateQuestionDatas);
                
            }
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            console.log(ERR.getMessage)
        }
    };
    let INPUTS: LabeledInputProps[] =[]
    if (props.isnew) {
        INPUTS = [
            {
                label: "A kérdés:",
                inputProps: {
                    name: "question",
                    placeholder: "",
                    type: "text",
                    onChangeFn: handleChange
                }
            },
            {
                label: "A kérdés típusa:",
                inputProps: {
                    name: "questionType",
                    placeholder: "",
                    type: "text",
                    onChangeFn: handleChange
                }
            },
            {
                label: "Válaszra szánt idő:",
                inputProps: {
                    name: "timeToAnswer",
                    placeholder: "",
                    type: "number",
                    onChangeFn: handleChange
                }
            }
        ];
    } else {
        INPUTS = [
            {
                label: "A kérdés:",
                inputProps: {
                    name: "question",
                    placeholder: "",
                    type: "text",
                    onChangeFn: handleChange
                }
            },
            {
                label: "Válaszra szánt idő:",
                inputProps: {
                    name: "timeToAnswer",
                    placeholder: "",
                    type: "number",
                    onChangeFn: handleChange
                }
            }
        ];
    }
    

    return(
        <div className="questionCreateForm">
            <form onSubmit={handleSubmit}>
                {INPUTS.map((input, index) => {
                    return(
                        <LabeledInput {...input} key={index}/>
                    )
                })}
                <Button name="createQuestion" title="Kérdés létrehozása" type="submit"/> 
            </form>
        </div>
    )

}

export default QuestionForm;
