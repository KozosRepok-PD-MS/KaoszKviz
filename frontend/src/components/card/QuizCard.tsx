import React from "react";
import { Link } from "react-router-dom";
import { Quiz } from "../../model/Quiz";
import "./QuizCard.css";
import Button from "../buttons/Button";
import ImageComp from "../images/ImageComp";
import ApiHandler from "src/helper/ApiHandler";

export type QuizCardProps = {
    quiz: Quiz
}

const QuizCard: React.FC<QuizCardProps> = (props: QuizCardProps) => {
    let questionsLink: string = "/quiz/" + props.quiz.id;

    const linkStyle = {
        textDecoration: "none",
        color: '#CBF7ED'
    };

    return(
        <div className="quizCard">
            <div className="quizCardName">{props.quiz.title}</div>
            <div className="quizCardDescription">{props.quiz.description}</div>
            <div className="quizCardImg"><ImageComp name='quizCard' src={ApiHandler.imageLinkBuild(props.quiz.mediaContentOwnerId, props.quiz.mediaContentName)}/></div>
            <div className="quizCardStart"><Button name="startButton" title="START" type="button"/></div>
            <div><Link className="" to={questionsLink} style={linkStyle}>Kvíz részletei</Link></div>
            
        </div>
    )
}

export default QuizCard;
