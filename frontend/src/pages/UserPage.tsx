
import React, { useEffect, useState } from "react";
import { Quiz, TQuizList } from "../model/Quiz";
import { AxiosResponse } from "axios";
import ApiHandler, { ApiError } from "../helper/ApiHandler";
import { API_CONTROLLER } from "../config/ApiEndpoints";
import QuizCard from "../components/card/QuizCard";
import { useParams } from "react-router";

type UserProps = {}


const UserPage: React.FC = (props: UserProps) => {
    const[quizes, setUsers] = useState<TQuizList>( {} as TQuizList );
    const {id} = useParams();

    function callbackFn(response: AxiosResponse<any, any>) {
        setUsers(response.data as TQuizList);
        
    }
    useEffect(() => {
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.QUIZ, "getQuizesByUserId", callbackFn, quizes);
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            
            alert(ERR.getMessage);
        }
    }, []);

    return(
        <div className="content">{id}
            {
                quizes.length > 0 ?
                    quizes.map((quiz: Quiz) => {
                        return(
                            <QuizCard quiz={quiz}></QuizCard>
                        )
                    })
                :
                    ""
            }
        </div>
    )
}

export default UserPage;