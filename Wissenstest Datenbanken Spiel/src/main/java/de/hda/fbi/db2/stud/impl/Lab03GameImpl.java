package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab03Game;
import de.hda.fbi.db2.stud.entity.Answer;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import javax.persistence.*;

public class Lab03GameImpl extends Lab03Game {


    //private EntityManagerFactory emf;
    private EntityManager em;

    private static final Random RANDOM = new Random();

    @Override
    public void init() {

        em = lab02EntityManager.getEntityManager();
    }

    @Override
    public Object getOrCreatePlayer(String playerName) {

        //String sql = "SELECT p.name FROM Player p WHERE p.name= :name";
        String sql = "SELECT p.name FROM db2p.player p WHERE p.name='"+playerName+"'";

        Object p = null;

        try {

            //p = em.createNativeQuery(sql, Player.class).setParameter("name", playerName).getSingleResult();
            p = em.createNativeQuery(sql, Player.class).getSingleResult();

        }
        catch(NoResultException nre)
        {
            //Bleibt leer, weil es ignoriert werden soll
        }

        if (p == null)
        {
            p = new Player(playerName);
        }

        return p;
    }

    @Override
    public Object interactiveGetOrCreatePlayer() {

        System.out.println("Please enter username: ");
        Scanner S = new Scanner(System.in, "UTF-8");
        String getPlayerName = S.next();

        return getOrCreatePlayer(getPlayerName);
    }

    @Override
    public List<?> getQuestions(List<?> categories, int amountOfQuestionsForCategory) {

        List questionList = new ArrayList();

        for (Object category : categories) {
            Category c = (Category) category;

            List questionsPerCategory = new ArrayList();
            try {
                questionsPerCategory = c.getQuestionList();
            }
            catch(NoResultException nre)
            {
                //System.out.println("No questions found for category: " + c.getName());
            }

            if (questionsPerCategory.size() < amountOfQuestionsForCategory) {
                questionList.addAll(questionsPerCategory);
            }
            else {
                for (int j = 0; j < amountOfQuestionsForCategory; j++) {

                    int index = (int) (Math.random() * questionsPerCategory.size());
                    Question q = (Question) questionsPerCategory.get(index);
                    while (questionList.contains(q)) {
                        index = (int) (Math.random() * questionsPerCategory.size());
                        q = (Question) questionsPerCategory.get(index);
                    }
                    questionList.add(q);
                }
            }
        }
        Collections.shuffle(questionList);
        return questionList;
    }

    @Override
    public List<?> interactiveGetQuestions() {

        List<Category> chosenCategories = new ArrayList<>();
        List<Category> allCategories = /*(List<Category>) lab01Data.getCategories();*/ new ArrayList<>();
        int amountOfQuestionsPerCategory;
        int categorysize = 0;

        String categorySQL = "SELECT c.categoryid, c.name FROM db2p.category c ORDER BY c.categoryid";
        try {
            allCategories = em.createNativeQuery(categorySQL, Category.class).getResultList();
            categorysize = allCategories.size();
        }
        catch(NoResultException nre)
        {
            System.out.println("No Categories found");
        }

        int i = 1;
        for (Category c : allCategories) {
            System.out.println(i + " : " + c.getName());
            i++;
        }

        System.out.println("Bitte geben Sie die Zahl von mindestens 2 Kategorien ein (0 zum Beenden der Eingabe): ");

        Scanner sc = new Scanner(System.in,"UTF-8");
        String inputCategoryName;

        do {

            inputCategoryName = sc.next();
            int testnum = Integer.parseInt(inputCategoryName);


            if(testnum > categorysize || testnum < 0){
                System.out.println("Geben Sie bitte eine Zahl zwischen 1 und 51 ein.");
            }
            else if(inputCategoryName.isEmpty()) {
                System.out.println("Nothing was entered. Please try again.");
            }else {

                int in_num = Integer.parseInt(inputCategoryName);
                int j = 1;

                for (Category c : allCategories) {
                    if (in_num == j) {
                        chosenCategories.add(c);
                        break;
                    } else j++;
                }
            }
        } while (chosenCategories.size() < 2 || !inputCategoryName.equals("0"));

        System.out.println("Bitte geben Sie die maximale Anzahl an Fragen an, die Sie zu jeder Kategorie gestellt bekommen möchten : ");

        sc = new Scanner(System.in, "UTF-8");
        String inputQuestionAnzahl = sc.nextLine();

        try {
            amountOfQuestionsPerCategory = Integer.parseInt(inputQuestionAnzahl);
        }
        catch(NumberFormatException e)
        {
            System.out.println(e);
            amountOfQuestionsPerCategory = 0;
        }
        return getQuestions(chosenCategories, amountOfQuestionsPerCategory);

    }

