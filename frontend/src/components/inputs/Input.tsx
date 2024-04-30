
import React, { ChangeEventHandler } from "react";
import "./Input.css";

export type InputProps = {
    name: string;
    type: "number" | "text" | "email" | "password" | "file" | "checkbox" | "select";
    placeholder?: string;
    onChangeFn?: ChangeEventHandler<HTMLInputElement | HTMLSelectElement>;
    autocomplete?: boolean;
    classes? : string;
    msg?: string;
    options?: string[];
}


const Input: React.FC<InputProps> = (props: InputProps) => {

    //props.placeholder = props.placeholder ? props.placeholder : "";
    //props.onChangeFn = props.onChangeFn ? props.onChangeFn : () => {};
    let autocomplete: string = props.autocomplete ? String(props.autocomplete) : "false";

    let classes = props.classes === "undefined" ? "" : props.classes;
    if(typeof props.msg === "undefined" || props.msg === "") {
        classes += " inputMsgBox";
    } else {
        classes += " inputMsgBoxShow";
    }

    return(
        props.type != "select" ?
            <div>
                <input className={props.classes}
                    type={props.type}
                    id={props.name}
                    name={props.name}
                    placeholder={props.placeholder}
                    onChange={props.onChangeFn}
                    autoComplete={autocomplete}
                />
                <div id={`input#${props.name}#msgbox`} className={classes}>{props.msg}</div>
            </div>
            :
            <div>
                <select className={props.classes}
                    id={props.name}
                    name={props.name}
                    onChange={props.onChangeFn}
                    autoComplete={autocomplete}>
                        {props.options!.map((value, key) => {
                            return(
                                <option value={value} key={key}>{value}</option>
                            )
                        })}
                </select>
                <div id={`input#${props.name}#msgbox`} className={classes}>{props.msg}</div>
            </div>
    )
}

export default Input;