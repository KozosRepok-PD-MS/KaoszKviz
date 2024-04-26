
import React from "react";
import QuizForm from "../components/forms/QuizForm";

type Props = {
    isnew: boolean;
    quizId: String;//!TODO nem string
}

export default function CreateQuiz(props: Props){
    return(
        <div className="content">
            <QuizForm quizId={props.quizId} isnew={props.isnew}/>
        </div>
    )
}
