
package de.pizzamanager.gui;

// Bibliotheken für Dateien
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

// Bibliotheken für Datum, Listen und Formatierung
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class OrderStore {

 // Speicherort für unsere Bestellungen
 private final Path path = Path.of(
         System.getProperty("user.home"),
         "PizzaManager",
         "bestellungen.csv"
 );


 // ========================================
 // METHODE 1: Bestellung speichern
 // ========================================

 public void save(List<String> items, double total)
         throws IOException {

  // Ordner erstellen, falls er nicht existiert
  Files.createDirectories(path.getParent());

  // Prüfen, ob die CSV-Datei bereits existiert
  boolean header = !Files.exists(path);

  // Datei zum Schreiben öffnen
  try (BufferedWriter writer = Files.newBufferedWriter(
          path,
          StandardCharsets.UTF_8,
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
  )) {

   // Überschriften nur bei einer neuen Datei
   if (header) {
    writer.write(
            "Zeitpunkt;Positionen;Gesamt_EUR\n"
    );
   }

   // Alle bestellten Produkte zusammenfassen
   String bestellung = String.join(" | ", items);

   // Anführungszeichen für CSV maskieren
   bestellung = bestellung.replace("\"", "\"\"");

   // Aktuelles Datum und Uhrzeit
   LocalDateTime zeitpunkt = LocalDateTime.now();

   // Gesamtpreis mit zwei Nachkommastellen
   String gesamtpreis = String.format(
           Locale.GERMANY,
           "%.2f",
           total
   );

   // Bestellung in die CSV-Datei schreiben
   writer.write(
           zeitpunkt + ";\"" +
                   bestellung + "\";" +
                   gesamtpreis + "\n"
   );
  }
 }


 // ========================================
 // METHODE 2: Bestellungen lesen
 // ========================================

 public String read() throws IOException {

  // Prüfen, ob die Datei existiert
  if (Files.exists(path)) {

   // Gesamten Dateiinhalt lesen
   return Files.readString(
           path,
           StandardCharsets.UTF_8
   );
  }

  // Falls noch keine Datei vorhanden ist
  return "Noch keine gespeicherten Bestellungen.";
 }
}
