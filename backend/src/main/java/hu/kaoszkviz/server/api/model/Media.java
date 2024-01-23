package hu.kaoszkviz.server.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = Media.TABLE_NAME)
public class Media {
    
    @Id
    @Setter
    @Getter
    private User owner;
    
    @Id
    @Setter
    @Getter
    @Column(nullable = false, columnDefinition = "nvarchar(30)", length = 30, name = "file_name")
    private String fileName;
    
    @Setter
    @Getter
    @Column(nullable = false, columnDefinition = "varbinary("+Media.MAX_FIE_SIZE+")")
    private byte[] data;
    
    public static final String TABLE_NAME = "media";
    public static final long MAX_FIE_SIZE = 5_000_000;
}
