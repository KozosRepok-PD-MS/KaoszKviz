
import React, { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { User } from "../model/User";
import { AxiosResponse } from "axios";
import ApiHandler, { ApiError } from "../helper/ApiHandler";
import { API_CONTROLLER } from "../config/ApiEndpoints";
import { Quiz, TQuizList } from "src/model/Quiz";
import QuizCard from "src/components/card/QuizCard";
import { AuthContext } from "src/context/AuthContext";

type UsersProps = {}


const AuthUserQuizes: React.FC = (props: UsersProps) => {
    const[user, setUser] = useState<User>();
    const{auth} = useContext(AuthContext);
    const userId = auth?.user?.id;
    const[quizes, setQuizes] = useState<TQuizList>( {} as TQuizList );

    function callbackFnQuiz(response: AxiosResponse<any, any>) {
        setQuizes(response.data as TQuizList);
        console.log(response.data);
        
    }
    
    function callbackFnUser(response: AxiosResponse<any, any>) {
        setUser(response.data as User);
        console.log(response.data);
    }
    
    useEffect(() => {
        try {
            if (!userId) { console.log("no id"); }
            ApiHandler.executeApiCall(API_CONTROLLER.QUIZ, "getQuizesByUserId", callbackFnQuiz, undefined, new Map([
                [
                    "ownerId",
                    userId!.toString()
                ]
            ]),);
            ApiHandler.executeApiCall(API_CONTROLLER.USER, "getById", callbackFnUser, undefined, new Map([
                [
                    "userId",
                    userId!.toString()
                ]
            ]),);
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            
            console.log(ERR.getMessage)
        }
    }, [userId]);

    return(
        <div className="content">
           <div>Üdv {user?.username}!</div>
           <div><Link to="/newquiz">Új kvíz létrehozása</Link></div>
            <div>
                {
                    quizes.length > 0 ?
                        quizes.map((quiz: Quiz, index) => {
                            return(
                                <div>
                                    <QuizCard quiz={quiz} key={index}></QuizCard>
                                </div>
                            )
                        })
                    :
                        "Még nincsenek kvízeid"
                }
            </div>
        </div>
    )
}

export default AuthUserQuizes;