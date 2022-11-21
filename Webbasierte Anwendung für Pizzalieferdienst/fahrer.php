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
class fahrer extends Page
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
        $bestellungen = array();
        $sql = 'SELECT ordered_article.ordered_article_id, ordered_article.ordering_id, ordered_article.article_id, article.name, article.price, ordered_article.status, ordering.address FROM ordered_article, article, ordering WHERE article.article_id=ordered_article.article_id AND ordering.ordering_id=ordered_article.ordering_id ORDER BY ordering.ordering_id;';
        // $sql = 'SELECT ordered_article.ordered_article_id, ordered_article.ordering_id, ordered_article.article_id, article.name, article.price, ordered_article.status, ordering.address FROM ordered_article, article, ordering WHERE article.article_id=ordered_article.article_id AND ordering.ordering_id=ordered_article.ordering_id AND ordered_article.status >1 AND ordered_article.status <4;';
        $recordset = $this->_database->query($sql);
        if (!$recordset) throw new Exception ("Fehler in Abfrage:" . $this->database->error);
        while ($record = $recordset->fetch_assoc()) {
            $bestellungen[] = $record;
        }
        $recordset->free();
        return $bestellungen;
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
        $this->generatePageHeader('Fahrer', "", true); //to do: set optional parameters
        $addressShowOnce =false;
        $statusShowOnce = false;
        $allOrderingID = [];
        $orderPrice = floatval(0);

        // all Order ID with Status from 0 - 4 are saved in array
        foreach ($data as $i) {  //get all distinct Ordering ID
            if (!in_array(htmlspecialchars($i["ordering_id"]), $allOrderingID)) { //if the i Position Ordering ID is not in our array $allOrderingId, then it is added to it
                $allOrderingID[] = htmlspecialchars($i["ordering_id"]);
            }
        }

        //all Order ID with status less 2 and greater 3 are saved in array

        $skiplist = [];
        foreach ($data as $i){
            if(htmlspecialchars($i['status']) < 2 or htmlspecialchars($i['status']) > 3){
                if(!in_array(htmlspecialchars($i["ordering_id"]),$skiplist)){
                    $skiplist[] = htmlspecialchars($i["ordering_id"]);

                }
            }
        }


        //all Order ID (with status less 2 and greater 4) from $skipList are subtracted from $allOrderingID
        $filteredOrderID = array_diff( $allOrderingID, $skiplist );







        echo <<<EOT
<section class="Fahrer">
    <h1>Fahrer</h1>
EOT;

        if (sizeof($data) == 0) {
            echo "<p>Es gibt in diesem Moment keine Bestellungen</p>";
            return;
        }

        foreach ($filteredOrderID as $k) {
            echo <<<EOT
    <form action="fahrer.php" id="fahrerBestellungStatus$k" method="post" accept-charset="UTF-8">

        <fieldset>
            <legend accesskey="1">Bestellung $k</legend> <!-- Überschrift von Rechteck (Gruppe)-->
EOT;
            foreach ($data as $i) {

                $address=htmlspecialchars($i["address"]);
                if (htmlspecialchars($i["ordering_id"]) == $k) {

                    if (!$addressShowOnce) {
                        echo <<<EOT
            <p>$address</p>
EOT;
                    }
                    $addressShowOnce = true;

                    if (!$statusShowOnce) {
                        $status = intval(htmlspecialchars($i['status']));


                        $isChecked = $status == 2 ? 'checked' : null;
                        echo <<<EOT
            <label> <input type="radio" $isChecked name="bestellungStatus" value="2" onclick="document.forms['fahrerBestellungStatus$k'].submit();" /> fertig </label>
EOT;
                        $isChecked = $status == 3 ? 'checked' : null;
                        echo <<<EOT
            <label> <input type="radio" $isChecked name="bestellungStatus" value="3" onclick="document.forms['fahrerBestellungStatus$k'].submit();" />unterwegs </label>
EOT;
                        $isChecked = $status == 4 ? 'checked' : null;

                        $orderingID=htmlspecialchars($i["ordering_id"]);
                        echo <<<EOT
            <label> <input type="radio" $isChecked name="bestellungStatus" value="4" onclick="document.forms['fahrerBestellungStatus$k'].submit();" /> geliefert </label>
             <input type="hidden" name="ordering_id" value="$orderingID"/>

EOT;

                    }
                    $statusShowOnce = true;

                    $name=htmlspecialchars($i["name"]);
                    $price=htmlspecialchars($i["price"]."€");
                    $orderPrice+=floatval(htmlspecialchars($i["price"]));
                    echo <<<EOT
                    <p>$name: $price</p>
            <!-- name bei allen Radio gleich um zu gruppieren, sonst mehrfachauswahl möglich // Wert von name wird an server übertragen, hier wird aber value wert üvertragen-->

EOT;

                }
            }
            $orderPrice = $orderPrice."€";
                echo <<<EOT
            <p>Gesamtpreis: $orderPrice</p>
EOT;
                $orderPrice = 0;
                echo <<<EOT
        </fieldset>   <!-- Fieldset erzeugt Rechteck zum Gruppieren  https://html5-tutorial.net/de/337/formulare/radio-buttons/   Label macht Text neben radio auch klickbar -->

       <!--  <p>
              <input type="submit" value="Ändern">
          </p> -->

      </form>

EOT;

            $addressShowOnce = false;
            $statusShowOnce = false;

        }

        echo <<<EOT
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


        if (count($_POST) > 0) {
            if (isset($_POST['ordering_id']) && is_numeric($_POST['ordering_id'])) {
                $ordering_id = (int) $_POST['ordering_id'];
            } else {
                return;
            }

            if (isset($_POST['bestellungStatus']) && is_numeric($_POST['bestellungStatus'])) {
                $status = (int) $_POST['bestellungStatus'];
            } else {
                return;
            }

            $query = <<<SQL
        UPDATE ordered_article
        SET status = ?
        WHERE ordering_id = ?
        SQL;
            $stmt = $this->_database->prepare($query); //prepared statement
            $stmt->bind_param('si', $status, $ordering_id);
            $stmt->execute();
            header("HTTP/1.1 303 See Other");
            header('Location:  Fahrer.php');
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
            $page = new fahrer();
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
fahrer::main();

// Zend standard does not like closing php-tag!
// PHP doesn't require the closing tag (it is assumed when the file ends). 
// Not specifying the closing ? >  helps to prevent accidents 
// like additional whitespace which will cause session 
// initialization to fail ("headers already sent"). 
//? >