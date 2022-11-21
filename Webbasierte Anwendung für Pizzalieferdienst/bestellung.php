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
class bestellung extends Page
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
        $pizza = array();
        $lastOrderingIdAndOrderedArticleID = array();

        $sql = 'SELECT article.article_id, article.name, article.picture, article.price  FROM article';
        $recordset = $this->_database->query($sql);
        if (!$recordset) throw new Exception ("Fehler in Abfrage:" . $this->database->error);
        while ($record = $recordset->fetch_assoc()) {
            $pizza[] = $record;
        }
        $recordset->free();

        $sql = 'SELECT MAX(ordering_id) as max_ordering_id, MAX(ordered_article_id) as max_ordered_article_id FROM ordered_article;';  //holt letzte BestellID und letzte BestellArtikelID
        $recordset = $this->_database->query($sql);
        if (!$recordset) throw new Exception ("Fehler in Abfrage:" . $this->database->error);
        while ($record = $recordset->fetch_assoc()) {
            $lastOrderingIdAndOrderedArticleID[] = $record;
        }
        $recordset->free();
        $pizza[] = $lastOrderingIdAndOrderedArticleID; // fügt dem Pizza Array das lastOrderingIdAndOrderedArticleID Array hinzu
        // $mergeArray = $lastOrderingIdAndOrderedArticleID + $pizza;
        return $pizza;
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
        $data = $this->getViewData();
        $this->generatePageHeader('Bestellung', "ECMA.js", false); //to do: set optional parameters
        $maxOrderingID = $data[count($data) - 1][0]["max_ordering_id"] + 1; // holt maxOrderingID aus Data Array und inkrementiert
        $maxOrderedArticleID = $data[count($data) - 1][0]["max_ordered_article_id"] + 1; // holt maxOrderedArticleID aus Data Array und inkrementiert
        array_pop($data); // löscht den letzten Array Index, da wir dort die aktuellste BestellID und BetellArtikelID stehen haben und nicht mehr brauchen


        echo <<<EOT

<section class="Speisekarte">
    <h1>Bestellung</h1>
    <h2>Speisekarte</h2>

EOT;

        if (sizeof($data) == 0) {
            echo "<p>Es gibt in diesem Moment keine Pizzen.</p>";
            return;
        }

        foreach ($data as $i) {
            $pizzaID = htmlspecialchars($i["article_id"]);
            $picture = htmlspecialchars($i["picture"]);
            $name = htmlspecialchars($i["name"]);
            $price = htmlspecialchars($i["price"]);
            echo <<<EOT
            <article class="PizzaArtikel">
            
            <img class="PizzaBild" src="$picture" onclick="shoppingCart.addPizza($pizzaID, '$name', $price);" alt=""/> 
      
            
             <p class="PizzaName">$name</p>
EOT;
            $price = htmlspecialchars($i["price"] . "€");
echo <<<EOT
             <p data-preispizza="$price" class="PizzaPreis">$price</p>
            </article>
EOT;
        }
        echo <<<EOT
</section>


<section class="Warenkorb">
    <h2>Warenkorb</h2>
    <!-- LABEL anschauen-->
    <form action="bestellung.php" id="bestellungAbwickeln" method="post" accept-charset="UTF-8">
        <p>
            <label for="warenkorbListBox"></label>
EOT;
      //  $countPizza = count($data);
        echo <<<EOT
            <select name="warenkorbListe[]" size="5" multiple id="warenkorbListBox" accesskey="w" tabindex="1" >
EOT;
        /*
        foreach ($data as $i) {
            $articleID = htmlspecialchars($i["article_id"]);
            $name = htmlspecialchars($i["name"]);
            echo <<<EOT
            <option value=$articleID>$name</option>
            EOT;
        }
        */

        echo <<<EOT
            </select>
        </p>
        <p id="warenkorbGesamtPreis">Gesamtpreis: 0.00€</p>
        <p>

                <label>  <input type="text" name="name" placeholder="Ihr Name" value="" required /> </label>
                <label>  <input type="text" name="adresse" id="address" placeholder="Ihre Adresse"  value="" required /></label>

        </p>
        <p>
        
           <button type="button" name="zurücksetzen" id="reset" onclick="shoppingCart.deleteAll();" accesskey="f" tabindex="3" value="">Alle Löschen</button>
            <button type="button" name="auswahlLoeschen" id="buttonBestellseite" onclick="shoppingCart.deleteSelected();" accesskey="d" tabindex="2" value="">Auswahl Löschen</button>
            <button type="submit" name="submitBtn" id="submit" onclick="order();" disabled="true" accesskey="g" tabindex="4" value="">Bestellen</button>
          
            <input type="hidden" name ="maxOrderingID" value="$maxOrderingID">
            <input type="hidden" name="maxOrderedArticleID" value="$maxOrderedArticleID">
        </p>

    </form>
</section>
EOT;


        $this->generatePageFooter();
    }

    /**
     * Processes the data that comes via GET or POST.
     * If this page is supposed to do something with submitted
     * data do it here.
     * @return void
     */
    protected function processReceivedData(): void
    {
        parent::processReceivedData();

        /*
                foreach ($_POST as $key => $value) { // PROF FRAGEN
                    $_POST[$key] = htmlspecialchars($value, ENT_QUOTES,"UTF-8");
                }
        */
        if (count($_POST) > 0) {
            if (isset($_POST['warenkorbListe'])) {
                $warenkorbListe[] = $_POST['warenkorbListe'];
            } else {
                return;
            }

            if (isset($_POST['maxOrderingID']) && is_numeric($_POST['maxOrderingID'])) {
                $maxOrderingID = intval($_POST['maxOrderingID']);
            } else {
                return;
            }

            if (isset($_POST['maxOrderedArticleID']) && is_numeric($_POST['maxOrderedArticleID'])) {
                $maxOrderedArticleID = intval($_POST['maxOrderedArticleID']);
            } else {
                return;
            }

            if (isset($_POST['name'])) {
                $name = $_POST['name'];
            } else {
                return;
            }

            if (isset($_POST['adresse'])) {
                $adresse = $_POST['adresse'];
            } else {
                return;
            }
            $addressAndName = $adresse . ", " . $name;
            $timestamp = date('Y-m-d H:i:s');
            $query = <<<SQL
        INSERT INTO ordering(ordering_id,address,ordering_time) VALUES (?,?,?);
        SQL;
            $stmt = $this->_database->prepare($query); //prepared statement beinhaltet schon real_escape_string
            $stmt->bind_param('iss', $maxOrderingID, $addressAndName, $timestamp);
            $stmt->execute();


            foreach ($warenkorbListe as $j) {
                foreach ($j as $i) {
                    $article_id = $i;
                    $status = 0;
                    $query = <<<SQL
        INSERT INTO ordered_article(ordered_article_id,ordering_id,article_id,status) VALUES (?,?,?,?);
        SQL;
                    $stmt = $this->_database->prepare($query); //prepared statement
                    $stmt->bind_param('iiii', $maxOrderedArticleID, $maxOrderingID, $article_id, $status);
                    $stmt->execute();
                    $maxOrderedArticleID++;
                }
            }


            //SessionID ist BestellID
            session_start();
            $_SESSION["latestOrderID"] = $maxOrderingID;

            header("HTTP/1.1 303 See Other");
            header('Location: ./kunde.php');
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
            $page = new bestellung();
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
bestellung::main();

// Zend standard does not like closing php-tag!
// PHP doesn't require the closing tag (it is assumed when the file ends). 
// Not specifying the closing ? >  helps to prevent accidents 
// like additional whitespace which will cause session 
// initialization to fail ("headers already sent"). 
//? >