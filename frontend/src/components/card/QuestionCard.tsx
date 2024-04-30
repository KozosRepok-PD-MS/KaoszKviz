import React, { useContext, useEffect, useState } from "react"
import { Question } from "../../model/Question";
import "./QuestionCard.css";
import { Answer, TAnswerList } from "src/model/Answer";
import { AxiosResponse } from "axios";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import { AuthContext } from "src/context/AuthContext";
import AnswerCard from "./AnswerCard";
import ImageComp from "../images/ImageComp";
import ApiHandler, { ApiError } from "src/helper/ApiHandler";
import Button from "../buttons/Button";
import AnswerForm from "../forms/AnswerCreateForm";
import QuestionForm from "../forms/QuestionCreateForm";
import { MdOutlineDeleteForever } from "react-icons/md";
import { FaPencilAlt } from "react-icons/fa";

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
        setIsEditing(!isEditing);
        if (!isEditing) {
            setEditState(<QuestionForm questionId={props.question.id!.toString()} quizId={props.question.quizId} isnew={false}/>);
        } else {
            setEditState(<></>);
        }
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
            <div className="questionCardImg">
                <ImageComp name="" src={ApiHandler.imageLinkBuild(props.question.mediaContentOwnerId, props.question.mediaContentName)}/>
            </div>
            <div className="questionCardDatas">
                <div className="questionCardQuestion">Kérdés: {props.question.question}</div>
                <div className="questionCardQuestionType">Kérdés típus: {props.question.questionType?.toString()} {/** //!TODO Backend még rosszul adja a választ. Át kell majd írni **/}
                </div>
                <div className="questionCardTimeToAnswer">Kérdés megválaszolására álló idő: {props.question.timeToAnswer}</div>
                {
                    authUserId?.toString() === props.quizOwnerId ? 
                    <div>
                        <div className="handle">
                            <div className="handleButtons">
                                {isEditing ? 
                                    <Button name="editButton" title="BECSUK" type="button" onClickFn={handleEdit}/>
                                    :
                                    <Button name="editButton" title={<FaPencilAlt size="20px" />} type="button" onClickFn={handleEdit}/>}
                                <div className="questionCardDelete">
                                    <Button name="deleteButton" title={<MdOutlineDeleteForever size="20px" />} type="button" onClickFn={handleDelete}/>
                                </div>
                            </div>
                            <div className="questionCardEdit">{editState}</div>
                        </div>
                        <div className="newSomething">
                            <Button name="newAnswer" title="Új válasz" type="button" onClickFn={handleNewAnswer}/>
                            <div className="newAnswer">{newAnswerState}</div>
                        </div>
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
