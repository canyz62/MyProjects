# Einführung in Git und GitLab

## Ziel
[Git](https://git-scm.com/) ist eine populäre Software zur (verteilten) Versionsverwaltung von Dateien. Sie wurde initial von [Linux Torvalds](https://en.wikipedia.org/wiki/Linus_Torvalds) für den [Linux Kernel](https://en.wikipedia.org/wiki/Linux_kernel) entwickelt und gilt heute als Standard in der Software Entwicklung. 

[GitLab](https://about.gitlab.com/) ist, ähnlich wie [GitHub](https://github.com/), eine Weboberfläche zum Verwalten von Git-Repositories. Das folgende Dokument führt in die für dieses Praktikum benötigte Infrastruktur und Software ein.

## Clonen eines Projekts von Gitlab
Um die Arbeit zu erleichtern, finden Sie in diesem Repository einen vorbereiteten Projektrahmen. Als Erstes müssen Sie sich eine lokale Kopie des Projektes erzeugen – dieser Schritt heißt bei Git clonen. Sie können das Clonen mit den installierten Git-Tools (vgl. „Benötigte Software für die Arbeit mit Git“) über das Dateisystem (`git clone`) oder über **IntelliJ** ausführen:

- VCS &rarr; Git &rarr; Clone
- Im Feld URL die URL Ihres Repositorys eintragen.
- Unter Directory ein geeignetes Verzeichnis auf Ihrem Rechner auswählen.
- Alle weiteren Angaben im IntelliJ-Dialog können Sie unverändert übernehmen.

Das vorbereitete Projekt kann in IntelliJ direkt und ohne weitere Anpassungen verwendet werden. Das eingesetzte Build-Automatisierungssystem heißt Gradle und muss von Ihnen in der Regel nicht angepasst werden, da bereits alles lauffähig konfiguriert ist und stellt eine Menge vorbereiteter Tasks, wie check, build, run, etc., zur Verfügung.
Der Projektrahmen ist in folgende Bereiche aufgeteilt:
- In src &rarr; main &rarr; java werden die Java-Sources abgespeichert. Das Package `de.hda.fbi.db2` ist bereits eingerichtet.
- In src &rarr; main &rarr; resources werden die zum Projekt gehörenden Ressource-Dateien abgespeichert.

**Hinweis**: Alle von Ihnen entwickelten Java-Klassen und -Packages müssen als Unter-Packages von `de.hda.fbi.db2.stud` abgelegt werden, damit der automatisierte Build-Prozess (vgl. „Weitere Hinweise zur Arbeit mit GitLab“) funktioniert!

**Beispiel**:	`de.hda.fbi.db2.stud.entity`

### Wichtige Git-Kommandos
**Add** *(Selektieren Ihrer Änderungen zum Commit)*:    `git add`<br/>
Add registriert Dateien im Git für den nächsten Commit. Dabei ist zu beachten, dass bei Anwendungen nur der Quellcode, nicht aber die fertigen Libraries/Programme hinzugefügt werden dürfen!

*Bitte adden und committen Sie Ihre Projekt-Dateien ausschließlich über IntelliJ!* Die Funktion Add ist nur für zusätzliche Dateien außerhalb des Projektordners gedacht.


**Commit** *(Hinzufügen Ihrer Änderungen zum lokalen Repository)*:    `git commit`<br/>
Dieses Kommando fügt Ihre Änderungen zum lokalen Repository hinzu. Sie müssen hierbei eine Commit-Message angeben, welche die Änderungen beschreibt. Eine Übertragung zum Server findet, im Gegensatz zu SVN, beim Commit nicht statt!


**Push** *(Übertragen Ihrer Änderungen auf den Server)*:  `git push`<br/>
Überträgt Ihre Änderungen zum Git-Server. Ggf. muss vorher ein Pull gemacht werden.


**Pull** *(Abrufen von Änderungen vom Server)*:   `git pull`<br/>
Lädt Änderungen vom Server herunter. Ggf. müssen diese Änderungen mit den lokalen Änderungen zusammengefügt (merge) werden.

Für Studierende, die über keine Erfahrung mit Git verfügen, empfehlen wir, nicht gleichzeitig an mehreren Rechnern zu arbeiten, und jeweils unmittelbar *vor* der Arbeit ein Pull und unmittelbar *nach* der Arbeit ein Push zu machen (Commit vor dem Push nicht vergessen!). 

Des Weiteren empfehlen wir unerfahrenen Studierenden folgende Links:
- [Buch: *Git Pro*](https://git-scm.com/book/en/v2) (verfügbar in verschiedenen Sprachen)Gutes Buch zu Git, mit vielen (sehr vielen) Beispielen. Das Buch steht unter Creative Commons Attribution Non Commercial Share Alike 3.0 license. Es ist in verschiedenen Sprachen und Formaten verfügbar.
- [Comparing Workflows](https://www.atlassian.com/git/tutorials/comparing-workflows)  eine gute Einführung in die Git-Workflows.
- [learngitbranching.js.org](https://learngitbranching.js.org/) Tutorial zum Thema Git Branching
 
