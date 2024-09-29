package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.QuestionRepository;

public class QuestionRepositoryMock implements QuestionRepository {

    private final DBTableImitation<Question, Integer> questions = new DBTableImitation<>(1000,
            params -> (new Question(0, (Integer) params[0], (String) params[1], (QuestionType) params[2])),
            Question::getId);

    @Override
    public Question getQuestionById(int id) {
        return questions.get(id);
    }

    @Override
    public void addQuestion(int surveyId, String text, QuestionType type) {
        questions.add(surveyId, text, type);
    }

    @Override
    public void updateQuestion(Question q) {
        questions.get(q.getId()).setSurveyId(q.getSurveyId());
        questions.get(q.getId()).setText(q.getText());
        questions.get(q.getId()).setType(q.getType());
    }

    @Override
    public void deleteQuestion(int id) {
        questions.remove(id);
    }

    @Override
    public boolean exists(int id) {
        return questions.containsKey(id);
    }

}
