package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.MediaId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends CrudRepository<Media, MediaId>{
    
    @Query(value = "SELECT * FROM dbo.findMediaByOwnerIdAndFileName(:owner_id, :file_name)", nativeQuery = true)
    public Media searchMediaByOwnerIdAndFileName(@Param("owner_id") long ownerId, @Param("file_name") String fileName);
}
