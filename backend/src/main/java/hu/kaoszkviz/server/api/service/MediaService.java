package hu.kaoszkviz.server.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hu.kaoszkviz.server.api.repository.MediaRepository;

@Service
public class MediaService {
    
    @Autowired
    private MediaRepository mediaRepository;
}