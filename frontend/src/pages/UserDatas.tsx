
import React, { useContext, useEffect, useState } from "react";
import { redirect } from "react-router";
import FileUploadForm from "src/components/forms/FileUploadForm";
import { AuthContext } from "src/context/AuthContext";
import { SuccessUploadDatas } from "src/components/forms/FileUploadForm"
import { AuthUser } from "src/model/Auth";
import ApiHandler from "src/helper/ApiHandler";
import { API_CONTROLLER } from "src/config/ApiEndpoints";
import { USER_DETAILS_STRING } from "src/config/GlobalDatas";
import { AxiosResponse } from "axios";
import ImageComp from "src/components/images/ImageComp";
type UserProps = {}


const UserDatas: React.FC = (props: UserProps) => {
    const { auth, setAuth } = useContext(AuthContext);
    const [imageUrl, setImgUrl] = useState<string>(ApiHandler.imageLinkBuild(auth?.user?.profilePictureOwnerId, auth?.user?.profilePictureName));

    function callbackFn(data: SuccessUploadDatas) {
        console.log(data);
        let oldDatas: AuthUser = auth!.user!;

        oldDatas.profilePictureName = data.filename;
        oldDatas.profilePictureOwnerId = data.owner;
        
        console.log(oldDatas);
        
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.USER, "update", call, oldDatas);
        } catch (ex) {
            console.log(ex);
        }
    }

    function call(response: AxiosResponse<any, any>) {
        let updatedAuthDetails: AuthUser = response.data;
        localStorage.setItem(USER_DETAILS_STRING, JSON.stringify(updatedAuthDetails));

        setAuth({
            isAuthenticated: true,
            user: updatedAuthDetails,
        });
        setImgUrl(ApiHandler.imageLinkBuild(updatedAuthDetails.profilePictureOwnerId, updatedAuthDetails.profilePictureName))
        
    }

    useEffect(() => {
        if (auth?.user?.id == null) { redirect("/"); }
    }, [auth]);

    return(
        <div className="content">
            <div>Adataim:</div>
            <div>
                <div>Email: {auth!.user!.email}</div>

                Profilkép módosítása:
                <ImageComp name="profileImage" src={imageUrl} />
                <FileUploadForm callback={callbackFn}/>
            </div>
        </div>
    )
}

export default UserDatas;