import React, { useEffect, useState } from "react"
import { Answer } from "../../model/Answer";
import "./AnswerCard.css";

export type QuestionCardProps = {
    answer: Answer
}

const AnswerCard: React.FC<QuestionCardProps> = (props: QuestionCardProps) => {
    
    return(
        <div className="answerCard">
            <div className="answerCardItem">{props.answer.answer}</div>
            <div className="answerCardItem">{props.answer.correctAnswer === null ? "nincs párja" : props.answer.correctAnswer}</div>
            <div className="answerCardItem">{props.answer.correct ? "helyes válasz" : "helytelen válasz"}</div>
        </div>
    )
}

export default AnswerCard;
