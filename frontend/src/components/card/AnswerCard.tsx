import React, { useContext, useEffect, useState } from "react"
import { Answer } from "../../model/Answer";
import "./AnswerCard.css";
import { AuthContext } from "src/context/AuthContext";
import AnswerForm from "../forms/AnswerCreateForm";
import Button from "../buttons/Button";
import ApiHandler, { ApiError } from "src/helper/ApiHandler";
import { API_CONTROLLER } from "src/config/ApiEndpoints";
import { MdOutlineDeleteForever } from "react-icons/md";
import { FaPencilAlt } from "react-icons/fa";

export type QuestionCardProps = {
    answer: Answer;
    quizOwnerId: string;
    questionId: string;
}

const AnswerCard: React.FC<QuestionCardProps> = (props: QuestionCardProps) => {
    const{auth} = useContext(AuthContext);
    const authUserId = auth?.user?.id;
    
    const [isAnswerEditing, setIsAnswerEditing] = useState(false);
    const [editAnswerState, setEditAnswerState] = useState(<></>);

    function handleQuestionEdit(){
        setIsAnswerEditing(!isAnswerEditing);
        if (!isAnswerEditing) {
            setEditAnswerState(<AnswerForm ordinalNumber={props.answer.ordinalNumber!.toString()} questionId={props.questionId!} isnew={false}/>);
        } else {
            setEditAnswerState(<></>);
        }
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
                <div className="handle">
                    <div className="handleButtons">
                        {isAnswerEditing ? 
                            <Button name="editButton" title="BECSUK" type="button" onClickFn={handleQuestionEdit}/>
                            :
                            <Button name="editButton" title={<FaPencilAlt size="20px" />} type="button" onClickFn={handleQuestionEdit}/>}
                        <div className="answerCardDelete">
                            <Button name="deleteButton" title={<MdOutlineDeleteForever size="20px" />} type="button" onClickFn={handleDelete}/>
                        </div>
                    </div>
                    <div>{editAnswerState}</div>
                </div>
                :
                <></>
            }
        </div>
    )
}

export default AnswerCard;
