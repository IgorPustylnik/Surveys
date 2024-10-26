package ru.vsu.cs.pustylnik_i_v.surveys.services.impl;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.*;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.*;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.util.Calendar;
import java.util.List;

public class SurveysServiceImpl implements SurveysService {

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
    public ResponseEntity<PagedEntity<List<Survey>>> getSurveysPagedList(Integer categoryId, Integer page, Integer perPageAmount) {
        PagedEntity<List<Survey>> surveysPagedEntity = surveyRepository.getSurveysPagedEntity(categoryId, page, perPageAmount);
        if (surveysPagedEntity.getPage().isEmpty()) {
            return new ResponseEntity<>(false, "Surveys not found", surveysPagedEntity);
        }
        return new ResponseEntity<>(true, "Surveys successfully found", surveysPagedEntity);
    }

    @Override
    public ResponseEntity<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer index) {
        List<Question> questions = questionRepository.getQuestions(surveyId);
        if (index >= questions.size()) {
            return new ResponseEntity<>(false, "Survey completed successfully", null);
        }
        Question question = questions.get(index);
        return new ResponseEntity<>(true, "Question successfully found", new PagedEntity<>(index, questions.size(), question));
    }

    @Override
    public ResponseEntity<List<Option>> getQuestionOptionList(Integer questionId) {
        List<Option> options = optionRepository.getOptions(questionId);
        if (options.isEmpty()) {
            return new ResponseEntity<>(true, "No options found", null);
        }
        return new ResponseEntity<>(true, "Options found successfully", options);
    }

    @Override
    public ResponseEntity<PagedEntity<List<Category>>> getCategoriesPagedList(Integer page, Integer perPageAmount) {
        List<Category> sliced;

        List<Category> surveys = categoryRepository.getAllCategories();
        int totalPages = (int) Math.ceil((double) surveys.size() / perPageAmount);

        int fromIndex = perPageAmount * page;
        int toIndex = Math.min(fromIndex + perPageAmount, surveys.size());

        sliced = surveys.subList(fromIndex, toIndex);

        if (totalPages < 1) totalPages = 1;

        if (sliced.isEmpty()) {
            return new ResponseEntity<>(false, "No categories found", new PagedEntity<>(page, totalPages, sliced));
        }

        return new ResponseEntity<>(true, "Categories successfully found", new PagedEntity<>(page, totalPages, sliced));
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
    public ResponseEntity<?> addQuestionToSurvey(Integer surveyId, String description, List<String> options, QuestionType questionType) {
        questionRepository.addQuestion(surveyId, description, questionType);

        List<Question> questions = questionRepository.getQuestions(surveyId);
        if (questions.isEmpty()) {
            return new ResponseEntity<>(false, "Error (couldn't create a question)", null);
        }
        Question question = questions.get(questions.size() - 1);

        for (String option : options) {
            optionRepository.addOption(question.getId(), option);
        }

        return new ResponseEntity<>(true, "Question created successfully", null);
    }

    @Override
    public ResponseEntity<String> getCategoryName(Integer categoryId) {
        try {
            return new ResponseEntity<>(true, "Category name found successfully", categoryRepository.getCategoryById(categoryId).getName());
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(false, "Category doesn't exist", null);
        }
    }

    @Override
    public ResponseEntity<?> deleteCategory(Integer categoryId) {
        try {
            categoryRepository.deleteCategory(categoryId);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(false, "Category to delete was not found", null);
        }
        return new ResponseEntity<>(true, "Category deleted successfully", null);
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
    public ResponseEntity<Survey> addSurveyAndGetSelf(String name, String description, String categoryName) {
        if (!categoryRepository.exists(categoryName)) {
            categoryRepository.addCategory(categoryName);
        }

        Integer categoryId = categoryRepository.getCategoryByName(categoryName).getId();
        Survey newSurvey = surveyRepository.addSurvey(name, description, categoryId, Calendar.getInstance().getTime());

        return new ResponseEntity<>(true, "Survey created successfully", newSurvey);
    }

    @Override
    public ResponseEntity<?> setSurveyCategory(Integer surveyId, String categoryName) {
        if (!categoryRepository.exists(categoryName)) {
            categoryRepository.addCategory(categoryName);
        }

        Integer categoryId = categoryRepository.getCategoryByName(categoryName).getId();
        try {
            surveyRepository.updateSurveyCategoryName(surveyId, categoryId);
        } catch (SurveyNotFoundException e) {
            return new ResponseEntity<>(false, "Survey doesn't exist", null);
        }

        return new ResponseEntity<>(true, "Category set successfully", null);
    }

    @Override
    public ResponseEntity<Integer> startSessionAndGetId(String userName, Integer surveyId) {
        Integer userId = null;

        try {
            userId = userRepository.getUser(userName).getId();
        } catch (UserNotFoundException ignored) {
        }

        try {
            surveyRepository.getSurveyById(surveyId);
        } catch (SurveyNotFoundException e) {
            return new ResponseEntity<>(false, "Survey doesn't exist", null);
        }

        Integer sessionId = sessionRepository.addSessionAndGetId(surveyId, userId, Calendar.getInstance().getTime(), null);
        return new ResponseEntity<>(true, "Successfully created a session", sessionId);
    }

    @Override
    public ResponseEntity<?> finishSession(Integer sessionId) {
        try {
            Session session = sessionRepository.getSessionById(sessionId);
            session.setFinishedAt(Calendar.getInstance().getTime());
            sessionRepository.updateSession(session);
            return new ResponseEntity<>(true, "Session finished successfully", null);
        } catch (SessionNotFoundException e) {
            return new ResponseEntity<>(false, "Session doesn't exist", null);
        }
    }
}
