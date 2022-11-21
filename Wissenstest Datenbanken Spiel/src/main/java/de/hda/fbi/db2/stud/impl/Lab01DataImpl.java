package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab01Data;
import de.hda.fbi.db2.stud.entity.Answer;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class Lab01DataImpl extends Lab01Data {

    private final List<Question> questions;
    private final List<Category> categories;

    public Lab01DataImpl() {
        questions = new ArrayList<>();
        categories = new ArrayList<>();
    }

    @Override
    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public void loadCsvFile(List<String[]> additionalCsvLines) {

        // all different categories
        Vector<String> seenCategories= new Vector();
        Category cat = null;
        //int catID = 1;

        for (int i = 1; i < additionalCsvLines.size(); i++) {

            // rows
            String[] Line = additionalCsvLines.get(i);

            // columns
            int questionID = Integer.parseInt(Line[0]);
            String questionText = Line[1];
            String answer1 = Line[2];
            String answer2 = Line[3];
            String answer3 = Line[4];
            String answer4 = Line[5];
            int solution = Integer.parseInt(Line[6]);
            String categoryName = Line[7];

            Answer[] answers = new Answer[4];

            if(!(seenCategories.contains(categoryName))) {
                cat = new Category(categoryName);
                seenCategories.add(categoryName);
                //cat.setCategoryID(catID);
                //catID++;
                categories.add(cat);
            }
            else {
                for(int j=0;j<categories.size();j++) {
                    if(Objects.equals(categoryName, categories.get(j).getName())) {
                        cat = categories.get(j);
                    }
                }
            }

            // insert all four answers in answer-Array
            Answer a = new Answer(answer1);
            answers[0] = a;

            Answer b = new Answer(answer2);
            answers[1] = b;

            Answer c = new Answer(answer3);
            answers[2] = c;

            Answer d = new Answer(answer4);
            answers[3] = d;

            // choose the right answer
            answers[solution - 1].setCorrect(true);


            Question q = new Question(questionID, questionText, answers, cat);
            questions.add(q);

            // question gets their answers
            // q.setFourAnswers(answers);

            cat.addQuestionToList(q);

            // print all objects
            for (Question question : questions) {
                question.printC();
            }

            System.out.println("Anzahl der Fragen: " + questions.size());
            System.out.println("Anzahl der Kategorien: " + categories.size());
        }
    }
}