    @Override
    public Object createGame(Object player, List<?> questions) {

        Game g = new Game();
        Map<Question, Boolean> example = new HashMap<>();

        g.setPlayer((Player) player);

        for (Object q : questions) {
            Question quest = (Question) q;
            example.put(quest, false);
        }

        g.setAskedQuestion(example);

        // define selected categories
        /*for(Map.Entry<Question, Boolean> entry : g.getAskedQuestion().entrySet()) {

            Question currentQuestion = entry.getKey();

            Category cat = currentQuestion.getCategory();

            if(!(g.getSelectedCats().contains(cat))) {
                g.addSelectedCat(cat);
            }
        }*/

        return g;
    }

    @Override
    public void playGame(Object game) {
        Game g = (Game) game;
        Date startTime = new Date();
        g.setStart(startTime);

        for(Map.Entry<Question, Boolean> entry : g.getAskedQuestion().entrySet()) {

            Question currentQuestion = entry.getKey();

//      System.out.println("Kategorie : " + currentQuestion.getCategory().getName());
//      System.out.println("Frage : " + currentQuestion.getQuest() + "  ");
//      System.out.println("Antwortmoeglichkeiten : ");

//            int i = 1;
//            for(Answer a : currentQuestion.getFourAnswers()) {
//        System.out.println(i + " " + a.getAns());
//                i++;
//            }

            int upperbound = 4;

            int antwort= RANDOM.nextInt(upperbound) + 1;

            if(currentQuestion.getFourAnswers().get(antwort-1).isCorrect()) {
                g.getAskedQuestion().replace(currentQuestion, false, true);
            }
        }
        Date endTime = new Date();
        g.setEnd(endTime);

        //random Date in between 2 weeks
        g.randomDate();
    }

    @Override
    public void interactivePlayGame(Object game) {
        Game g = (Game) game;
        Date startDate = new Date();
        g.setStart(startDate);
        int score = 0;

        for(Map.Entry<Question, Boolean> entry : g.getAskedQuestion().entrySet()) {

            Question currentQuestion = entry.getKey();

            System.out.println("Kategorie : " + currentQuestion.getCategory().getName());
            System.out.println("Frage : " + currentQuestion.getQuest() + "  ");
            System.out.println("Antwortmoeglichkeiten : ");
            int i = 1;

            for(Answer a : currentQuestion.getFourAnswers())
            {
                System.out.println(i + " " + a.getAns());
                i++;
            }

            System.out.println(" Ihre Antwort : ");

            Scanner sc = new Scanner(System.in, "UTF-8");
            String Inputantwort = sc.nextLine();

            while (Inputantwort.equals(""))
            {
                Inputantwort = sc.nextLine();
            }

            int antwort = Integer.parseInt(Inputantwort);


            if (antwort < 1 || antwort > 4){
                System.out.println("Bitte geben sie eine Zahl zwischen 1 und 4 ein.");
            }

            while (antwort < 1 || antwort > 4)
            {
                Inputantwort = sc.nextLine();
                antwort = Integer.parseInt(Inputantwort);
                if (antwort < 1 || antwort > 4){
                    System.out.println("Bitte geben sie eine Zahl zwischen 1 und 4 ein.");
                }
            }

            if(currentQuestion.getFourAnswers().get(antwort-1).isCorrect())
            {
                System.out.println("Das war die richtige Antwort!!");
                g.getAskedQuestion().replace(currentQuestion, false, true);
                score++;
            }
            else
            {
                System.out.println("Das war die falsche Antwort!!");
            }
        }
        Date endDate = new Date();
        g.setEnd(endDate);
        long difference_In_Time = endDate.getTime() - startDate.getTime();
        long difference_In_Seconds = (difference_In_Time / 1000) % 60;

        System.out.println("Der Spieler " + g.getPlayer().getName() + " hat " + score + " Fragen richtig beantwortet. Das Spiel dauerte " + difference_In_Seconds + " Sekunden.");
        Player p = g.getPlayer();
        p.addGame(g);
        g.setPlayer(p);
    }

    @Override
    public void persistGame(Object game) {

        em.getTransaction().begin();

        Game g = (Game) game;

        em.persist(g);

        em.getTransaction().commit();

    }
}
