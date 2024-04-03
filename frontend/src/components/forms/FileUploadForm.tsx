import React from "react"
import { useState } from "react";
import ApiHandler, { ApiError } from "../../helper/ApiHandler";
import { API_CONTROLLER } from "../../config/ApiEndpoints";
import { AxiosResponse, HttpStatusCode } from "axios";
import Button from "../buttons/Button";
import Input from "../inputs/Input";


const FileUploadForm: React.FC = (props) => {
    //const navigate: NavigateFunction = useNavigate();
    const[formData, setFormData] = useState(new FormData());
    
    const setFile = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { files } = e.target;
        const fileList: FileList = files as FileList;
        const selectedFile: File = fileList[0];
        
        const form: FormData = new FormData();
        form.append("file", selectedFile);
        form.append("filename", selectedFile.name)
        setFormData(form);
    };

    function onRequestCompleted(response: AxiosResponse<any, any>) {

        if (response.status === HttpStatusCode.Created) {
            console.log("sikeres feltöltés");
        } else {
            console.log("sikertelen feltöltés");
            
        }
        
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    
        try {
            ApiHandler.executeApiCall(API_CONTROLLER.MEDIA, "upload", onRequestCompleted, formData);
        } catch (error) {
            const ERR: ApiError = error as ApiError;
            
            alert(ERR.getMessage);
        }
    };

    return(
        <div className="fileUploadForm">
            <form onSubmit={handleSubmit}>
                <Input name="file" type="file" onChangeFn={setFile} />
                <Button name="uploadButton" title="Feltöltés" type="submit"/>
            </form>
    </div>
    )
}

export default FileUploadForm;