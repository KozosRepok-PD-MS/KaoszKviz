import React from "react"
import { Quiz } from "../../model/Quiz";
import "./QuizCard.css";
import Button from "../buttons/Button";


export type QuizCardProps = {
    quiz: Quiz
}

const QuizCard: React.FC<QuizCardProps> = (props: QuizCardProps) => {
    
    return(
        <div className="quizCard">
            <div className="quizCardName">{props.quiz.title}</div>
            <div className="quizCardDescription">{props.quiz.description}</div>
            <div className="quizCardImg"><img src="/logo512.png"/></div>
            <div className="quizCardStart"><Button name="loginButton" title="START" type="button"/></div>
        </div>
    )
}

export default QuizCard;
