package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab02EntityManager;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Question;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Lab02Impl extends Lab02EntityManager {

    private static EntityManagerFactory emf;
    @Override
    public void init() {
        // Die EntityManagerFactory erstellen
        emf = Persistence.createEntityManagerFactory("fbi-postgresPU");
        //emf = Persistence.createEntityManagerFactory("docker-local-postgresPU");
        // Neuen EntityManager erstellen

    }

    @Override
    public void destroy() {
        emf.close();
    }

    @Override
    public void persistData() {
        EntityManager em;

        em = getEntityManager();

        em.getTransaction().begin();

        List<Category> category = (List<Category>) lab01Data.getCategories();

        for(Category c : category){
            for(Question q : c.getQuestionList()){
                em.persist(q); // verwaltete Entity
            }
            em.persist(c); // verwaltete Entity
        }

        em.getTransaction().commit();
        em.close();

    }

    @Override
    public EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }
}
