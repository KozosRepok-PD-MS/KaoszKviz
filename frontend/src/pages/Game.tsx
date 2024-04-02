
import React from "react";
import LabeledInput from "../components/inputs/LabeledInput";
import Button from "../components/buttons/Button";

type Props = {}

export default function Game(props: Props){
    return(
        <div className="content">
            <LabeledInput label="Játék kódja:" inputProps={{
                name: "gamePin",
                type: "number",
                placeholder: "000000"
            }}/>
            <Button name="playGame" title="Csatlakozás" type="button"/>
        </div>
    )
}