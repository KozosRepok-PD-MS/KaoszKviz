
import React, { useContext, useEffect, useState } from "react";
import { TQuestionList, Question } from "../model/Question";
import { AxiosResponse } from "axios";
import ApiHandler, { ApiError } from "../helper/ApiHandler";
import { API_CONTROLLER } from "../config/ApiEndpoints";
import QuestionCard from "../components/card/QuestionCard";
import { useParams } from "react-router";
import { AuthContext } from "src/context/AuthContext";
import CreateQuiz from "./CreateQuiz";
import Button from "src/components/buttons/Button";
import { Quiz } from "src/model/Quiz";
import "./QuestionList.css";

type QuestionProps = {}


const QuestionList: React.FC = (props: QuestionProps) => {
    const{auth} = useContext(AuthContext);
    const {id: quizId} = useParams();
    const authUserId = auth?.user?.id;
    const [isEditing, setIsEditing] = useState(false);
    const [editState, setEditState] = useState(<></>);
    const[questions, setQustions] = useState<TQuestionList>( {} as TQuestionList );
    const[quiz, setQuiz] = useState<Quiz>({} as Quiz);

    function handleEdit(){
        if (isEditing) {
            setEditState(<CreateQuiz quizId={BigInt(quizId!)} isnew={false}/>);
        } else {
            setEditState(<></>);
        }
        setIsEditing(!isEditing);
    }

    function questionCallbackFn(response: AxiosResponse<any, any>) {
        setQustions(response.data as TQuestionList);
    }
    
    function quizCallbackFn(response: AxiosResponse<any, any>) {
        setQuiz(response.data as Quiz);
    }

    useEffect(() => {
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.QUESTION, "getQuestionsByQuizId", questionCallbackFn, undefined, new Map([
                [
                    "quizId",
                    quizId!.toString()
                ]
            ]),);
            
            ApiHandler.executeApiCall(API_CONTROLLER.QUIZ, "getQuizById", quizCallbackFn, undefined, new Map([
                [
                    "id",
                    quizId!.toString()
                ]
            ]),);
        } catch (error) {
            const ERR: ApiError = error as ApiError;            
            alert(ERR.getMessage);
        }
    }, []);

    return(
        <div className="content">
            <div className="questionList">
                <div className="quizDatas">
                    <div className="quizCardName">{quiz.title}cím.....</div>
                    <div className="quizCardImg"><img src="/logo512.png"/></div>
                    <div className="quizCardDescription">{quiz.description}leírás......................................</div>
                    {
                        authUserId === quiz.ownerId ? 
                            <div>
                                <div className="quizCardEdit">
                                    {isEditing ? 
                                        <Button name="editButton" title="BACK" type="button" onClickFn={handleEdit}/>
                                        :
                                        <Button name="editButton" title="EDIT" type="button" onClickFn={handleEdit}/>}
                                    {editState}
                                </div>
                                <div className="quizCardDelete">
                                    <Button name="deleteButton" title="DELETE" type="button"/>
                                </div>
                            </div> 
                            : 
                            ""
                    }
                </div>
                <div className="questions">
                    {
                        questions.length > 0 ?
                            questions.map((question: Question, key: number) => {
                                return(
                                    <QuestionCard question={question} key={key}></QuestionCard>
                                )
                            })
                        :
                            "Ennek a kvíznek nincs kérdése"
                    }
                </div>
            </div>
        </div>
    )
}

export default QuestionList;