<?php declare(strict_types=1);
// UTF-8 marker äöüÄÖÜß€
/**
 * Class PageTemplate for the exercises of the EWA lecture
 * Demonstrates use of PHP including class and OO.
 * Implements Zend coding standards.
 * Generate documentation with Doxygen or phpdoc
 *
 * PHP Version 7.4
 *
 * @file     PageTemplate.php
 * @package  Page Templates
 * @author   Bernhard Kreling, <bernhard.kreling@h-da.de>
 * @author   Ralf Hahn, <ralf.hahn@h-da.de>
 * @version  3.1
 */

// to do: change name 'PageTemplate' throughout this file
require_once './Page.php';

/**
 * This is a template for top level classes, which represent
 * a complete web page and which are called directly by the user.
 * Usually there will only be a single instance of such a class.
 * The name of the template is supposed
 * to be replaced by the name of the specific HTML page e.g. baker.
 * The order of methods might correspond to the order of thinking
 * during implementation.
 * @author   Bernhard Kreling, <bernhard.kreling@h-da.de>
 * @author   Ralf Hahn, <ralf.hahn@h-da.de>
 */
class baecker extends Page
{
    // to do: declare reference variables for members 
    // representing substructures/blocks

    /**
     * Instantiates members (to be defined above).
     * Calls the constructor of the parent i.e. page class.
     * So, the database connection is established.
     * @throws Exception
     */
    protected function __construct()
    {
        parent::__construct();
        // to do: instantiate members representing substructures/blocks
    }

    /**
     * Cleans up whatever is needed.
     * Calls the destructor of the parent i.e. page class.
     * So, the database connection is closed.
     */
    public function __destruct()
    {
        parent::__destruct();
    }

    /**
     * Fetch all data that is necessary for later output.
     * Data is returned in an array e.g. as associative array.
     * @return array An array containing the requested data.
     * This may be a normal array, an empty array or an associative array.
     */
    protected function getViewData(): array
    {
        $baecker = array();
        $sql = 'SELECT ordered_article.ordered_article_id, ordered_article.ordering_id, article.name, ordered_article.status FROM ordered_article, article WHERE article.article_id=ordered_article.article_id AND ordered_article.status < 3 ORDER BY ordered_article.ordering_id ASC';
        $recordset = $this->_database->query($sql);
        if (!$recordset) throw new Exception ("Fehler in Abfrage:" . $this->database->error);
        while ($record = $recordset->fetch_assoc()) {
            $baecker[] = $record;
        }
        $recordset->free();
        return $baecker;
    }

    /**
     * First the required data is fetched and then the HTML is
     * assembled for output. i.e. the header is generated, the content
     * of the page ("view") is inserted and -if available- the content of
     * all views contained is generated.
     * Finally, the footer is added.
     * @return void
     */
    protected function generateView(): void
    {
        $data = $this->getViewData(); // daten vom array der datenbank aus getviewdata
        $this->generatePageHeader('Bäcker', "", true); //to do: set optional parameters
        $allOrderingID = [];
        foreach ($data as $i) {
            if (!in_array(htmlspecialchars($i["ordering_id"]), $allOrderingID)) {
                $allOrderingID[] = htmlspecialchars($i["ordering_id"]);
            }
        }

        echo <<<EOT

<section class="Bäcker">
    <h1>Bäcker</h1>
EOT;

        if (sizeof($data) == 0) {
            echo "<p>Es gibt in diesem Moment keine Bestellungen</p>";
            return;
        }


        foreach ($allOrderingID as $k) {
            echo <<<EOT
        <fieldset>
          <legend accesskey="1">Bestellung $k</legend>
EOT;
            foreach ($data as $i) {

                $orderingArticleID = htmlspecialchars($i["ordered_article_id"]);
                if (htmlspecialchars($i["ordering_id"]) == $k) {

                    echo <<<EOT
                        <form action="baecker.php" id="baeckerBestellungForm$orderingArticleID" method="post" accept-charset="UTF-8">
EOT;



                    $status = intval(htmlspecialchars($i['status']));

                    $isChecked = $status == 0 ? 'checked' : null;
                    echo <<<EOT
                <label> <input type="radio" $isChecked name="bestellungStatus" value="0" onclick="document.forms['baeckerBestellungForm$orderingArticleID'].submit();" /> Bestellt </label>
EOT;
                    $isChecked = $status == 1 ? 'checked' : null;
                    echo <<<EOT
                 <label> <input type="radio" $isChecked name="bestellungStatus" value="1" onclick="document.forms['baeckerBestellungForm$orderingArticleID'].submit();" /> im Ofen </label>
EOT;
                    $isChecked = $status == 2 ? 'checked' : null;
                    echo <<<EOT
                 <label> <input type="radio" $isChecked name="bestellungStatus" value="2" onclick="document.forms['baeckerBestellungForm$orderingArticleID'].submit();" /> Fertig </label>
EOT;


                    echo <<<EOT
             <input type="hidden" name="ordered_article_id" value="$orderingArticleID"/>

EOT;


                    $name = htmlspecialchars($i["name"]);
                    echo <<<EOT
                    <p>$name</p> 
                  <!--   <input type="submit" value="Ändern"> -->
             </form>
            <!-- name bei allen Radio gleich um zu gruppieren, sonst mehrfachauswahl möglich // Wert von name wird an server übertragen, hier wird aber value wert üvertragen-->

EOT;

                }
            }
            echo <<<EOT
 </fieldset>


EOT;

        }
        echo <<<EOT
  </section>
EOT;
        $this->generatePageFooter();
    }


