package hu.kaoszkviz.server.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = QuizHistory.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"quiz_id", "starter_user_id", "starting_time"})
})
public class QuizHistory {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Getter
    @Setter
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    @Getter
    @Setter
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "starter_user_id", nullable = false)
    private User starter;
    
    @Getter
    @Setter
    @CreationTimestamp
    @Column(name = "starting_time", nullable = false)
    private LocalDateTime startingTime;
    
    
    @Getter
    @Setter
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quizHistory")
    private List<QuizPlayer> players = new ArrayList<>();
    
    
    public static final String TABLE_NAME = "quiz_history";
}
