
import React from "react";
import Input from "../Components/Input";

type Props = {}

export default function Game(props: Props){
    return(
        <div className="content">
            <Input name="gamePin" label="Játék kódja:" type="number" placeholder="000000"/>
            <button>Csatlakozás</button>
        </div>
    )
}