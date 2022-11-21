package de.hda.fbi.db2.stud.entity;

import javax.persistence.Embeddable;
import java.util.Objects;
import javax.persistence.Table;

@Embeddable
@Table(name = "Answer", schema = "db2p")
public class Answer {

    private boolean isCorrect = false;

    private String ans;

    public Answer() {
        ans = null;
    }

    public Answer(String a) {
        this.ans = a;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getAns() {
        return ans;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }


    public void printA() {
        System.out.print(ans + ", ");
        System.out.print(isCorrect);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer) o;
        return isCorrect == answer.isCorrect && Objects.equals(ans, answer.ans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isCorrect, ans);
    }
}
