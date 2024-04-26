
import React, { useContext, useEffect } from "react";
import { redirect } from "react-router";
import FileUploadForm from "src/components/forms/FileUploadForm";
import { AuthContext } from "src/context/AuthContext";

type UserProps = {}


const UserDatas: React.FC = (props: UserProps) => {
    const { auth } = useContext(AuthContext);

    function callbackFn() {

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
                <FileUploadForm callback={callbackFn}/>
            </div>
        </div>
    )
}

export default UserDatas;