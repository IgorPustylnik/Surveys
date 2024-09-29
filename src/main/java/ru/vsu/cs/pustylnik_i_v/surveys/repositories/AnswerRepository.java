package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Answer;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

public interface AnswerRepository {
    public Answer getAnswerById(int id);

    public void addAnswer(Answer a) throws SessionNotFoundException, OptionNotFoundException;

    public void updateAnswer(Answer a);

    public void deleteAnswer(int id);

    public boolean exists(int id);
}
