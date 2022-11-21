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
class kunde extends Page
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
    public
    static function main(): void
    {
        try {
            $page = new kunde();
            $page->processReceivedData();
            $page->generateView();
        } catch (Exception $e) {
            //header("Content-type: text/plain; charset=UTF-8");
            header("Content-type: text/html; charset=UTF-8");
            echo $e->getMessage();
        }
    }

    /**
     * Processes the data that comes via GET or POST.
     * If this page is supposed to do something with submitted
     * data do it here.
     * @return void
     */
    protected
    function processReceivedData(): void
    {
        parent::processReceivedData();
        // to do: call processReceivedData() for all members

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
        $this->generatePageHeader('Kunde', "StatusUpdate.js", false);


        echo <<<EOT
<script onload="requestData()"></script>
<section class="Kunde">
    <h1>Kunde</h1>
<article class="kundenBestellungen" id="Bestellungen">
<p>Hier gibt es aktuell nichts zu sehen</p>
</article>
EOT;
        /*
        $allOrderingID = [];
        foreach ($data as $i) {
            if (!in_array($i["ordering_id"], $allOrderingID)) {
                $allOrderingID[] = htmlspecialchars($i["ordering_id"]);
            }
        }

        foreach ($allOrderingID as $k) {


            echo <<<EOT



    <fieldset>
        <legend accesskey="1">Bestellung $k</legend> <!-- Überschrift von Rechteck (Gruppe)-->
EOT;

            foreach ($data as $i) {
                if (htmlspecialchars($i["ordering_id"]) == $k) {
                    $name = htmlspecialchars($i["name"]);
                    echo <<<EOT
        <p>$name: Status: 
EOT;
                    $statusMessage = null;
                    if (htmlspecialchars($i["status"]) == 0) {
                        $statusMessage = "bestellt";
                    }
                    if (htmlspecialchars($i["status"]) == 1) {
                        $statusMessage = "im Ofen";
                    }
                    if (htmlspecialchars($i["status"]) == 2) {
                        $statusMessage = "fertig";
                    }
                    if (htmlspecialchars($i["status"]) == 3) {
                        $statusMessage = "unterwegs";
                    }
                    if (htmlspecialchars($i["status"]) == 4) {
                        $statusMessage = "geliefert";
                    }
                    echo <<<EOT
            $statusMessage</p>

EOT;
                }
            }

            echo <<<EOT
    </fieldset>
    EOT;
        }
        */
        echo <<< EOT
<a href="bestellung.php">
    <button type="button" id="bestellungAufgeben" accesskey="b" tabindex="2" value="">Bestellung aufgeben</button>
</a>

</section>
EOT;


        $this->generatePageFooter();

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
        if (isset($_SESSION["latestOrderID"])) {
            $ID = $_SESSION["latestOrderID"];
            $sql = "SELECT ordered_article.ordered_article_id, ordered_article.ordering_id, ordered_article.article_id, article.name, ordered_article.status FROM ordered_article, article, ordering WHERE article.article_id=ordered_article.article_id AND ordering.ordering_id=ordered_article.ordering_id AND ordered_article.ordering_id = '{$ID}';";
            //$sql = "SELECT ordered_article.ordered_article_id, ordered_article.ordering_id, ordered_article.article_id, article.name, ordered_article.status FROM ordered_article, article, ordering WHERE article.article_id=ordered_article.article_id AND ordering.ordering_id=ordered_article.ordering_id ORDER BY ordering_id;";
            $recordset = $this->_database->query($sql);
            if (!$recordset) throw new Exception ("Fehler in Abfrage:" . $this->database->error);
            while ($record = $recordset->fetch_assoc()) {
                $bestellungen[] = $record;
            }
            $recordset->free();
        }
        return $bestellungen;

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
}

// This call is starting the creation of the page. 
// That is input is processed and output is created.
session_start();
kunde::main();
// Zend standard does not like closing php-tag!
// PHP doesn't require the closing tag (it is assumed when the file ends). 
// Not specifying the closing ? >  helps to prevent accidents 
// like additional whitespace which will cause session 
// initialization to fail ("headers already sent"). 
//? >