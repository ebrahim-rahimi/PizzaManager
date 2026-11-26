
package de.pizzamanager;

public enum Pizza {

 MARGHERITA(
         "Margherita",
         "Tomatensauce, Mozzarella, Basilikum",
         7.50,
         "margherita.png"
 ),

 SALAMI(
         "Salami",
         "Tomatensauce, Mozzarella, Salami",
         8.90,
         "salami.png"
 ),

 TONNO(
         "Tonno",
         "Tomatensauce, Mozzarella, Thunfisch, Zwiebeln",
         9.50,
         "tonno.png"
 ),

 VEGETARIA(
         "Vegetaria",
         "Tomatensauce, Mozzarella, Paprika, Champignons",
         9.20,
         "vegetaria.png"
 );

 public final String title, description, image;
 public final double price;

 Pizza(String t, String d, double p, String i) {
  title = t;
  description = d;
  price = p;
  image = i;
 }
}
