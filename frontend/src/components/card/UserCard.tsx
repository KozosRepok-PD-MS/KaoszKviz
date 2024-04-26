import React from "react"
import { User } from "../../model/User";
import { Link } from "react-router-dom";
import "./UserCard.css";
import ImageComp from "../images/ImageComp";
import ApiHandler from "src/helper/ApiHandler";

export type UserCardProps = {
    user: User
}

const UserCard: React.FC<UserCardProps> = (props: UserCardProps) => {

    let userLink: string = "/user/" + props.user.id;
    
    return(
        <div className="userCard">
            <div className="userCardPicture"><ImageComp name='userCard' src={ApiHandler.imageLinkBuild(props.user.profilePictureOwnerId, props.user.profilePictureName)}/></div>
            <div className="userCardUsername">
                <Link className="" to={userLink} style={{ textDecoration: 'none' }}>{props.user.username}</Link>
            </div>
            <div className="userCardNuberOfQuizes">0</div>
        </div>
    )
}

export default UserCard;
