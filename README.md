# PizzaManager – Java Swing 

Reine Java-Swing-Anwendung mit modern gestalteter Bestelloberfläche, orientiert am gelieferten Screenshot. Enthält Pizzaauswahl, Größen, Extras, Menge, Warenkorb, CSV-Speicherung und Bestellübersicht.

## Start in IntelliJ IDEA

1. Projektordner öffnen und JDK 21 einstellen.
2. `src/main/java` als Sources Root und `src/main/resources` als Resources Root markieren (falls nicht automatisch erkannt).
3. `de.pizzamanager.Main` ausführen.

## Start über Terminal (JDK 21)

```bash
mkdir -p out
javac -encoding UTF-8 -d out src/main/java/de/pizzamanager/*.java
cp -r src/main/resources/* out/
java -cp out de.pizzamanager.Main
```

CSV-Datei: `~/PizzaManager/bestellungen.csv`. Bilder sind im Ressourcenordner. Kein JavaFX, Spring oder externe Bibliotheken.
