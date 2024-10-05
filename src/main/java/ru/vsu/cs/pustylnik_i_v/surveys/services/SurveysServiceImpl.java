package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.*;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.*;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.util.Calendar;
import java.util.List;

public class SurveysServiceImpl implements SurveysService {

    private static final int maxPageElementsAmount = 7;

    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final SessionRepository sessionRepository;

    public SurveysServiceImpl(UserRepository userRepository,
                              SurveyRepository surveyRepository,
                              QuestionRepository questionRepository,
                              OptionRepository optionRepository,
                              AnswerRepository answerRepository,
                              CategoryRepository categoryRepository,
                              SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.answerRepository = answerRepository;
        this.categoryRepository = categoryRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public ResponseEntity<PagedEntity<List<Survey>>> getSurveysPagedList(Integer categoryId, Integer page) {
        List<Survey> sliced;

        List<Survey> surveys = surveyRepository.getSurveys(categoryId);
        int totalPages = (int) Math.ceil((double) surveys.size() / 7);

        int fromIndex = maxPageElementsAmount * (page - 1);
        int toIndex = Math.min(fromIndex + maxPageElementsAmount, surveys.size());

        sliced = surveys.subList(fromIndex, toIndex);

        if (totalPages < 1) totalPages = 1;

        return new ResponseEntity<>(true, "Surveys successfully found", new PagedEntity<>(page, totalPages, sliced));
    }

    @Override
    public ResponseEntity<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer index) {
        List<Question> questions = questionRepository.getQuestions(surveyId);
        if (index >= questions.size()) {
            return new ResponseEntity<>(true, "Survey completed successfully", null);
        }
        Question question = questions.get(index);
        return new ResponseEntity<>(true, "Question successfully found", new PagedEntity<>(index, questions.size(), question));
    }

    @Override
    public ResponseEntity<List<Option>> getQuestionOptionList(Integer questionId) {
        List<Option> options = optionRepository.getOptions(questionId);
        if (options.isEmpty()) {
            return new ResponseEntity<>(true, "No questions found", null);
        }
        return new ResponseEntity<>(true, "Questions found successfully", options);
    }

    @Override
    public ResponseEntity<?> submitAnswer(Integer sessionId, Integer optionId) {
        try {
            answerRepository.addAnswer(sessionId, optionId);
        } catch (SessionNotFoundException e) {
            return new ResponseEntity<>(false, "Session doesn't exist", null);
        } catch (OptionNotFoundException e) {
            return new ResponseEntity<>(false, "Option doesn't exist", null);
        }
        return new ResponseEntity<>(true, "Answer submitted successfully", null);
    }

    @Override
    public ResponseEntity<?> deleteSurvey(Integer surveyId) {
        try {
            surveyRepository.deleteSurvey(surveyId);
            return new ResponseEntity<>(true, "Survey deleted successfully", null);
        } catch (SurveyNotFoundException e) {
            return new ResponseEntity<>(false, "Survey doesn't exist", null);
        }
    }

    @Override
    public ResponseEntity<Integer> addSurveyAndGetId(String name, String description, String categoryName) {
        if (!categoryRepository.exists(categoryName)) {
            categoryRepository.addCategory(categoryName);
        }

        Integer id = categoryRepository.getCategoryByName(categoryName).getId();
        surveyRepository.addSurvey(name, description, id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(true, "Survey created successfully", null);
    }

    @Override
    public ResponseEntity<Integer> startSessionAndGetId(String userName, Integer surveyId) {
        try {
            Integer userId = userRepository.getUser(userName).getId();
            Integer sessionId = sessionRepository.addSessionAndGetId(surveyId, userId, Calendar.getInstance().getTime(), null);
            return new ResponseEntity<>(true, "Successfully created a session", sessionId);
        } catch (SurveyNotFoundException e) {
            return new ResponseEntity<>(false, "Survey doesn't exist", null);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
    }
}
