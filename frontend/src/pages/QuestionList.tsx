
import React, { useEffect, useState } from "react";
import { TQuestionList, Question } from "../model/Question";
import { AxiosResponse } from "axios";
import ApiHandler, { ApiError } from "../helper/ApiHandler";
import { API_CONTROLLER } from "../config/ApiEndpoints";
import QuestionCard from "../components/card/QuestionCard";
import { useParams } from "react-router";

type QuestionProps = {}


const QuestionList: React.FC = (props: QuestionProps) => {
    const[questions, setQustions] = useState<TQuestionList>( {} as TQuestionList );
    const {id} = useParams();

    function callbackFn(response: AxiosResponse<any, any>) {
        setQustions(response.data as TQuestionList);
    }
    
    useEffect(() => {
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.QUESTION, "getQuestionsByQuizId", callbackFn, undefined, new Map([
                [
                    "quizId",
                    id!.toString()
                ]
            ]),);            
        } catch (error) {
            const ERR: ApiError = error as ApiError;            
            alert(ERR.getMessage);
        }
    }, []);

    return(
        <div className="content">
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
    )
}

export default QuestionList;