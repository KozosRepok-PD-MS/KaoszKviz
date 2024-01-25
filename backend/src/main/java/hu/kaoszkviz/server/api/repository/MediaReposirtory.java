package hu.kaoszkviz.server.api.repository;

import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.MediaId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaReposirtory extends CrudRepository<Media, MediaId>{
    
}
