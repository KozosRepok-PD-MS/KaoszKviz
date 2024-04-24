
import React, { ReactEventHandler, SyntheticEvent, useEffect, useState } from "react";
import { TUserList, User } from "src/model/User";
import { AxiosResponse } from "axios";
import ApiHandler, { ApiError } from "src/helper/ApiHandler";
import { API_CONTROLLER } from "src/config/ApiEndpoints";
import UserCard from "../components/card/UserCard";

type UsersProps = {}


const UsersList: React.FC = (props: UsersProps) => {
    const[users, setUsers] = useState<TUserList>( {} as TUserList );

    function callbackFn(response: AxiosResponse<any, any>) {
        setUsers(response.data as TUserList);
        
    }

    useEffect(() => {
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.USER, "getAll", callbackFn, users);
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            
            console.log(ERR.getMessage)
        }
    }, [users]);

    return(
        <div className="content">
            {
                users.length > 0 ?
                    users.map((user: User) => {
                        return(
                            <UserCard user={user}></UserCard>
                        )
                    })
                :
                    ""
            }
        </div>
    )
}

export default UsersList;