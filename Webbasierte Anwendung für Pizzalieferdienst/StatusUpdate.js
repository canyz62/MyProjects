"use strict";

// request als globale Variable anlegen (haesslich, aber bequem)
var request = new XMLHttpRequest();

function requestData() { // fordert die Daten asynchron an
    request.open("GET", "KundenStatus.php"); // URL für HTTP-GET
    request.onreadystatechange = processData; //Callback-Handler zuordnen
    request.send(null); // Request abschicken
}


function processData() {
    if(request.readyState == 4) { // Uebertragung = DONE
        if (request.status == 200) {   // HTTP-Status = OK
            if(request.responseText != null)
                process(request.responseText);// Daten verarbeiten
            else console.error ("Dokument ist leer");
        }
        else console.error ("Uebertragung fehlgeschlagen");
    } else ;          // Uebertragung laeuft noch
}

window.setInterval(requestData, 2000);


function process(orders){
    "use strict";
    orders = JSON.parse(orders);
    const Bestellungen = document.getElementById('Bestellungen');
    while(Bestellungen.firstChild){
        Bestellungen.removeChild(Bestellungen.firstChild);
    }

    /*  Einfache Ausgabe
orders.forEach((pizza) => {
    let placeholder = document.createElement('p');
    placeholder.innerText = pizza.name + " - Status: " + processStatus(pizza.status);
    Bestellungen.appendChild(placeholder);
});
*/

    let allOrderIds = [];
    orders.forEach((orderId) => {
        if(!allOrderIds.includes(orderId.ordering_id)){
            allOrderIds.push(orderId.ordering_id);
        }
    });
    allOrderIds.forEach((orderId) => {
        let newFieldSet = document.createElement('fieldset');
        newFieldSet.classList.add('kundenFieldset');
        let legend = document.createElement("legend");
        legend.innerText = "Bestellung " + orderId;

        let textOutput = [];
        orders.forEach((pizza) => {
            if(pizza.ordering_id=== orderId) {
                textOutput.push("🍕    " + pizza.name  + "    📦 " + processStatus(pizza.status));
            }
        });
        newFieldSet.innerText = textOutput.join('\r\n'); //jede Pizza als neue Zeile ausgeben vom Array

        Bestellungen.appendChild(newFieldSet);
        newFieldSet.appendChild(legend);
    });

    /*
    //Ausgabe mit Fieldset und Legend
    orders.forEach((pizza) => {
        let newFieldSet = document.createElement('fieldset');
        let legend = document.createElement ("legend");
        legend.innerText = "Personal Information";
        newFieldSet.innerText = pizza.name + " - Status: " + processStatus(pizza.status);
        Bestellungen.appendChild(newFieldSet);
        newFieldSet.appendChild(legend);

    });
*/

}


function processStatus(status){
    if(status == 0) return "bestellt";
    if(status == 1) return "im Ofen";
    if(status == 2) return "fertig";
    if(status == 3) return "unterwegs";
    if(status == 4) return "geliefert";
    return "missing";

}