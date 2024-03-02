
import React from "react";

type Props = {
    name: string;
    label: string;
    type: "number" | "text" | "email" | "password";
    placeholder: string;
}

export default function Input(props: Props){
    return(
        <div>
            <label htmlFor={props.name} className="">{props.label}</label>
            <input type={props.type}
                   id={props.name}
                   name={props.name}
                   placeholder={props.placeholder}></input>
        </div>
    )
}
