import React, { useEffect, useState } from "react"
import { Question } from "../../model/Question";
import "./QuestionCard.css";
import { Answer, TAnswerList } from "src/model/Answer";
import { AxiosResponse } from "axios";
import ApiHandler, { ApiError } from "src/helper/ApiHandler";
import { API_CONTROLLER } from "src/config/ApiEndpoints";
import AnswerCard from "./AnswerCard";
import Button from "../buttons/Button";
import AnswerForm from "../forms/AnswerCreateForm";

export type QuestionCardProps = {
    question: Question
}

const QuestionCard: React.FC<QuestionCardProps> = (props: QuestionCardProps) => {
    const[answers, setAnswers] = useState<TAnswerList>( {} as TAnswerList );
    const id = props.question.id;
    const [newAnswerState, setNewAnswerState] = useState(<></>);

    function callbackFn(response: AxiosResponse<any, any>) {
        setAnswers(response.data as TAnswerList);
    }

    function handleNewAnswer(){
        setNewAnswerState(<AnswerForm questionId={id!.toString()} isnew={true}/>);
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
                <div className="questionCardAnswers">
                    {
                        answers.length > 0 ?
                            answers.map((answer: Answer, key: number) => {
                                return(
                                    <AnswerCard answer={answer} key={key}></AnswerCard>
                                )
                            })
                        :
                            "Ennek a kérdésnek nincs válasza"
                    }
                    <Button name="newAnswer" title="Új válasz" type="button" onClickFn={handleNewAnswer}/>
                    <div className="newAnswer">{newAnswerState}</div>
                </div>
            </div>
        </div>
    )
}

export default QuestionCard;
