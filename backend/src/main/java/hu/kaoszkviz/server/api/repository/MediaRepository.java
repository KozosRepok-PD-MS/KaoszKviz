package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.MediaId;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends CrudRepository<Media, MediaId>{

    @Override
    public Optional<Media> findById(MediaId id);
    
}
