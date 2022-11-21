package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab04MassData;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Lab04MassDataImpl extends Lab04MassData {
    static final int numberPlayers = 10000;
    static final int numberGames = 100;
    static final int maxCategories = 12;
    static final int maxQuestions = 15;
    static final int commitThreshold = 1000;

    @Override
    public void createMassData() {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("docker-local-postgresPU");
        EntityManager em2 = emf.createEntityManager();

        final long createStart = System.currentTimeMillis();

        List<Category> categories = getCategoriesFromDB();
        Random rng = new Random();
        List<Object> players = new ArrayList<>();
        List<Game> playedGames = new ArrayList<>();

        System.out.println("Creating Players...");

        for (int i = 0; i < numberPlayers; i++) {
            players.add(new Player("player" + i, new ArrayList<>()));
        }


        System.out.println("Persisting games...");
        EntityTransaction tx = null;
        try {
            tx = em2.getTransaction();
            tx.begin();
            System.out.println("Configuring and creating multiple games...");
            for (int i = 0; i < numberPlayers; i++) {
                for (int j = 0; j < numberGames; j++) {
                    List<Category> chosenCategories = new ArrayList<>();
                    Collections.shuffle(categories);
                    int random = rng.nextInt(maxCategories);
                    if (random < 2) {
                        random = 2;
                    }

                    for (int k = 0; k < random; k++) {
                        chosenCategories.add(categories.get(k));
                    }

                    random = rng.nextInt(maxQuestions) + 1;

                    List<?> questions = lab03Game.getQuestions(chosenCategories, random);
                    Game game = (Game) lab03Game.createGame(players.get(i), questions);
                    lab03Game.playGame(game);
                    playedGames.add(game);
                }
                if(playedGames.size() == 10000) {
                    for (int k = 0; k < playedGames.size(); k++) {

                        em2.persist(playedGames.get(k));

                        //System.out.println(k);
                        if ((k + 1) % commitThreshold == 0) {
                            em2.flush();
                            em2.clear();
                        }
                    }
                    playedGames.clear();
                    //System.out.println(i);
                }
            }
            tx.commit();

            Query query = em2.createQuery("SELECT count(g) FROM Game g");
            long count = (long) query.getSingleResult();
            System.out.println("Anzahl der Games: " + count);

            Query query2 = em2.createQuery("SELECT count(p) FROM Player p");
            long count2 = (long) query2.getSingleResult();
            System.out.println("Anzahl der Players: " + count2);

            Query query3 = em2.createQuery("SELECT count(KEY(k))/count(g) FROM Game g ,g.askedQuestion k");
            long avg = (long) query3.getSingleResult();
            System.out.println("Durchschnittliche Anzahl der Fragen pro Spiel: " + avg);

        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                em2.getTransaction().rollback();
            }
            throw e;
        } finally {
            em2.close();
        }

        //Persist
        /*System.out.println("Persisting games...");
        EntityTransaction tx = null;
        try {
            tx = em2.getTransaction();
            tx.begin();*/

            /*for (int i = 0; i < playedGames.size(); i++) {

                em2.persist(playedGames.get(i));

                System.out.println(i);
                if ((i + 1) % commitThreshold == 0) {
                    em2.flush();
                    em2.clear();
                }
            }
            tx.commit();*/

        /*} catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                em2.getTransaction().rollback();
            }
            throw e;
        } finally {
            em2.close();
        }*/

        long createEnd = System.currentTimeMillis();
        System.out.println("Finished creation of mass-data.");
        System.out.println("Time spent: " + (((double)(createEnd - createStart)) / 1000 / 60) + " min");

    }

    @Nullable
    private List<Category> getCategoriesFromDB() {
        EntityManager em = lab02EntityManager.getEntityManager();
        String categorySQL = "SELECT c.categoryid, c.name FROM db2p.category c";
        List<Category> allCategories = em.createNativeQuery(categorySQL, Category.class).getResultList();

        if (allCategories.isEmpty() || allCategories.size() < 1) {
            System.out.println("Not enough categories in DB. Please add more!");
            return null;
        } else {
            return allCategories;
        }
    }
}
