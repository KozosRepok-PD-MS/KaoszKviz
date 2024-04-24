
package hu.kaoszkviz.server.api.controller;

import hu.kaoszkviz.server.api.dto.MediaPayload;
import hu.kaoszkviz.server.api.service.MediaService;
import hu.kaoszkviz.server.api.tools.ErrorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/media")
public class MediaController {
    
    @Autowired
    private MediaService mediaService;
    
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<String> addMedia(@ModelAttribute MediaPayload mediaPayload) {
        if (mediaPayload.getFile() == null) { return ErrorManager.notFound("media"); }
        
        if (mediaPayload.getFilename() == null) { return ErrorManager.notFound("media name"); }

        return this.mediaService.addMedia(mediaPayload.getFile(), mediaPayload.getFilename());
    }
    
    
    @GetMapping(value = "/{owner}/{mediaName}")
    public ResponseEntity<byte[]> getMedia(
            @PathVariable(value = "owner") long owner,
            @PathVariable(value = "mediaName") String fileName
    ) {
        return this.mediaService.getMedia(owner, fileName);
    }
}
