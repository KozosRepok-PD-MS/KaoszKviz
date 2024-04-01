
import React from "react";
import Input from "../components/Input";

type Props = {}

export default function Game(props: Props){
    return(
        <div className="content">
            <Input name="gamePin" label="Játék kódja:" type="number" placeholder="000000" onChangeFn={() => {}}/>
            <button>Csatlakozás</button>
        </div>
    )
}