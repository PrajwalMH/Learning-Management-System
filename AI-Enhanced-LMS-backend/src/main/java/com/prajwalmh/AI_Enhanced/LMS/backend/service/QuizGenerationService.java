package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.QuizGenerateRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.QuizQuestionResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.QuizResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.*;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseModuleRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.QuizQuestionRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.QuizRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizGenerationService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final CourseRepository courseRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final UserRepository userRepository;

    public QuizResponse generateQuiz(QuizGenerateRequest request) {

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));

        CourseModule module = null;

        if (request.getModuleId() != null) {
            module = courseModuleRepository.findById(request.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found with id: " + request.getModuleId()));
        }

        User teacher = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + request.getCreatedById()));

        if (teacher.getRole() != Role.TEACHER && teacher.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only teacher or admin can generate quizzes");
        }

        Quiz quiz = Quiz.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .topic(request.getTopic())
                .aiGenerated(true)
                .published(false)
                .course(course)
                .module(module)
                .createdBy(teacher)
                .build();

        Quiz savedQuiz = quizRepository.save(quiz);

        int questionCount = request.getNumberOfQuestions() != null && request.getNumberOfQuestions() > 0
                ? Math.min(request.getNumberOfQuestions(), 10)
                : 5;

        List<QuizQuestion> generatedQuestions = generateRuleBasedQuestions(
                savedQuiz,
                request.getTopic(),
                questionCount
        );

        quizQuestionRepository.saveAll(generatedQuestions);

        return mapToResponse(savedQuiz, true);
    }

    public List<QuizResponse> getQuizzesByCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        return quizRepository.findByCourse(course)
                .stream()
                .map(quiz -> mapToResponse(quiz, false))
                .toList();
    }

    public QuizResponse getQuizById(Long quizId) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        return mapToResponse(quiz, true);
    }

    public QuizResponse publishQuiz(Long quizId) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        quiz.setPublished(true);
        quiz.setPublishedAt(LocalDateTime.now());

        Quiz updatedQuiz = quizRepository.save(quiz);

        return mapToResponse(updatedQuiz, true);
    }

    public void deleteQuiz(Long quizId) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        List<QuizQuestion> questions = quizQuestionRepository.findByQuizOrderByQuestionOrderAsc(quiz);
        quizQuestionRepository.deleteAll(questions);

        quizRepository.delete(quiz);
    }

    private List<QuizQuestion> generateRuleBasedQuestions(Quiz quiz, String topic, int count) {

        List<QuizQuestion> questions = new ArrayList<>();

        for (int i = 1; i <= count; i++) {

            QuizQuestion question = QuizQuestion.builder()
                    .quiz(quiz)
                    .questionOrder(i)
                    .marks(1.0)
                    .questionText(buildQuestionText(topic, i))
                    .optionA(buildOptionA(topic, i))
                    .optionB(buildCorrectOption(topic, i))
                    .optionC(buildOptionC(topic, i))
                    .optionD(buildOptionD(topic, i))
                    .correctAnswer("B")
                    .explanation("The correct answer is B because it best matches the core concept of " + topic + ".")
                    .build();

            questions.add(question);
        }

        return questions;
    }

    private String buildQuestionText(String topic, int index) {
        return switch (index) {
            case 1 -> "What is the main purpose of " + topic + "?";
            case 2 -> "Which statement best describes " + topic + "?";
            case 3 -> "Why is " + topic + " important in software development?";
            case 4 -> "Which of the following is a common use case of " + topic + "?";
            case 5 -> "What is a key benefit of learning " + topic + "?";
            default -> "Which option is most accurate about " + topic + "?";
        };
    }

    private String buildOptionA(String topic, int index) {
        return switch (index) {
            case 1 -> "To remove all application logic";
            case 2 -> "It is only used for frontend styling";
            case 3 -> "It has no impact on application quality";
            case 4 -> "It is used only for database backup";
            case 5 -> "It makes testing unnecessary";
            default -> "It is unrelated to application development";
        };
    }

    private String buildCorrectOption(String topic, int index) {
        return switch (index) {
            case 1 -> "To improve understanding and implementation of " + topic;
            case 2 -> topic + " is an important concept used to build reliable applications";
            case 3 -> "It helps developers build better, more maintainable systems";
            case 4 -> "It can be used to solve real-world application problems";
            case 5 -> "It improves practical knowledge and technical confidence";
            default -> "It supports better software design and development";
        };
    }

    private String buildOptionC(String topic, int index) {
        return switch (index) {
            case 1 -> "To avoid using APIs";
            case 2 -> "It is only useful for graphic design";
            case 3 -> "It reduces the need to understand backend systems";
            case 4 -> "It is used only for changing font colors";
            case 5 -> "It prevents developers from using databases";
            default -> "It is only used in non-technical fields";
        };
    }

    private String buildOptionD(String topic, int index) {
        return switch (index) {
            case 1 -> "To delete all course resources";
            case 2 -> "It is not related to programming";
            case 3 -> "It is useful only for documentation formatting";
            case 4 -> "It only applies to image editing";
            case 5 -> "It removes the need for project architecture";
            default -> "It has no real-world use";
        };
    }

    private QuizResponse mapToResponse(Quiz quiz, boolean includeQuestions) {

        Course course = quiz.getCourse();
        CourseModule module = quiz.getModule();
        User createdBy = quiz.getCreatedBy();

        List<QuizQuestionResponse> questionResponses = null;

        if (includeQuestions) {
            questionResponses = quizQuestionRepository.findByQuizOrderByQuestionOrderAsc(quiz)
                    .stream()
                    .map(this::mapQuestionToResponse)
                    .toList();
        }

        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .topic(quiz.getTopic())
                .aiGenerated(quiz.isAiGenerated())
                .published(quiz.isPublished())
                .courseId(course != null ? course.getId() : null)
                .courseTitle(course != null ? course.getTitle() : null)
                .moduleId(module != null ? module.getId() : null)
                .moduleTitle(module != null ? module.getTitle() : null)
                .createdById(createdBy != null ? createdBy.getId() : null)
                .createdByName(createdBy != null ? createdBy.getFullName() : null)
                .createdAt(quiz.getCreatedAt())
                .publishedAt(quiz.getPublishedAt())
                .questions(questionResponses)
                .build();
    }

    private QuizQuestionResponse mapQuestionToResponse(QuizQuestion question) {

        return QuizQuestionResponse.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .optionA(question.getOptionA())
                .optionB(question.getOptionB())
                .optionC(question.getOptionC())
                .optionD(question.getOptionD())
                .correctAnswer(question.getCorrectAnswer())
                .explanation(question.getExplanation())
                .questionOrder(question.getQuestionOrder())
                .marks(question.getMarks())
                .build();
    }
}