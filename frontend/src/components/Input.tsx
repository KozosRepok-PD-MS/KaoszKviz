
import React, { ChangeEventHandler } from "react";

type Props = {
    name: string;
    label: string;
    type: "number" | "text" | "email" | "password";
    placeholder: string;
    onChangeFn: ChangeEventHandler<HTMLInputElement>;
}

export default function Input(props: Props){
    return(
        <div>
            <label htmlFor={props.name} className="">{props.label}</label>
            <input type={props.type}
                   id={props.name}
                   name={props.name}
                   placeholder={props.placeholder}
                   onChange={props.onChangeFn}></input>
        </div>
    )
}