    protected function processReceivedData(): void
    {
        parent::processReceivedData();


        if (count($_POST) > 0) {
            if (isset($_POST['ordered_article_id']) && is_numeric($_POST['ordered_article_id'])) {
                $ordering_id = $_POST['ordered_article_id'];
            } else {
                return;
            }

            if (isset($_POST['bestellungStatus']) && is_numeric($_POST['bestellungStatus'])) {
                $status = $_POST['bestellungStatus'];
            } else {
                return;
            }

            $query = <<<SQL
        UPDATE ordered_article
        SET status = ?
        WHERE ordered_article_id = ?
        SQL;
            $stmt = $this->_database->prepare($query); //prepared statement
            $stmt->bind_param('ii', $status, $ordering_id);
            $stmt->execute();
            header("HTTP/1.1 303 See Other");
            header('Location: baecker.php');
            die();
        }
    }

    /**
     * This main-function has the only purpose to create an instance
     * of the class and to get all the things going.
     * I.e. the operations of the class are called to produce
     * the output of the HTML-file.
     * The name "main" is no keyword for php. It is just used to
     * indicate that function as the central starting point.
     * To make it simpler this is a static function. That is you can simply
     * call it without first creating an instance of the class.
     * @return void
     */
    public static function main(): void
    {
        try {
            $page = new baecker();
            $page->processReceivedData();
            $page->generateView();
        } catch (Exception $e) {
            //header("Content-type: text/plain; charset=UTF-8");
            header("Content-type: text/html; charset=UTF-8");
            echo $e->getMessage();
        }
    }
}

// This call is starting the creation of the page. 
// That is input is processed and output is created.
baecker::main();

// Zend standard does not like closing php-tag!
// PHP doesn't require the closing tag (it is assumed when the file ends). 
// Not specifying the closing ? >  helps to prevent accidents 
// like additional whitespace which will cause session 
// initialization to fail ("headers already sent"). 
//? >

//Praktikum2 Termin: PRG-Pattern - Formular mit POST an sich selbst schicken und dann in processReceivedData mit GET weiterleiten?
//data-Attribute für Daten, die man in Javascript brauchen sollten da sein. -> Alles was lokal beim Client berechnet wird, wie Warenkorb etc.
//beim Redirect kann es eine Endlosschleife geben PRG-Patten. ERst überprüfen mit count(POST) ob Parameter reingekommen sind, dann diese weiterleiten/verarbeiten und dann die aufrufen
//data tag kann man für Preis der Pizza nutzen. Später kann man dies mit Javascirpt manipulieren
