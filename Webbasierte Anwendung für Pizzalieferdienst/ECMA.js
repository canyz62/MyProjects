"use strict";

class Cart {
    constructor() {
        this.cart = []; // Array Pizza:  pizzaID - pizzaName - pizzaPrice
    }

    cartLength() {
        return this.cart.length;
    }

    addPizza(pizzaID, pizzaName, pizzaPrice) {
        "use strict";
        this.cart.push([pizzaID, pizzaName, pizzaPrice]);
        let selected = document.getElementById('warenkorbListBox');
        //selected.add(new Option(pizzaName, pizzaID));
        let option=document.createElement("option");
        option.value=pizzaID;
        option.text=pizzaName;
        selected.appendChild(option);
        this.totalPrice();
        orderButton();
    }

    totalPrice() {
        "use strict";
        let price = 0;
        let i = 0;
        for (i; i < this.cart.length; i++) {
            price += this.cart[i][2];
        }
        let showPrice = document.getElementById('warenkorbGesamtPreis');
        showPrice.innerText = "Gesamtpreis: " + price.toFixed(2) + "€";
    }

    deleteSelected() {
        // for Schleife rückwärts laufen, wegen doppel verkettete Liste wäre alternative
        // A B B A - Warenkorb, man will nur das erste B aus Warenkorb löschen
        "use strict";
        let selectbox = document.getElementById('warenkorbListBox');
        let i = 0;
        while (i < selectbox.length) {
            if (!selectbox[i].selected) {
                i++;
                continue;
            }
            selectbox.remove(i);
            this.cart.splice(i, 1);
        }
        this.totalPrice();
        orderButton();
    }


    deleteAll() {
        console.log(this.cart)
        "use strict";
        let selectbox = document.getElementById('warenkorbListBox');
        let i = 0;
        while (i < selectbox.length) {
            selectbox.remove(i);
            this.cart.splice(i, 1);
        }
        this.totalPrice();
        orderButton();

    }
}


let shoppingCart = new Cart();


function orderButton(){
    "use strict";
    let submitBtn = document.getElementById('submit');
    if(addressAndPizzaCheck()){
        submitBtn.disabled = false;
    }
    else submitBtn.disabled = true;
}

function addressAndPizzaCheck(){
    "use strict";
    let text = document.getElementById("address");
    if (text.value===""){
        return false;
    }
    if(shoppingCart.cartLength()==0){
        return false;
    }
    return true;
}

function order(){
    "use strict";
    let shoppingcart = document.getElementById('warenkorbListBox');
    let i = 0;
    for(i; i<shoppingcart.length; i++){
        shoppingcart[i].selected=true;
    }
}

function Init(){
    document.getElementById("address").oninput=orderButton;

}
window.onload=Init;