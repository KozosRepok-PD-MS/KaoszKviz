import React, { useEffect, useState } from "react"
import { User } from "../../model/User";
import { Link } from "react-router-dom";
import "./UserCard.css";
import ImageComp from "../images/ImageComp";
import ApiHandler, { ApiError } from "src/helper/ApiHandler";
import { TQuizList } from "src/model/Quiz";
import { AxiosResponse } from "axios";
import { API_CONTROLLER } from "src/config/ApiEndpoints";

export type UserCardProps = {
    user: User
}

const UserCard: React.FC<UserCardProps> = (props: UserCardProps) => {

    let userLink: string = "/user/" + props.user.id;
    const[quizes, setQuizes] = useState<TQuizList>( {} as TQuizList );

    function callbackFnQuiz(response: AxiosResponse<any, any>) {
        setQuizes(response.data as TQuizList);
        console.log(response.data);
    }
    
    useEffect(() => {
        try {
            if (!props.user.id) { console.log("no id"); }
            ApiHandler.executeApiCall(API_CONTROLLER.QUIZ, "getQuizesByUserId", callbackFnQuiz, undefined, new Map([
                [
                    "ownerId",
                    props.user.id!.toString()
                ]
            ]),);
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            
            console.log(ERR.getMessage)
        }
    }, []);
    
    const linkStyle = {
        fontSize: 20,
        textDecoration: "none",
        color: '#CBF7ED'
    };

    return(
        <div className="userCard">
            <div className="userCardPicture"><ImageComp name='userCard' src={ApiHandler.imageLinkBuild(props.user.profilePictureOwnerId, props.user.profilePictureName)}/></div>
            <div className="userCardUsername">
                <Link className="" to={userLink} style={linkStyle}>{props.user.username}</Link>
            </div>
            <div className="userCardNuberOfQuizes">A felhasználó kvízeinek száma: {quizes.length}</div>
        </div>
    )
}

export default UserCard;
