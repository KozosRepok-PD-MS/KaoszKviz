package hu.kaoszkviz.server.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = Topic.TABLE_NAME)
public class Topic {
    
    @Id
    @Setter
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Setter
    @Getter
    @Column(nullable = false, columnDefinition = "nvarchar(20)", length = 20)
    private String title;
    
    public static final String TABLE_NAME = "topic";
}