package hu.kaoszkviz.server.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = QuizPlayer.TABLE_NAME)
public class QuizPlayer implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_history_id", nullable = false)
    private QuizHistory quizHistory;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private User player;
    
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(20)", length = 20)
    private String name;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name="media_file_name"),
        @JoinColumn(name="media_owner_id")
    })
    private Media profilePicture;
    
    @Column(name = "reached_point", nullable = false)
    private long reachedPoint;
    
    @Column(name = "answer_time", nullable = false)
    private long answerTime;
    
    public static final String TABLE_NAME = "quiz_player";
}
