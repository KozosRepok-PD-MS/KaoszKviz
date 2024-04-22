import React, { useContext, useState } from "react";
import { Link } from "react-router-dom";
import { Quiz } from "../../model/Quiz";
import "./QuizCard.css";
import Button from "../buttons/Button";
import { AuthContext } from "src/context/AuthContext";
import CreateQuiz from "src/pages/CreateQuiz";


export type QuizCardProps = {
    quiz: Quiz
}

const QuizCard: React.FC<QuizCardProps> = (props: QuizCardProps) => {
    const{auth} = useContext(AuthContext);
    const authUserId = auth?.user?.id;
    const [isEditing, setIsEditing] = useState(false);
    const [editState, setEditState] = useState(<></>);

    let questionsLink: string = "/quiz/" + props.quiz.id;

    function handleEdit(){
        if (isEditing) {
            setEditState(<CreateQuiz quizId={-1n} isnew={false}/>);
        } else {
            setEditState(<></>);
        }
        setIsEditing(!isEditing);
    }
    
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
                            {isEditing ? 
                                <Button name="editButton" title="BACK" type="button" onClickFn={handleEdit}/>
                                :
                                <Button name="editButton" title="EDIT" type="button" onClickFn={handleEdit}/>}
                            
                            {editState}
                        </div>
                        <div className="quizCardDelete">
                            <Button name="deleteButton" title="DELETE" type="button"/>
                        </div>
                    </div> 
                    : 
                    ""
            }       
            <div><Link className="" to={questionsLink} style={{ textDecoration: 'none' }}>Kvíz részletei</Link></div>
            
        </div>
    )
}

export default QuizCard;
