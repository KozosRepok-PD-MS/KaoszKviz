import React from "react"
import { useState } from "react";
import ApiHandler, { ApiError } from "../../helper/ApiHandler";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import LabeledInput, { LabeledInputProps } from "../inputs/LabeledInput";
import { AxiosResponse, HttpStatusCode } from "axios";
import Button from "../buttons/Button";
import { AnswerCreateFormType, AnswerModifyFormType } from "../../model/Answer";

type AnswerFormProps = {
    isnew: boolean;
    questionId: String;//!TODO nem string
    ordinalNumber: string;
}

const AnswerForm: React.FC<AnswerFormProps> = (props: AnswerFormProps) => {
    const[newAnswerDatas, setNewAnswerDatas] = useState<AnswerCreateFormType>( {
        questionId: props.questionId,
        answer: "",
        correct: false,
        correctAnswer: "",
    });
    const[updateAnswerDatas, setUpdateAnswerDatas] = useState<AnswerModifyFormType>( {
        questionId: props.questionId,
        ordinalNumber: props.ordinalNumber,
        answer: "",
        correct: false,
        correctAnswer: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (props.isnew) {
            if (e.target.name == "correct") {
                setNewAnswerDatas({
                  ...newAnswerDatas,
                  [e.target.name]: e.target.checked,
                });
            } else {
                setNewAnswerDatas({
                  ...newAnswerDatas,
                  [e.target.name]: e.target.value,
                });
            }
        } else {
            if (e.target.name == "correct") {
                setUpdateAnswerDatas({
                  ...updateAnswerDatas,
                  [e.target.name]: e.target.checked,
                });
            } else {
                setUpdateAnswerDatas({
                  ...updateAnswerDatas,
                  [e.target.name]: e.target.value,
                });
            }
        }
    }
     
    function newCallbackFn(response: AxiosResponse<any, any>) {
        if (response.status === HttpStatusCode.Ok) {
            console.log("válasz létrehozva");
            window.location.reload();
        } else {
            console.log("nem sikertült létrehozni a választ");
        }
    }

    function modifyCallbackFn(response: AxiosResponse<any, any>) {
        if (response.status === HttpStatusCode.Ok) {
            console.log("valasz modosítva");
            window.location.reload();
        } else {
            console.log("nem sikertült módosítani a valaszt");
            
        }
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        
        try {
            if (props.isnew) {
                ApiHandler.executeApiCall(API_CONTROLLER.ANSWER, "addAnswer", newCallbackFn, newAnswerDatas);
            } else {
                ApiHandler.executeApiCall(API_CONTROLLER.ANSWER, "updateAnswer", modifyCallbackFn, updateAnswerDatas);
                console.log(updateAnswerDatas);
                
            }
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            console.log(ERR.getMessage)
        }
    };

    const INPUTS: LabeledInputProps[] = [
        {
            label: "A válasz:",
            inputProps: {
                name: "answer",
                placeholder: "",
                type: "text",
                onChangeFn: handleChange
            }
        },
        {
            label: "Helyes e:",
            inputProps: {
                name: "correct",
                placeholder: "",
                type: "checkbox",
                onChangeFn: handleChange
            }
        },
        {
            label: "Kérdés párja (ha van):",
            inputProps: {
                name: "correctAnswer",
                placeholder: "",
                type: "text",
                onChangeFn: handleChange
            }
        }
    ];

    return(
        <div className="AnswerCreateForm">
            <form onSubmit={handleSubmit}>
                {INPUTS.map((input, index) => {
                    return(
                        <LabeledInput {...input} key={index}/>
                    )
                })}
                {props.isnew ? 
                    <Button name="createAnswer" title="Válasz létrehozása" type="submit"/> 
                    :
                    <Button name="createAnswer" title="Válasz módosítása" type="submit"/>}
            </form>
        </div>
    )

}

export default AnswerForm;
