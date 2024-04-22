
package hu.kaoszkviz.server.api.tools;

import hu.kaoszkviz.server.api.dto.DTO;
import hu.kaoszkviz.server.api.dto.QuizDTO;
import hu.kaoszkviz.server.api.dto.UserDTO;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.MediaId;
import hu.kaoszkviz.server.api.model.Model;
import hu.kaoszkviz.server.api.model.Quiz;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.MediaRepository;
import hu.kaoszkviz.server.api.repository.QuizRepository;
import hu.kaoszkviz.server.api.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomModelMapper extends ModelMapper {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private MediaRepository mediaRepository;
    
    /**
     * Model to DTO.
     * @param <S> Source model
     * @param <D> Destination class
     * @param source
     * @param destination
     * @return DTO of the given Entity.
     */
    public <S extends Model, D extends DTO> D fromModel(S source, Class<D> destination) {
        
        // <editor-fold defaultstate="collapsed" desc="User custom map">
        if (source instanceof User && UserDTO.class.equals(destination)) {
            User src = (User) source;
            UserDTO userDTO = super.map(src, UserDTO.class);
            Media profilePicture = src.getProfilePicture();
            
            if (profilePicture != null) {
                userDTO.setProfilePictureOwnerId(profilePicture.getOwner().getId());
                userDTO.setProfilePictureName(profilePicture.getFileName());
            } else {
                userDTO.setProfilePictureOwnerId(-1);
                userDTO.setProfilePictureName(null);
            }
            
            return (D) userDTO;
        }
        // </editor-fold>
                
        // <editor-fold defaultstate="collapsed" desc="Quiz custom map">
        if (source instanceof Quiz && QuizDTO.class.equals(destination)) {
            Quiz src = (Quiz) source;
            QuizDTO quizDTO = super.map(src, QuizDTO.class);
            Media mediaContent = src.getMediaContent();
            
            if (mediaContent != null) {
                quizDTO.setMediaContentOwner(mediaContent.getOwner().getId());
                quizDTO.setMediaContentName(mediaContent.getFileName());
            } else {
                quizDTO.setMediaContentOwner(-1);
                quizDTO.setMediaContentName(null);
            }
            return (D) quizDTO;
        }
        // </editor-fold>
        
        return super.map(source, destination);
    }
    
    /**
     * DTO to Model.
     * @param <S> Source DTO
     * @param <D> Destination model
     * @param source 
     * @param destination
     * @return 
     */
    public <S extends DTO, D extends Model> void fromDTO(S source, D destination) {
        // <editor-fold defaultstate="collapsed" desc="UserDTO custom map">
        if (source instanceof UserDTO && destination instanceof User) {
            UserDTO src = (UserDTO) source;
            super.map(source, destination);
            
            if ((src.getProfilePictureName() == null || src.getProfilePictureName().isBlank()) || src.getProfilePictureOwnerId() == -1) {                
                ((User) destination).setProfilePicture(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(src.getProfilePictureOwnerId()).orElseThrow(() -> new NotFoundException("user not found id:%d".formatted(src.getProfilePictureOwnerId())));
                ((User) destination).setProfilePicture(this.findMediaById(owner, src.getProfilePictureName()));
            }
            
            return;
        } 
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="QuizDTO custom map">
        if (source instanceof QuizDTO && destination instanceof Quiz) {
            QuizDTO src = (QuizDTO) source;
            super.map(src, destination);
            
            if ((src.getMediaContentName() == null || src.getMediaContentName().isBlank()) || src.getMediaContentOwner()== -1) {
                ((Quiz) destination).setMediaContent(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(src.getMediaContentOwner()).orElseThrow(() -> new NotFoundException("user not found id:%d".formatted(src.getMediaContentOwner())));
                ((Quiz) destination).setMediaContent(this.findMediaById(owner, src.getMediaContentName()));
            }
            return;
        }
        // </editor-fold>

        super.map(source, destination);
    }
    
    /**
     * Updates a Model from the given DTO
     * @param <S> Source DTO
     * @param <D> Model to update
     * @param source
     * @param destination 
     */
    public <S extends DTO, D extends Model> void updateFromDTO(S source, D destination) {
        // <editor-fold defaultstate="collapsed" desc="UserDTO custom map">
        if (source instanceof UserDTO && destination instanceof User) {
            UserDTO src = (UserDTO) source;
            
            ((User) destination).setUsername(src.getUsername());
            ((User) destination).setEmail(src.getEmail());
            
            if ((src.getProfilePictureName() == null || src.getProfilePictureName().isBlank()) || src.getProfilePictureOwnerId() == -1) {                
                ((User) destination).setProfilePicture(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(src.getProfilePictureOwnerId()).orElseThrow(() -> new NotFoundException("user not found id:%d".formatted(src.getProfilePictureOwnerId())));
                ((User) destination).setProfilePicture(this.findMediaById(owner, src.getProfilePictureName()));
            }
            
            return;
        } 
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="QuizDTO custom map">
        if (source instanceof QuizDTO && destination instanceof Quiz) {
            QuizDTO src = (QuizDTO) source;
            
            ((Quiz) destination).setDescription(src.getDescription());
            ((Quiz) destination).setPublic(src.isPublic());
            ((Quiz) destination).setShortAccessibleName(src.getShortAccessibleName());
            ((Quiz) destination).setTitle(src.getTitle());
            // ! TODO
//            updateQuiz.setTopics(quiz.getTopics());
            
            if ((src.getMediaContentName() == null || src.getMediaContentName().isBlank()) || src.getMediaContentOwner()== -1) {
                ((Quiz) destination).setMediaContent(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(src.getMediaContentOwner()).orElseThrow(() -> new NotFoundException("user not found id:%d".formatted(src.getMediaContentOwner())));
                ((Quiz) destination).setMediaContent(this.findMediaById(owner, src.getMediaContentName()));
            }
            return;
        }
        // </editor-fold>

        super.map(source, destination);
    }
    
    public <S> List<Object> map(List<S> sources) {
        return this.map(sources, Object.class);
    }
    
    public <S, D> List<D> map(List<S> sources, Class<D> destinationType) {
        List<D> modelDTOS = new ArrayList<>();
        
        if (!sources.isEmpty()) {
            for (S source : sources) {
                D mapped = (D) this.map(source, destinationType);
                modelDTOS.add(mapped);
            }
        }

        return modelDTOS;
    }
    
    public <S extends Model, D extends DTO> List<D> fromModelList(List<S> sources, Class<D> destinationType) {
        List<D> modelDTOS = new ArrayList<>();
        
        if (!sources.isEmpty()) {
            for (S source : sources) {
                modelDTOS.add(this.fromModel(source, destinationType));
            }
        }

        return modelDTOS;
    }
    
    public Media findMediaById(User owner, String mediaName) {
        return this.mediaRepository.findById(new MediaId(owner, mediaName)).orElseThrow(() -> new NotFoundException("media not found"));
    }
}
