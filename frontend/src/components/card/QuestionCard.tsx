import React from "react"
import { Question } from "../../model/Question";
import { Link } from "react-router-dom";
//import "./QuestionCard.css";

export type QuestionCardProps = {
    question: Question
}

const QuestionCard: React.FC<QuestionCardProps> = (props: QuestionCardProps) => {
    
    return(
        <div className="questionCard">
            <div className="questionCardPicture"><img src="/logo512.png"/></div>
            <div className="questionCardDatas">
                Kérdés: {props.question.question}
                Kérdés típus: {props.question.questionType?.toString()} {/** //!TODO Backend még rosszul adja a választ. Át kell majd írni **/}
                Kérdés megválaszolására álló idő: {props.question.timeToAnswer}
            </div>
        </div>
    )
}

export default QuestionCard;
