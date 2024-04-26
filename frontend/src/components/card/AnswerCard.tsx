import React, { useContext, useEffect, useState } from "react"
import { Answer } from "../../model/Answer";
import "./AnswerCard.css";
import { AuthContext } from "src/context/AuthContext";
import AnswerForm from "../forms/AnswerCreateForm";
import Button from "../buttons/Button";
import ApiHandler, { ApiError } from "src/helper/ApiHandler";
import { API_CONTROLLER } from "src/config/ApiEndpoints";

export type QuestionCardProps = {
    answer: Answer;
    quizOwnerId: string;
    questionId: string;
}

const AnswerCard: React.FC<QuestionCardProps> = (props: QuestionCardProps) => {
    const{auth} = useContext(AuthContext);
    const authUserId = auth?.user?.id;
    
    const [isQuestionEditing, setIsQuestionEditing] = useState(false);
    const [editQuestionState, setEditQuestionState] = useState(<></>);

    function handleQuestionEdit(){
        if (isQuestionEditing) {
            setEditQuestionState(<AnswerForm ordinalNumber={props.answer.ordinalNumber!.toString()} questionId={props.questionId!} isnew={false}/>);
        } else {
            setEditQuestionState(<></>);
        }
        setIsQuestionEditing(!isQuestionEditing);
    }
    
    function deleteCallbackFn() {
        window.location.reload();
    }

    function handleDelete(){
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.ANSWER, "deleteAnswerById", deleteCallbackFn, undefined, new Map([
                [
                    "questionId",
                    props.answer.questionId!.toString()
                ],
                [
                    "ordinalNumber",
                    props.answer.ordinalNumber!.toString()
                ]
            ]),);
        } catch (error) {
            const ERR: ApiError = error as ApiError;            
            console.log(ERR.getMessage)
        }
    }
    return(
        <div className="answerCard">
            <div className="answerCardItem">{props.answer.answer}</div>
            <div className="answerCardItem">{props.answer.correctAnswer === null ? "nincs párja" : props.answer.correctAnswer}</div>
            <div className="answerCardItem">{props.answer.correct ? "helyes válasz" : "helytelen válasz"}</div>
            {
                authUserId?.toString() === props.quizOwnerId ?
                <div className="questionCardEdit">
                    {isQuestionEditing ? 
                        <Button name="editButton" title="BECSUK" type="button" onClickFn={handleQuestionEdit}/>
                        :
                        <Button name="editButton" title="MÓDOSÍTÁS" type="button" onClickFn={handleQuestionEdit}/>}
                    {editQuestionState}
                    <div className="answerCardDelete">
                        <Button name="deleteButton" title="TÖRLÉS" type="button" onClickFn={handleDelete}/>
                    </div>
                </div>
                :
                <></>
            }
        </div>
    )
}

export default AnswerCard;
