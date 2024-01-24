package hu.kaoszkviz.server.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = QuizPlayer.TABLE_NAME)
public class QuizPlayer {
    
    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_history_id", nullable = false)
    private QuizHistory quizHistory;
    
    @Getter
    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private User player;
    
    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;
    
    @Getter
    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pfp_id", nullable = false)
    private Media pfp;
    
    @Getter
    @Setter
    @Column(name = "reached_point", nullable = false)
    private long reachedPoint;
    
    @Getter
    @Setter
    @Column(name = "answer_time", nullable = false)
    private long answerTime;
    
    public static final String TABLE_NAME = "quiz_player";
}
