import React, { MouseEventHandler, ReactNode } from "react"

export type ButtonProps = {
    name: string;
    title: string | ReactNode;
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
