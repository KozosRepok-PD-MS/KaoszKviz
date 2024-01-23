
package hu.kaoszkviz.server.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Quiz.TABLE_NAME)
public class Quiz {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Getter
    @Setter
    @Column(name = "user_id")
    private User owner;

    @Getter
    @Setter
    @Column(columnDefinition = "nvarchar(30)", length = 30, nullable = false)
    private String title;
    
    @Getter
    @Setter
    @Column(columnDefinition = "nvarchar(500)", length = 500)
    private String description;
    
    @Getter
    @Setter
    @Column(name = "media_content")
    private Media mediaContent;
    
    @Getter
    @Setter
    @Column(name = "is_public", columnDefinition = "bit default 0")
    private boolean isPublic;
    
    @Getter
    @Setter
    @Column(name = "short_accessible_name")
    private String shortAccessibleName;
    
    
    public static final String TABLE_NAME = "quiz";
}
