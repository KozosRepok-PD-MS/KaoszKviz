import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { Quiz } from "../../model/Quiz";
import "./QuizCard.css";
import Button from "../buttons/Button";
import { AuthContext } from "src/context/AuthContext";


export type QuizCardProps = {
    quiz: Quiz
}

const QuizCard: React.FC<QuizCardProps> = (props: QuizCardProps) => {
    const{auth} = useContext(AuthContext);
    const authUserId = auth?.user?.id;

    let questionsLink: string = "/questions/" + props.quiz.id;
    
    return(
        <div className="quizCard">
            <div className="quizCardName">{props.quiz.title}</div>
            <div className="quizCardDescription">{props.quiz.description}</div>
            <div className="quizCardImg"><img src="/logo512.png"/></div>
            <div className="quizCardStart"><Button name="startButton" title="START" type="button"/></div>
            {
                authUserId === props.quiz.ownerId ? 
                    <div>
                        <div className="quizCardEdit">
                            <Button name="editButton" title="EDIT" type="button"/>
                        </div>
                        <div className="quizCardDelete">
                            <Button name="deleteButton" title="DELETE" type="button"/>
                        </div>
                    </div> 
                    : 
                    ""
            }
            <div><Link className="" to={questionsLink} style={{ textDecoration: 'none' }}>Kvíz kérdési</Link></div>
            
        </div>
    )
}

export default QuizCard;
