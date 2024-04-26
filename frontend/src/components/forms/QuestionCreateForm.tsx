import React from "react"
import { useState } from "react";
import ApiHandler, { ApiError } from "../../helper/ApiHandler";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import LabeledInput, { LabeledInputProps } from "../inputs/LabeledInput";
import { AxiosResponse, HttpStatusCode } from "axios";
import { NavigateFunction, useNavigate } from "react-router-dom";
import Button from "../buttons/Button";
import { QuestionCreateFormType } from "../../model/Question";

type QuestionFormProps = {
    isnew: boolean;
    quizId: String;//!TODO nem string
}

const QuestionForm: React.FC<QuestionFormProps> = (props: QuestionFormProps) => {
    const[newQuestionDatas, setNewQuestionDatas] = useState<QuestionCreateFormType>( {
        quizId: props.quizId,
        question: "",
        questionType: "",
        timeToAnswer: 100,
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setNewQuestionDatas({
            ...newQuestionDatas,
            [e.target.name]: e.target.value,
        });
    }
     
    function newCallbackFn(response: AxiosResponse<any, any>) {
        if (response.status === HttpStatusCode.Ok) {
            console.log("kérdés létrehozva");
            window.location.reload();
        } else {
            console.log("nem sikertült létrehozni a kérdést");
        }
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.QUESTION, "addQuestion", newCallbackFn, newQuestionDatas);
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            console.log(ERR.getMessage)
        }
    };

    const INPUTS: LabeledInputProps[] = [
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

    return(
        <div className="questionCreateForm">
            <form onSubmit={handleSubmit}>
                {INPUTS.map((input, index) => {
                    return(
                        <LabeledInput {...input} key={index}/>
                    )
                })}
                <Button name="createQuiz" title="Kérdés létrehozása" type="submit"/> 
            </form>
        </div>
    )

}

export default QuestionForm;
