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
import FileUploadForm, { SuccessUploadDatas } from "./FileUploadForm";

type QuizFormProps = {
    isnew: boolean;
    quizId: String;//!TODO nem string
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
        shortAccessibleName: "",
        mediaContentOwnerId: ("-1" as unknown) as bigint,
        mediaContentName: "",
    });
    const[modifyQuizDatas, setModifyQuizDatas] = useState<QuizModifyFormType>( {
        id: props.quizId,
        title: "",
        description: "",
        isPublic: true,
        shortAccessibleName: "",
        mediaContentOwnerId: ("-1" as unknown) as bigint,
        mediaContentName: "",
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

    function fileUploaded(response: SuccessUploadDatas) {
        
        if(props.isnew){
            setNewQuizDatas({
                ...newQuizDatas,
                mediaContentOwnerId: response.owner,
                mediaContentName: response.filename,
              });
        } else{
            setModifyQuizDatas({
                ...modifyQuizDatas,
                mediaContentOwnerId: response.owner,
                mediaContentName: response.filename,
              });
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
            
            console.log(ERR.getMessage)
        }
      };

    const INPUTS: LabeledInputProps[] = [
        {
            label: "A kvíz címe",
            inputProps: {
                name: "title",
                placeholder: "",
                type: "text",
                onChangeFn: handleChange
            }
        },
        {
            label: "A kvíz leírása",
            inputProps: {
                name: "description",
                placeholder: "",
                type: "text",
                onChangeFn: handleChange
            }
        },
        {
            label: "Publikus kvíz?",
            inputProps: {
                name: "isPublic",
                placeholder: "",
                type: "checkbox",
                onChangeFn: handleChange
            }
        },
        {
            label: "Rövid név",
            inputProps: {
                name: "shortAccessibleName",
                placeholder: "",
                type: "text",
                onChangeFn: handleChange
            }
        }
    ];

    return(
        <div className="quizForm">
            Kép:
            <FileUploadForm callback={fileUploaded}/>
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
