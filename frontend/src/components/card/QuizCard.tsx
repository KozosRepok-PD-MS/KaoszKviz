import React from "react";
import { Link } from "react-router-dom";
import { Quiz } from "../../model/Quiz";
import "./QuizCard.css";
import Button from "../buttons/Button";

export type QuizCardProps = {
    quiz: Quiz
}

const QuizCard: React.FC<QuizCardProps> = (props: QuizCardProps) => {
    let questionsLink: string = "/quiz/" + props.quiz.id;

    return(
        <div className="quizCard">
            <div className="quizCardName">{props.quiz.title}</div>
            <div className="quizCardDescription">{props.quiz.description}</div>
            <div className="quizCardImg"><img src="/logo512.png"/></div>
            <div className="quizCardStart"><Button name="startButton" title="START" type="button"/></div>
            <div><Link className="" to={questionsLink} style={{ textDecoration: 'none' }}>Kvíz részletei</Link></div>
            
        </div>
    )
}

export default QuizCard;
