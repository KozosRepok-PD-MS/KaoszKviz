package hu.kaoszkviz.server.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = QuizPlayer.TABLE_NAME)
public class QuizPlayer {
    
    public static final String TABLE_NAME = "quiz_player";
}
