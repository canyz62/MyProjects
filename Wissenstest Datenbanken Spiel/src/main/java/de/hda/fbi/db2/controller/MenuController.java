package de.hda.fbi.db2.controller;

import de.hda.fbi.db2.api.Lab02EntityManager;
import de.hda.fbi.db2.api.Lab03Game;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * MenuController Created by l.koehler on 05.08.2019.
 */
public class MenuController {

  private final Controller controller;

  public MenuController(Controller controller) {
    this.controller = controller;
  }

  /**
   * shows the menu.
   */
  public void showMenu() {
    do {
      System.out.println("Choose your Destiny?");
      System.out.println("--------------------------------------");
      System.out.println("1: Re-read csv");
      System.out.println("2: Play test");
      System.out.println("3: Create mass data");
      System.out.println("4: Analyze game data");
      System.out.println("0: Quit");
    } while (readInput());
  }

  private boolean readInput() {
    try {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
      String input = reader.readLine();
      if (input == null) {
        return true;
      }
      switch (input) {
        case "0":
          return false;
        case "1":
          readCsv();
          break;
        case "2":
          playTest();
          break;
        case "3":
          createMassData();
          break;
        case "4":
          analyzeData();
          break;
        default:
          System.out.println("Input Error");
          break;
      }

      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private void analyzeData() {

    Lab02EntityManager lab = controller.getLab02EntityManager();
    EntityManager em = lab.getEntityManager();

    Query query3;
    System.out.println("\n\nWählen Sie eine Analyse aus: \n"
            + "1. Ausgabe aller Spieler (Spielername), die in einem bestimmten Zeitraum gespielt hatten\n"
            + "2. Ausgabe zu einem bestimmten Spieler: Alle Spiele (Id, Datum), sowie die Anzahl der korrekten Antworten pro Spiel mit Angabe der Gesamtanzahl der Fragen pro Spiel \n"
            + "3. Ausgabe aller Spieler mit Anzahl der gespielten Spiele, nach Anzahl absteigend geordnet\n"
            + "4. Ausgabe der am meisten gefragten Kategorie\n"
            + "5. Zurück ins Hauptmenü\n\n"
            + "Eingabe: ");


    Scanner sc = new Scanner(System.in, "utf-8");
    int input = sc.nextInt();

    switch (input) {
      case 1:
        System.out.println("Ausgabe aller Spieler, die zu einem bestimmten Zeitraum gespielt haben");
        query1(em);
        break;
      case 2:
        System.out.println("Ausgabe zu einem bestimmten Spieler");
        query2(em);
        break;
      case 3:
        System.out.println("Ausgabe aller Spieler mit Anzahl der gespielten Spiele");
        query3(em);
        break;
      case 4:
        System.out.println("Ausgabe der am meisten gefragten Kategorie");
        query4(em);
        break;
      case 5:
        return;
      default:
        System.out.println("Keine gültige Eingabe!");
        return;
    }

  }

  /**
   * Query 1
   * @param em
   */
  public void query1(EntityManager em) {

    Date start = null;
    Date end = null;

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,
              StandardCharsets.UTF_8));
      String input;
      do {
        System.out.println("Welcher Zeitraum als Startpunkt? Format: yyyy-mm-dd hh:mm:ss");
        input = reader.readLine();
      } while (input == null || !input.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));

      String start1 = input;

      do {
        System.out.println("Welcher Zeitraum als Endpunkt? Format: yyyy-mm-dd hh:mm:ss");
        input = reader.readLine();
      } while (input == null || !input.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));

      String end1 = input;

      start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start1);
      end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end1);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }


    List<String> players = em.createQuery( "SELECT p.name FROM Player p INNER JOIN Game g "
            + "ON p.name = g.player.name "
            + "WHERE g.start >= :start "
            + "AND g.end <= :end " + "GROUP BY p.name" ).setParameter("start", start).setParameter("end", end).getResultList();

    if(players.size() == 0) {
      System.out.println("In diesem Zeitraum wurden keine Spiele gespielt. ");
    }
    else {
      for (String name: players) {
        System.out.println(name);
      }
      System.out.println("\n");
    }
  }

  /**
   * Query 2
   * @param em
   */
  public  void query2(EntityManager em) {
    System.out.println("Namen eingeben: ");
    String name = null;
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,
              StandardCharsets.UTF_8));
      name = reader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }

    List results = em.createQuery(
                    "SELECT p.name, g.id, g.start, g.end, COUNT(KEY(q)) AS questions, "
                            + "SUM(CASE WHEN VALUE(q) = TRUE THEN 1 ELSE 0 END) AS correctAnswers "
                            + "FROM Game g JOIN g.player p JOIN g.askedQuestion q "
                            + "WHERE p.name = :name "
                            + "GROUP BY p.name, g.id")
            .setParameter("name", name)
            .getResultList();

    for (Iterator i = results.iterator(); i.hasNext();) {
      Object[] element = (Object[]) i.next();
      System.out.println(
              "Player name               : " + element[0]
                      + "\nGame-ID                   : " + element[1]
                      + "\nStart-time                : " + element[2]
                      + "\nEnd-time                  : " + element[3]
                      + "\nAnzahl Fragen             : " + element[4]
                      + "\nAnzahl richtige Antworten : " + element[5]);
      System.out.println();
    }
    em.close();
  }

  /**
   * Query 3
   * @param em
   */
  public void query3(EntityManager em) {
    String query3 = (" select p.name, count(g) as playedGames from Player p, Game g where g.player = p group by p.name order by playedGames desc");
    List pl = em.createQuery(query3).getResultList();

    for (Iterator i = pl.iterator(); i.hasNext();) {
      Object[] element = (Object[]) i.next();
      System.out.println(element[0] + " - " + element[1]);
    }
  }

  /**
   * Query 4
   * @param em
   */
  public void query4(EntityManager em) {
    List results = em.createQuery(
                    "SELECT c.name, COUNT(q.quest) AS times_chosen "
                            + "FROM Game g JOIN g.askedQuestion qog JOIN KEY(qog) q JOIN q.ctgry c "
                            + "GROUP BY c.name "
                            + "ORDER BY times_chosen DESC")
            .getResultList();

    for (Iterator i = results.iterator(); i.hasNext();) {
      Object[] element = (Object[]) i.next();
      System.out.println(
              "Caterory name : " + element[0] + "\n"
                      + "times chosen : " + element[1] + "\n");
    }
    em.close();
  }

  private void createMassData() {
    controller.createMassData();
  }

  private void playTest() {
    Lab03Game gameController = controller.getLab03Game();
    Object player = gameController.interactiveGetOrCreatePlayer();
    List<?> questions = gameController.interactiveGetQuestions();
    Object game = gameController.createGame(player, questions);
    gameController.interactivePlayGame(game);
    gameController.persistGame(game);
  }

  private void readCsv() {
    controller.readCsv();
  }
}
