package de.hda.fbi.db2.stud.entity;

import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.persistence.*;


@Entity
@Table(name = "Category", schema = "db2p")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "db2p")
    private int categoryID;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "ctgry", cascade = {CascadeType.PERSIST})
    private List<Question> questionList;

    public Category(String catName) {

        name = catName;
        questionList = new Vector<>();
    }

    public Category() {
        name = "";
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setCategoryName(String catName) {
        name = catName;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void addQuestionToList(Question question) {
        questionList.add(question);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return categoryID == category.categoryID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryID);
    }
}
