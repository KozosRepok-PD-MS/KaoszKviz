
import React, { useContext, useEffect, useState } from "react";
import { TQuestionList, Question } from "../model/Question";
import { AxiosResponse } from "axios";
import ApiHandler, { ApiError } from "../helper/ApiHandler";
import { API_CONTROLLER } from "../config/ApiEndpoints";
import QuestionCard from "../components/card/QuestionCard";
import { NavigateFunction, useNavigate, useParams } from "react-router";
import { AuthContext } from "src/context/AuthContext";
import CreateQuiz from "./CreateQuiz";
import QuestionForm from "../components/forms/QuestionCreateForm";
import Button from "src/components/buttons/Button";
import { Quiz } from "src/model/Quiz";
import "./QuestionList.css";
import ImageComp from "src/components/images/ImageComp";

type QuestionProps = {}


const QuestionList: React.FC = (props: QuestionProps) => {
    const navigate: NavigateFunction = useNavigate();
    const{auth} = useContext(AuthContext);
    const {id: quizId} = useParams();
    const authUserId = auth?.user?.id;

    const [isQuizEditing, setIsQuizEditing] = useState(false);
    const [editQuizState, setEditQuizState] = useState(<></>);
    
    const[questions, setQustions] = useState<TQuestionList>( {} as TQuestionList );
    const[quiz, setQuiz] = useState<Quiz>({} as Quiz);
    const [newQuestionState, setNewQuestionState] = useState(<></>);

    function handleQuizEdit(){
        if (isQuizEditing) {
            setEditQuizState(<CreateQuiz quizId={quizId!} isnew={false}/>);
        } else {
            setEditQuizState(<></>);
        }
        setIsQuizEditing(!isQuizEditing);
    }
    
    function deleteCallbackFn() {
        navigate("/myquizes");
    }

    function handleDelete(){
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.QUIZ, "deleteQuizById", deleteCallbackFn, undefined, new Map([
                [
                    "id",
                    quizId!.toString()
                ]
            ]),);
            console.log(quizId!.toString());
            
        } catch (error) {
            const ERR: ApiError = error as ApiError;            
            console.log(ERR.getMessage)
        }
    }

    function questionCallbackFn(response: AxiosResponse<any, any>) {
        setQustions(response.data as TQuestionList);
    }
    
    function quizCallbackFn(response: AxiosResponse<any, any>) {
        setQuiz(response.data as Quiz);
    }

    function handleNewQuestion(){
        setNewQuestionState(<QuestionForm questionId="" quizId={quizId!} isnew={true}/>);
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
            console.log(ERR.getMessage)
        }
    }, [quizId]);

    return(
        <div className="content">
            <div className="questionList">
                <div className="quizDatas">
                    <div className="quizCardName">{quiz.title}</div>
                    <div className="quizCardImg"><ImageComp name='quizCardImg' src={ApiHandler.imageLinkBuild(quiz.mediaOwnerId, quiz.mediaFileName)}/></div>
                    <div className="quizCardDescription">{quiz.description}</div>
                    {
                        authUserId === quiz.ownerId ? 
                            <div>
                                <div className="quizCardEdit">
                                    {isQuizEditing ? 
                                        <Button name="editButton" title="BECSUK" type="button" onClickFn={handleQuizEdit}/>
                                        :
                                        <Button name="editButton" title="MÓDOSÍTÁS" type="button" onClickFn={handleQuizEdit}/>}
                                    {editQuizState}
                                </div>
                                <div className="quizCardDelete">
                                    <Button name="deleteButton" title="TÖRLÉS" type="button" onClickFn={handleDelete}/>
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
                                    <QuestionCard question={question} quizOwnerId={quiz.ownerId!.toString()} key={key}></QuestionCard>
                                )
                            })
                        :
                            "Ennek a kvíznek nincs kérdése"
                    }
                    {
                        authUserId === quiz.ownerId ? 
                            <div>
                                <Button name="newQuestion" title="Új kérdés" type="button" onClickFn={handleNewQuestion}/>
                                <div className="newQuestion">{newQuestionState}</div>
                            </div> 
                            :
                            <></>
                    }
                </div>
            </div>
        </div>
    )
}

export default QuestionList;