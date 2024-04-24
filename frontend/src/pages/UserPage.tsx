
import React, { useEffect, useState } from "react";
import { Quiz, TQuizList } from "../model/Quiz";
import { AxiosResponse } from "axios";
import ApiHandler, { ApiError } from "../helper/ApiHandler";
import { API_CONTROLLER } from "../config/ApiEndpoints";
import QuizCard from "../components/card/QuizCard";
import { useParams } from "react-router";
import { User } from "src/model/User";

type UserProps = {}


const UserPage: React.FC = (props: UserProps) => {
    const[quizes, setQuizes] = useState<TQuizList>( {} as TQuizList );
    const[user, setUser] = useState<User>();
    const {id} = useParams();

    function callbackFnQuiz(response: AxiosResponse<any, any>) {
        setQuizes(response.data as TQuizList);
    }
    function callbackFnUser(response: AxiosResponse<any, any>) {
        setUser(response.data as User);
        
    }
    useEffect(() => {
        try {
            if (!id) { alert("no id"); }
            ApiHandler.executeApiCall(API_CONTROLLER.QUIZ, "getQuizesByUserId", callbackFnQuiz, undefined, new Map([
                [
                    "ownerId",
                    id!.toString()
                ]
            ]),);
            ApiHandler.executeApiCall(API_CONTROLLER.USER, "getById", callbackFnUser, undefined, new Map([
                [
                    "userId",
                    id!.toString()
                ]
            ]),);
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            
            console.log(ERR.getMessage)
        }
    }, []);

    return(
        <div className="content">
            <div>{user?.username} felhasználó kvízei:</div>
            <div>
                {
                    quizes.length > 0 ?
                        quizes.map((quiz: Quiz) => {
                            return(
                                <QuizCard quiz={quiz}></QuizCard>
                            )
                        })
                    :
                        "Ennek a felhassználónak nincs kvíze"
                }
            </div>
        </div>
    )
}

export default UserPage;