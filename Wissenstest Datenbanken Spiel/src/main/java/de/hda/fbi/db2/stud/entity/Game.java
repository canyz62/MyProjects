package de.hda.fbi.db2.stud.entity;

import javax.persistence.*;
import java.util.*;

        import java.util.ArrayList;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.Objects;
        import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.CascadeType;
        import javax.persistence.Column;
        import javax.persistence.ElementCollection;
        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.GenerationType;
        import javax.persistence.Id;
        import javax.persistence.ManyToOne;
        import javax.persistence.Table;
        import javax.persistence.Temporal;
        import javax.persistence.TemporalType;

@Entity
@Table(name = "Game", schema = "db2p")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "db2p")
    int id;

    @Column(name = "startTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Column(name = "endTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;

    @ManyToOne (cascade = {CascadeType.PERSIST})
    Player player;

    @ElementCollection
    @CollectionTable(name = "AskedQuestion", schema = "db2p")
    private Map<Question, Boolean> askedQuestion = new HashMap<>();

    public Game() {
        player = null;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setAskedQuestion(Map<Question, Boolean> askedQuestion) {
        this.askedQuestion.putAll(askedQuestion);
    }

    public Map<Question, Boolean> getAskedQuestion() {
        return askedQuestion;
    }

    public void setStart(Date start) {
        this.start = new Date(start.getTime());
    }

    public void setEnd(Date end) {
        this.end = new Date(end.getTime());
    }

    public void randomDate() {
        long diffInMillies = end.getTime() - start.getTime();

        final int twoWeeks = 1209600000;
        final int randDays = ThreadLocalRandom.current().nextInt(0, twoWeeks);

        this.start = new Date(System.currentTimeMillis() + randDays);

        long convertStart = start.getTime();

        this.end = new Date(convertStart + diffInMillies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return id == game.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
