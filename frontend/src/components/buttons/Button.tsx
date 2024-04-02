import React, { MouseEventHandler } from "react"

export type ButtonProps = {
    name: string;
    title: string;
    type: "button" | "submit" | "reset";
    onClickFn?: MouseEventHandler<HTMLButtonElement>;
}

const Button: React.FC<ButtonProps> = (props: ButtonProps) => {

    return(
        <div className="button">
            <button name={props.name}
                    onClick={props.onClickFn}>
                {props.title}
            </button>
        </div>
    )
}

export default Button;
