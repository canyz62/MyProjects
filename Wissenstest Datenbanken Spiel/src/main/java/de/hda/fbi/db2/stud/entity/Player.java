package de.hda.fbi.db2.stud.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Player", schema = "db2p")
public class Player {

    @Id
    String name;

    @OneToMany(mappedBy = "player")
    List<Game> games = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public Player() {}

    public Player(String name, List<Game> game) {
        this.name = name;
        this.games = game;
    }

    public String getName() {
        return name;
    }

    public void addGame(Game g) {
        games.add(g);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

