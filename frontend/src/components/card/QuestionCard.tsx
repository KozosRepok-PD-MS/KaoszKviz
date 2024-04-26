import React, { useContext, useEffect, useState } from "react"
import { Question } from "../../model/Question";
import "./QuestionCard.css";
import { Answer, TAnswerList } from "src/model/Answer";
import { AxiosResponse } from "axios";
import ApiHandler, { ApiError } from "src/helper/ApiHandler";
import { API_CONTROLLER } from "src/config/ApiEndpoints";
import { AuthContext } from "src/context/AuthContext";
import AnswerCard from "./AnswerCard";
import Button from "../buttons/Button";
import AnswerForm from "../forms/AnswerCreateForm";
import QuestionForm from "../forms/QuestionCreateForm";

export type QuestionCardProps = {
    question: Question
    quizOwnerId: string
}

const QuestionCard: React.FC<QuestionCardProps> = (props: QuestionCardProps) => {
    const[answers, setAnswers] = useState<TAnswerList>( {} as TAnswerList );
    const id = props.question.id;
    const [newAnswerState, setNewAnswerState] = useState(<></>);
    const{auth} = useContext(AuthContext);
    const authUserId = auth?.user?.id;
    const [isEditing, setIsEditing] = useState(false);
    const [editState, setEditState] = useState(<></>);

    function handleEdit(){
        if (isEditing) {
            setEditState(<QuestionForm questionId={props.question.id!.toString()} quizId={props.question.quizId} isnew={false}/>);
        } else {
            setEditState(<></>);
        }
        setIsEditing(!isEditing);
    }

    function callbackFn(response: AxiosResponse<any, any>) {
        setAnswers(response.data as TAnswerList);
    }

    function handleNewAnswer(){
        setNewAnswerState(<AnswerForm questionId={id!.toString()} isnew={true} ordinalNumber=""/>);
    }

    function deleteCallbackFn() {
        window.location.reload();
    }

    function handleDelete(){
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.QUESTION, "deleteQuestionById", deleteCallbackFn, undefined, new Map([
                [
                    "id",
                    props.question.id!.toString()
                ]
            ]),);
        } catch (error) {
            const ERR: ApiError = error as ApiError;            
            console.log(ERR.getMessage)
        }
    }
    
    useEffect(() => {
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.ANSWER, "getAnswersByQuestionId", callbackFn, undefined, new Map([
                [
                    "questionId",
                    id!.toString()
                ]
            ]),);            
        } catch (error) {
            const ERR: ApiError = error as ApiError;            
            console.log(ERR.getMessage)
        }
    }, [id]);
    
    return(
        <div className="questionCard">
            <div className="questionCardImg"><img src="/logo512.png"/></div>
            <div className="questionCardDatas">
                <div className="questionCardQuestion">Kérdés: {props.question.question}</div>
                <div className="questionCardQuestionType">Kérdés típus: {props.question.questionType?.toString()} {/** //!TODO Backend még rosszul adja a választ. Át kell majd írni **/}
                </div>
                <div className="questionCardTimeToAnswer">Kérdés megválaszolására álló idő: {props.question.timeToAnswer}</div>
                {
                    authUserId?.toString() === props.quizOwnerId ? 
                    <div>
                            <div>
                                {isEditing ? 
                                    <Button name="editButton" title="BECSUK" type="button" onClickFn={handleEdit}/>
                                    :
                                    <Button name="editButton" title="MÓDOSÍTÁS" type="button" onClickFn={handleEdit}/>}
                                <div className="questionCardEdit">{editState}</div>
                                <div className="questionCardDelete">
                                    <Button name="deleteButton" title="TÖRLÉS" type="button" onClickFn={handleDelete}/>
                                </div>
                            </div>
                            <Button name="newAnswer" title="Új válasz" type="button" onClickFn={handleNewAnswer}/>
                            <div className="newAnswer">{newAnswerState}</div>
                        </div> 
                        :
                        <></>
                    }
                <div className="questionCardAnswers">
                        {
                            answers.length > 0 ?
                                answers.map((answer: Answer, key: number) => {
                                    return(
                                        <AnswerCard quizOwnerId={props.quizOwnerId} questionId={props.question.id!.toString()} answer={answer} key={key}></AnswerCard>
                                    )
                                })
                            :
                                "Ennek a kérdésnek nincs válasza"
                        }
                </div>
            </div>
        </div>
    )
}

export default QuestionCard;
