
import React from "react";
import Input, { InputProps } from "./Input";

export type LabeledInputProps = {
    label: string;
    inputProps: InputProps;
    classes?: string;
}

export default function LabeledInput(props: LabeledInputProps){
    return(
        <div>
            <label htmlFor={props.inputProps.name} className={props.classes}>{props.label}</label>
            <Input {...props.inputProps}/>
        </div>
    )
}
