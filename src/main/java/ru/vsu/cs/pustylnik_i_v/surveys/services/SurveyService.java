package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.*;
import ru.vsu.cs.pustylnik_i_v.surveys.json.EditOptionDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.json.EditQuestionDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.json.EditSurveyDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SurveyService {

    private final UserDAO userDAO;
    private final SurveyDAO surveyDAO;
    private final QuestionDAO questionDAO;
    private final OptionDAO optionDAO;
    private final CategoryDAO categoryDAO;

    public SurveyService(UserDAO userDAO,
                         SurveyDAO surveyDAO,
                         QuestionDAO questionDAO,
                         OptionDAO optionDAO,
                         CategoryDAO categoryDAO) {
        this.userDAO = userDAO;
        this.surveyDAO = surveyDAO;
        this.questionDAO = questionDAO;
        this.optionDAO = optionDAO;
        this.categoryDAO = categoryDAO;
    }

    public ServiceResponse<PagedEntity<List<Survey>>> getSurveysPagedList(String authorName, Integer categoryId, Date fromDate, Date toDate, Integer page, Integer perPageAmount) throws DatabaseAccessException {
        PagedEntity<List<Survey>> surveysPagedEntity;

        fromDate = fromDate == null ? null : DateUtil.setTimeToStartOfDay(fromDate);
        toDate = toDate == null ? null : DateUtil.setTimeToEndOfDay(toDate);

        surveysPagedEntity = surveyDAO.getSurveysPagedEntity(authorName, categoryId, fromDate, toDate, page, perPageAmount);

        if (surveysPagedEntity.page().isEmpty()) {
            return new ServiceResponse<>(false, "Nothing found", surveysPagedEntity);
        }
        return new ServiceResponse<>(true, "Surveys successfully found", surveysPagedEntity);
    }

    public ServiceResponse<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer index) throws DatabaseAccessException {
        try {
            List<Question> questions = questionDAO.getQuestions(surveyId);

            if (index >= questions.size()) {
                return new ServiceResponse<>(false, "Survey completed successfully", null);
            }
            Question question = questions.get(index);

            return new ServiceResponse<>(true, "Question successfully found", new PagedEntity<>(index, questions.size(), question));
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey not found", null);
        }
    }

    public ServiceResponse<List<Question>> getQuestions(Integer surveyId) throws DatabaseAccessException {
        List<Question> questions;
        try {
            questions = questionDAO.getQuestions(surveyId);
            return new ServiceResponse<>(true, "Questions successfully found", questions);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey not found", null);
        }
    }

    public ServiceResponse<PagedEntity<List<Category>>> getCategoriesPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException {
        List<Category> sliced;
        List<Category> categories = categoryDAO.getAllCategories();

        int totalPages = (int) Math.ceil((double) categories.size() / perPageAmount);

        int fromIndex = perPageAmount * page;
        int toIndex = Math.min(fromIndex + perPageAmount, categories.size());

        sliced = categories.subList(fromIndex, toIndex);

        if (totalPages < 1) totalPages = 1;

        if (sliced.isEmpty()) {
            return new ServiceResponse<>(false, "No categories found", new PagedEntity<>(page, totalPages, sliced));
        }

        return new ServiceResponse<>(true, "Categories successfully found", new PagedEntity<>(page, totalPages, sliced));
    }

    public ServiceResponse<?> addQuestionToSurvey(Integer surveyId, String description, List<String> options, QuestionType questionType) throws DatabaseAccessException {
        try {
            questionDAO.addQuestion(surveyId, description, questionType, options);

            List<Question> questions = questionDAO.getQuestions(surveyId);
            if (questions.isEmpty()) {
                return new ServiceResponse<>(false, "Error (couldn't create a question)", null);
            }

        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(true, "Survey doesn't exist", null);
        }

        return new ServiceResponse<>(true, "Question created successfully", null);
    }

    public ServiceResponse<?> deleteCategory(Integer categoryId) throws DatabaseAccessException {
        try {
            categoryDAO.deleteCategory(categoryId);
        } catch (CategoryNotFoundException e) {
            return new ServiceResponse<>(false, "Category to delete was not found", null);
        }
        return new ServiceResponse<>(true, "Category deleted successfully", null);
    }

    public ServiceResponse<Survey> getSurvey(Integer surveyId) throws DatabaseAccessException {
        try {
            Survey survey = surveyDAO.getSurveyById(surveyId);
            return new ServiceResponse<>(true, "Successfully found survey", survey);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey not found", null);
        }
    }

    public ServiceResponse<?> deleteSurvey(Integer surveyId) throws DatabaseAccessException {
        surveyDAO.deleteSurvey(surveyId);
        return new ServiceResponse<>(true, "Survey deleted successfully", null);
    }

    public ServiceResponse<Survey> addSurveyAndGetSelf(String name, String description, String categoryName, Integer authorId) throws DatabaseAccessException {
        if (categoryName != null && !categoryDAO.exists(categoryName)) {
            categoryDAO.addCategory(categoryName);
        }

        Integer categoryId = null;
        if (categoryName != null) {
            categoryId = categoryDAO.getCategoryByName(categoryName).getId();
        }

        String authorName;
        try {
            authorName = userDAO.getUser(authorId).getName();
        } catch (UserNotFoundException e) {
            return new ServiceResponse<>(false, "User doesn't exist", null);
        }
        Survey newSurvey = surveyDAO.addSurvey(name, description, categoryId, authorName, Calendar.getInstance().getTime());

        if (newSurvey == null) {
            return new ServiceResponse<>(false, "Couldn't create new survey", null);
        }

        return new ServiceResponse<>(true, "Survey created successfully", newSurvey);
    }

    public ServiceResponse<?> setSurveyCategory(Integer surveyId, String categoryName) throws DatabaseAccessException {
        if (!categoryDAO.exists(categoryName)) {
            categoryDAO.addCategory(categoryName);
        }

        Integer categoryId = categoryDAO.getCategoryByName(categoryName).getId();
        try {
            surveyDAO.updateSurveyCategory(surveyId, categoryId);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey doesn't exist", null);
        }

        return new ServiceResponse<>(true, "Category set successfully", null);
    }

    public ServiceResponse<?> updateSurvey(EditSurveyDTO editSurveyDTO) throws DatabaseAccessException {
        int surveyId = editSurveyDTO.id();
        String surveyName = editSurveyDTO.name();
        String surveyDescription = editSurveyDTO.description();
        String categoryName = editSurveyDTO.category();

        Survey survey;
        try {
            survey = surveyDAO.getSurveyById(surveyId);

            if (!categoryName.isEmpty()) {
                Category category = categoryDAO.getCategoryByName(categoryName);
                survey.setCategoryId(category.getId());
            } else {
                survey.setCategoryId(null);
            }

            survey.setName(surveyName);
            survey.setDescription(surveyDescription);
            survey.setCategoryName(categoryName);

            surveyDAO.updateSurvey(survey);

            List<EditQuestionDTO> editQuestions = editSurveyDTO.questions();

            List<Question> dbQuestions = questionDAO.getQuestions(surveyId);

            List<Integer> questionIdsSpared = new ArrayList<>();

            for (EditQuestionDTO editQuestion : editQuestions) {
                List<Integer> optionIdsSpared = new ArrayList<>();

                int questionId = editQuestion.id();
                String questionText = editQuestion.text();
                QuestionType questionType = QuestionType.valueOf(editQuestion.type());

                List<EditOptionDTO> editOptions = editQuestion.options();

                if (questionId == -1) {
                    List<String> optionsString = editOptions.stream().map(EditOptionDTO::description).toList();
                    questionDAO.addQuestion(surveyId, questionText, questionType, optionsString);
                } else {
                    try {
                        Question question = questionDAO.getQuestion(questionId);

                        question.setText(editQuestion.text());
                        question.setType(questionType);
                        questionDAO.updateQuestion(question);

                        questionIdsSpared.add(question.getId());

                        for (EditOptionDTO editOption : editOptions) {
                            int optionId = editOption.id();
                            if (optionId == -1) {
                                optionDAO.addOption(questionId, editOption.description());
                            } else {
                                try {
                                    Option option = optionDAO.getOption(optionId);

                                    option.setDescription(editOption.description());

                                    optionDAO.updateOption(option);

                                    optionIdsSpared.add(option.getId());
                                } catch (OptionNotFoundException ignored) {
                                    optionDAO.addOption(questionId, editOption.description());
                                }
                            }
                        }

                        for (Option option : question.getOptions()) {
                            if (!optionIdsSpared.contains(option.getId())) {
                                optionDAO.deleteOption(option.getId());
                            }
                        }
                    } catch (QuestionNotFoundException e) {
                        List<String> optionsString = editOptions.stream().map(EditOptionDTO::description).toList();
                        questionDAO.addQuestion(surveyId, questionText, questionType, optionsString);
                    }
                }
            }

            for (Question question : dbQuestions) {
                if (!questionIdsSpared.contains(question.getId())) {
                    questionDAO.deleteQuestion(question.getId());
                }
            }

        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey not found", null);
        }

        return new ServiceResponse<>(true, "Successfully updated survey", null);
    }


}
