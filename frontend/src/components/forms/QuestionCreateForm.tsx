import React, { useEffect } from "react"
import { useState } from "react";
import ApiHandler, { ApiError } from "../../helper/ApiHandler";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import LabeledInput, { LabeledInputProps } from "../inputs/LabeledInput";
import { AxiosResponse, HttpStatusCode } from "axios";
import { NavigateFunction, useNavigate } from "react-router-dom";
import Button from "../buttons/Button";
import { QuestionCreateFormType, QuestionModifyFormType } from "../../model/Question";
import { TQuestionTypeList } from "../../model/QuestionType";
import FileUploadForm, { SuccessUploadDatas } from "./FileUploadForm";

type QuestionFormProps = {
    isnew: boolean;
    quizId: String;//!TODO nem string
    questionId: String;
	mediaContentOwnerId?: bigint;
	mediaContentName?: string;
}

const QuestionForm: React.FC<QuestionFormProps> = (props: QuestionFormProps) => {
    const[newQuestionDatas, setNewQuestionDatas] = useState<QuestionCreateFormType>( {
        quizId: props.quizId,
        question: "",
        questionType: "multiple correct answer",
        timeToAnswer: 90,
        mediaContentOwnerId: ("-1" as unknown) as bigint,
        mediaContentName: "",
    });
    const[updateQuestionDatas, setUpdateQuestionDatas] = useState<QuestionModifyFormType>( {
        id: props.questionId,
        question: "",
        timeToAnswer: 90,
        mediaContentOwnerId: ("-1" as unknown) as bigint,
        mediaContentName: "",
    });
    const[questionTypes, setQuestionTypes] = useState<TQuestionTypeList>({} as TQuestionTypeList);
    const[questionTypesString, setQuestionTypesString] = useState<string[]>([] as string[]);

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
        if (response.status === HttpStatusCode.Created) {
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

    function fileUploaded(response: SuccessUploadDatas) {
        
        if(props.isnew){
            setNewQuestionDatas({
                ...newQuestionDatas,
                mediaContentOwnerId: response.owner,
                mediaContentName: response.filename,
              });
        } else{
            setUpdateQuestionDatas({
                ...updateQuestionDatas,
                mediaContentOwnerId: response.owner,
                mediaContentName: response.filename,
              });
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

    function questionTypeCallbackFn(response: AxiosResponse<any, any>) {
        setQuestionTypes(response.data as TQuestionTypeList);
        let responsData = response.data as TQuestionTypeList;
        let stringList: string[] = [];
        for (let i = 0; i < responsData.length; i++) {
            stringList.push(responsData[i].type!);
        }
        setQuestionTypesString(stringList);
    }

    useEffect(() => {
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.QUESTIONTYPE, "getAll", questionTypeCallbackFn);
        } catch (error) {
            const ERR: ApiError = error as ApiError;            
            console.log(ERR.getMessage)
        }
    }, []);

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
                    type: "select",
                    onChangeFn: handleChange,
                    options: questionTypesString,
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
            Kép:
            <FileUploadForm callback={fileUploaded}/>
            <form onSubmit={handleSubmit}>
                {INPUTS.map((input, index) => {
                    return(
                        <LabeledInput {...input} key={index}/>
                    )
                })}
                {props.isnew ? 
                    <Button name="createQuestion" title="Kérdés létrehozása" type="submit"/> 
                    :
                    <Button name="createQuestion" title="Kérdés módosítása" type="submit"/>}
            </form>
        </div>
    )

}

export default QuestionForm;
