
import React from "react";
import Input from "../Components/Input";

type Props = {}

export default function Login(props: Props){
    return(
        <div className="content">
            <Input name="loginUsernameOrEmail" label="Felhasználónév vagy email:" type="text" placeholder="Valaki Vagyok"/>
            <Input name="loginPassword" label="Email:" type="password" placeholder=""/>
            <button>Belépés</button>
        </div>
    )
}
