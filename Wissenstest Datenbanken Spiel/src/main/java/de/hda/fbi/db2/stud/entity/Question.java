package de.hda.fbi.db2.stud.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "Question", schema = "db2p")
public class Question {

    @Id
    private int id;

    private String quest;

    @ElementCollection
    @CollectionTable(name = "Answer", schema = "db2p")
    private List<Answer> fourAnswers;

    @ManyToOne
    private Category ctgry;

    public Question() {
        id = -1;
        fourAnswers = null;
        ctgry = null;
        quest = "";
    }

    public Question(int ID, String q, Answer[] ans, Category c) {
        this.id = ID;
        this.quest = q;
        this.fourAnswers = Arrays.asList(ans.clone());
        this.ctgry = c;
    }

    public List<Answer> getFourAnswers() {
        return fourAnswers;
    }
    public String getQuest() {
        return quest;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    public Category getCategory() {
        return ctgry;
    }

    public void setCategory(Category ctgry) {
        this.ctgry = ctgry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void printC() {
        System.out.println("ID : " + id);
        System.out.println("Kategorie : " + ctgry.getName());
        System.out.println("Frage : " + quest + "  ");
        System.out.println("Antwortmoeglichkeiten : ");
        for (int i = 0; i < 4; i++) {
            System.out.print("Antwort " + (i+1) + " -> ");
            fourAnswers.get(i).printA();
            System.out.println("   ");
        }
        System.out.println(" ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id == question.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
