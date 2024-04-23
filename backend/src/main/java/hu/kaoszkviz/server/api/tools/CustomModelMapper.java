
package hu.kaoszkviz.server.api.tools;

import hu.kaoszkviz.server.api.config.ConfigDatas;
import hu.kaoszkviz.server.api.dto.AnswerDTO;
import hu.kaoszkviz.server.api.dto.DTO;
import hu.kaoszkviz.server.api.dto.QuestionDTO;
import hu.kaoszkviz.server.api.dto.QuizDTO;
import hu.kaoszkviz.server.api.dto.QuizPlayerDTO;
import hu.kaoszkviz.server.api.dto.QuizTopicDTO;
import hu.kaoszkviz.server.api.dto.UserDTO;
import hu.kaoszkviz.server.api.exception.InternalServerErrorException;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.model.Answer;
import hu.kaoszkviz.server.api.model.Media;
import hu.kaoszkviz.server.api.model.MediaId;
import hu.kaoszkviz.server.api.model.Model;
import hu.kaoszkviz.server.api.model.Question;
import hu.kaoszkviz.server.api.model.QuestionType;
import hu.kaoszkviz.server.api.model.Quiz;
import hu.kaoszkviz.server.api.model.QuizHistory;
import hu.kaoszkviz.server.api.model.QuizPlayer;
import hu.kaoszkviz.server.api.model.QuizTopic;
import hu.kaoszkviz.server.api.model.Topic;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.AnswerRepository;
import hu.kaoszkviz.server.api.repository.MediaRepository;
import hu.kaoszkviz.server.api.repository.QuestionRepository;
import hu.kaoszkviz.server.api.repository.QuestionTypeRepository;
import hu.kaoszkviz.server.api.repository.QuizHistoryRepository;
import hu.kaoszkviz.server.api.repository.QuizRepository;
import hu.kaoszkviz.server.api.repository.TopicRepository;
import hu.kaoszkviz.server.api.repository.UserRepository;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
    
    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private QuizHistoryRepository quizHistoryRepository;
    
    @Autowired
    private QuestionTypeRepository questionTypeRepository;
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
//    @Autowired
//    private 
    
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
        if (source instanceof User user && UserDTO.class.equals(destination)) {
            UserDTO userDTO = super.map(user, UserDTO.class);
            Media profilePicture = user.getProfilePicture();
            
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
        if (source instanceof Quiz quiz && QuizDTO.class.equals(destination)) {
            QuizDTO quizDTO = super.map(quiz, QuizDTO.class);
            Media mediaContent = quiz.getMediaContent();
            
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
        
        // <editor-fold defaultstate="collapsed" desc="QuizTopic custom map">
        if (source instanceof QuizTopic quizTopic && QuizTopicDTO.class.equals(destination)) {
            QuizTopicDTO quizTopicDTO = super.map(quizTopic, QuizTopicDTO.class);
            
            return (D) quizTopicDTO;
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="QuizPlayer custom map">
        if (source instanceof QuizPlayer quizPlayer && QuizPlayerDTO.class.equals(destination)) {
            QuizPlayerDTO quizPlayerDTO = super.map(quizPlayer, QuizPlayerDTO.class);
            User player = quizPlayer.getPlayer();
            
            if (player != null) {
                quizPlayerDTO.setPlayerId(player.getId());
            } else {
                quizPlayerDTO.setPlayerId(-1);
            }
            
            quizPlayerDTO.setQuizHistoryId(quizPlayer.getQuizHistory().getId());
            Media profilePicture = quizPlayer.getProfilePicture();
            
            if (profilePicture != null) {
                quizPlayerDTO.setProfilePictureOwnerId(profilePicture.getOwner().getId());
                quizPlayerDTO.setProfilePictureName(profilePicture.getFileName());
            } else {
                quizPlayerDTO.setProfilePictureOwnerId(-1);
                quizPlayerDTO.setProfilePictureName(null);
            }
            
            return (D) quizPlayerDTO;
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Question custom map">
        if (source instanceof Question question && QuestionDTO.class.equals(destination)) {
            QuestionDTO questionDTO = super.map(question, QuestionDTO.class);
            
            questionDTO.setQuizId(question.getQuiz().getId());
            questionDTO.setQuestionType(question.getQuestionType().getType());
            Media profilePicture = question.getMediaContent();
            
            if (profilePicture != null) {
                questionDTO.setMediaContentOwnerId(profilePicture.getOwner().getId());
                questionDTO.setMediaContentName(profilePicture.getFileName());
            } else {
                questionDTO.setMediaContentOwnerId(-1);
                questionDTO.setMediaContentName(null);
            }
            
            return (D) questionDTO;
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Answer custom map">
        if (source instanceof Answer answer && AnswerDTO.class.equals(destination)) {
            AnswerDTO answerDTO = super.map(answer, AnswerDTO.class);
            
            answerDTO.setQuestionId(answer.getQuestion().getId());
            if (answer.getQuestion().getQuestionType().getType().equals(ConfigDatas.PAIRING_QUESTION_TYPE)) {
                answerDTO.setCorrectAnswer(answerDTO.getCorrectAnswer());
                answerDTO.setCorrect(true);
            } else {
                answerDTO.setCorrectAnswer(null);
            }
            return (D) answerDTO;
        }
        // </editor-fold>
        
        return super.map(source, destination);
    }
    
    public <S extends DTO, D extends Model> D fromDTO(S source, Class<D> destinationClass) {
        Constructor<?> constructor;
        try {
            constructor = destinationClass.getDeclaredConstructor();
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new InternalServerErrorException();
        }
        
        D d;
        try {
            d = (D) constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new InternalServerErrorException();
        }
        
        this.fromDTO(source, d);
        return d;
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
        if (source instanceof UserDTO userDTO && destination instanceof User user) {
            super.map(userDTO, user);
            
            if ((userDTO.getProfilePictureName() == null || userDTO.getProfilePictureName().isBlank()) || userDTO.getProfilePictureOwnerId() == -1) {                
                user.setProfilePicture(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(userDTO.getProfilePictureOwnerId()).orElseThrow(() -> new NotFoundException(User.class, userDTO.getProfilePictureOwnerId()));
                user.setProfilePicture(this.findMediaById(owner, userDTO.getProfilePictureName()));
            }
            
            return;
        } 
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="QuizDTO custom map">
        if (source instanceof QuizDTO quizDTO && destination instanceof Quiz quiz) {
            super.map(quizDTO, quiz);
            
            if ((quizDTO.getMediaContentName() == null || quizDTO.getMediaContentName().isBlank()) || quizDTO.getMediaContentOwner()== -1) {
                quiz.setMediaContent(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(quizDTO.getMediaContentOwner()).orElseThrow(() -> new NotFoundException(User.class, quizDTO.getMediaContentOwner()));
                quiz.setMediaContent(this.findMediaById(owner, quizDTO.getMediaContentName()));
            }
            return;
        }
        // </editor-fold>
 
        // <editor-fold defaultstate="collapsed" desc="QuizTopicDTO custom map">
        if (source instanceof QuizTopicDTO quizTopicDTO && destination instanceof QuizTopic quizTopic) {
            
            quizTopic.setQuiz(this.quizRepository.findById(quizTopicDTO.getQuizId()).orElseThrow(() -> new NotFoundException(Quiz.class, quizTopicDTO.getQuizId())));
            quizTopic.setTopic(this.topicRepository.findById(quizTopicDTO.getTopicId()).orElseThrow(() -> new NotFoundException(Topic.class, quizTopicDTO.getTopicId())));
            return;
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="QuizPlayerDTO custom map">
        if (source instanceof QuizPlayerDTO quizPlayerDTO && destination instanceof QuizPlayer quizPlayer) {
            super.map(quizPlayerDTO, quizPlayer);
            
            if (quizPlayerDTO.getPlayerId() != -1) {
                quizPlayer.setPlayer(this.userRepository.findById(quizPlayerDTO.getPlayerId()).orElseThrow(() -> new NotFoundException(User.class, quizPlayerDTO.getPlayerId())));
            } else {
               quizPlayer.setPlayer(null);
            }
            quizPlayer.setQuizHistory(this.quizHistoryRepository.findById(quizPlayerDTO.getQuizHistoryId()).orElseThrow(() -> new NotFoundException(QuizHistory.class, quizPlayerDTO.getQuizHistoryId())));
            
            if ((quizPlayerDTO.getProfilePictureName() == null || quizPlayerDTO.getProfilePictureName().isBlank()) || quizPlayerDTO.getProfilePictureOwnerId() == -1) {                
                quizPlayer.setProfilePicture(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(quizPlayerDTO.getProfilePictureOwnerId()).orElseThrow(() -> new NotFoundException(User.class, quizPlayerDTO.getProfilePictureOwnerId()));
                quizPlayer.setProfilePicture(this.findMediaById(owner, quizPlayerDTO.getProfilePictureName()));
            }
            
            return;
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="QuestionDTO custom map">
        if (source instanceof QuestionDTO questionDTO && destination instanceof Question question) {
            super.map(questionDTO, question);
            
            question.setQuiz(this.quizRepository.findById(questionDTO.getQuizId()).orElseThrow(() -> new NotFoundException(QuizHistory.class, questionDTO.getQuizId())));
            question.setQuestionType(this.questionTypeRepository.findById(questionDTO.getQuestionType()).orElseThrow(() -> new NotFoundException(QuestionType.class, questionDTO.getQuestionType())));
            
            if ((questionDTO.getMediaContentName()== null || questionDTO.getMediaContentName().isBlank()) || questionDTO.getMediaContentOwnerId()== -1) {                
                question.setMediaContent(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(questionDTO.getMediaContentOwnerId()).orElseThrow(() -> new NotFoundException(User.class, questionDTO.getMediaContentOwnerId()));
                question.setMediaContent(this.findMediaById(owner, questionDTO.getMediaContentName()));
            }
            
            return;
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="AnswerDTO custom map">
        if (source instanceof AnswerDTO answerDTO && destination instanceof Answer answer) {
            super.map(answerDTO, answer);
            
            answer.setQuestion(this.questionRepository.findById(answerDTO.getQuestionId()).orElseThrow(() -> new NotFoundException(Question.class, answerDTO.getQuestionId())));
            if (answer.getQuestion().getQuestionType().getType().equals(ConfigDatas.PAIRING_QUESTION_TYPE)) {
                answer.setCorrectAnswer(answerDTO.getCorrectAnswer());
                answer.setCorrect(true);
            } else {
                answer.setCorrectAnswer(null);
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
        if (source instanceof UserDTO userDTO && destination instanceof User user) {
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            
            if ((userDTO.getProfilePictureName() == null || userDTO.getProfilePictureName().isBlank()) || userDTO.getProfilePictureOwnerId() == -1) {                
                user.setProfilePicture(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(userDTO.getProfilePictureOwnerId()).orElseThrow(() -> new NotFoundException(User.class, userDTO.getProfilePictureOwnerId()));
                user.setProfilePicture(this.findMediaById(owner, userDTO.getProfilePictureName()));
            }
            
            return;
        } 
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="QuizDTO custom map">
        if (source instanceof QuizDTO quizDTO && destination instanceof Quiz quiz) {
            
            quiz.setDescription(quizDTO.getDescription());
            quiz.setPublic(quizDTO.isPublic());
            quiz.setShortAccessibleName(quizDTO.getShortAccessibleName());
            quiz.setTitle(quizDTO.getTitle());
            // ! TODO
//            updateQuiz.setTopics(quiz.getTopics());
            
            if ((quizDTO.getMediaContentName() == null || quizDTO.getMediaContentName().isBlank()) || quizDTO.getMediaContentOwner()== -1) {
                quiz.setMediaContent(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(quizDTO.getMediaContentOwner()).orElseThrow(() -> new NotFoundException(User.class, quizDTO.getMediaContentOwner()));
                quiz.setMediaContent(this.findMediaById(owner, quizDTO.getMediaContentName()));
            }
            return;
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="QuestionDTO custom map">
        if (source instanceof QuestionDTO questionDTO && destination instanceof Question question) {
            
            question.setQuestion(questionDTO.getQuestion());
            question.setTimeToAnswer(questionDTO.getTimeToAnswer());
            
            if ((questionDTO.getMediaContentName()== null || questionDTO.getMediaContentName().isBlank()) || questionDTO.getMediaContentOwnerId()== -1) {                
                question.setMediaContent(null);
            } else {
                //ha jók a kép adatai
                User owner = this.userRepository.findById(questionDTO.getMediaContentOwnerId()).orElseThrow(() -> new NotFoundException(User.class, questionDTO.getMediaContentOwnerId()));
                question.setMediaContent(this.findMediaById(owner, questionDTO.getMediaContentName()));
            }
            
            return;
        }
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="AnswerDTO custom map">
        if (source instanceof AnswerDTO answerDTO && destination instanceof Answer answer) {
            
            answer.setAnswer(answerDTO.getAnswer());
            answer.setCorrect(answerDTO.isCorrect());
            if (answer.getQuestion().getQuestionType().getType().equals(ConfigDatas.PAIRING_QUESTION_TYPE)) {
                answer.setCorrectAnswer(answerDTO.getCorrectAnswer());
                answer.setCorrect(true);
            } else {
                answer.setCorrectAnswer(null);
            }
            answer.setQuestion(this.questionRepository.findById(answerDTO.getQuestionId()).orElseThrow(() -> new NotFoundException(Question.class, answerDTO.getQuestionId())));
            
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
    
    public <S extends DTO, D extends Model> List<D> fromDTOList(List<S> sources, Class<D> destination) {   
        List<D> destinationList = new ArrayList<>();
        
        if (!sources.isEmpty()) {
            for (S source : sources) {
                destinationList.add(this.fromDTO(source, destination));
            }
        }
        return destinationList;
    }
    
    public Media findMediaById(User owner, String mediaName) {
        return this.mediaRepository.findById(new MediaId(owner, mediaName)).orElseThrow(() -> new NotFoundException(Media.class, ""));
    }
}
