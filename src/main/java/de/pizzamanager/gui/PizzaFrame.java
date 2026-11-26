
package de.pizzamanager.gui;

import de.pizzamanager.Pizza;

import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;


public class PizzaFrame extends JFrame {

 // ==================================================
 // 1. FARBEN UND DESIGN
 // ==================================================

 private static final Color RED = new Color(139, 36, 39);
 private static final Color DARK = new Color(62, 16, 22);
 private static final Color BG = new Color(247, 247, 249);


 // ==================================================
 // 2. ATTRIBUTE UND GUI-KOMPONENTEN
 // ==================================================

 private final Map<Pizza, JRadioButton> pizzas =
         new LinkedHashMap<>();

 private final ButtonGroup pizzaGroup = new ButtonGroup();
 private final ButtonGroup sizeGroup = new ButtonGroup();

 private final Map<String, JCheckBox> extras =
         new LinkedHashMap<>();

 private final JSpinner quantity =
         new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));

 private final JPanel cartRows = new JPanel();

 private final JPanel mainContent =
         new JPanel(new BorderLayout());

 private final JLabel totalLabel = new JLabel("0,00 €");
 private final JLabel heroPicture = new JLabel();

 private final List<CartLine> cart = new ArrayList<>();
 private final OrderStore store = new OrderStore();


 // ==================================================
 // 3. EXTRAS UND PREISE
 // ==================================================

 private final String[] extraNames = {
         "Extra Käse",
         "Zwiebeln",
         "Paprika",
         "Champignons",
         "Oliven"
 };

 private final double[] extraPrices = {
         1.00, 0.50, 0.50, 0.50, 0.50
 };


 // ==================================================
 // 4. WARENKORBPOSITION
 // ==================================================

 private static class CartLine {

  String label;
  double price;

  CartLine(String label, double price) {
   this.label = label;
   this.price = price;
  }
 }


 // ==================================================
 // 5. KONSTRUKTOR
 // ==================================================

 public PizzaFrame() {

  super("PizzaManager");

  setDefaultCloseOperation(EXIT_ON_CLOSE);

  setMinimumSize(new Dimension(1100, 740));
  setSize(1510, 950);
  setLocationRelativeTo(null);

  setLayout(new BorderLayout());

  // Seitenleiste links
  add(sidebar(), BorderLayout.WEST);

  // Hauptbereich rechts
  mainContent.setBackground(BG);
  add(mainContent, BorderLayout.CENTER);

  // Bestellansicht starten
  showOrder();
 }


 // ==================================================
 // 6. SEITENLEISTE ERSTELLEN
 // ==================================================

 private JPanel sidebar() {

  JPanel side = new JPanel();

  side.setBackground(DARK);
  side.setPreferredSize(new Dimension(300, 0));

  side.setLayout(
          new BoxLayout(side, BoxLayout.Y_AXIS)
  );

  side.setBorder(
          new EmptyBorder(34, 5, 20, 5)
  );


  // ---------- Logo ----------

  JLabel logo = new JLabel(
          "🍕",
          SwingConstants.CENTER
  );

  logo.setForeground(Color.WHITE);
  logo.setFont(new Font("SansSerif", Font.PLAIN, 58));
  logo.setAlignmentX(Component.CENTER_ALIGNMENT);

  side.add(logo);


  // ---------- Programmname ----------

  JLabel brand = new JLabel(
          "PizzaManager",
          SwingConstants.CENTER
  );

  brand.setForeground(Color.WHITE);
  brand.setFont(new Font("SansSerif", Font.BOLD, 27));
  brand.setAlignmentX(Component.CENTER_ALIGNMENT);

  side.add(brand);

  side.add(Box.createVerticalStrut(52));


  // ==========================================
  // NAVIGATIONSBEREICH
  // ==========================================

  // GridLayout:
  // 4 Zeilen, 1 Spalte, 8 Pixel Abstand
  JPanel navigation = new JPanel(
          new GridLayout(4, 1, 0, 8)
  );

  navigation.setBackground(DARK);

  // Volle Breite innerhalb der Seitenleiste
  navigation.setPreferredSize(
          new Dimension(290, 240)
  );

  navigation.setMaximumSize(
          new Dimension(Integer.MAX_VALUE, 240)
  );

  navigation.setAlignmentX(
          Component.CENTER_ALIGNMENT
  );


  // ---------- Navigationsbuttons ----------

  navigation.add(
          nav("🛒  Bestellung", this::showOrder)
  );

  navigation.add(
          nav("☷  Bestellübersicht", this::showHistory)
  );

  navigation.add(
          nav(
                  "⚙  Verwaltung",
                  () -> JOptionPane.showMessageDialog(
                          this,
                          "Verwaltung ist für eine spätere Erweiterung vorgesehen."
                  )
          )
  );

  navigation.add(
          nav("⏻  Beenden", this::dispose)
  );


  // Navigation zur Seitenleiste hinzufügen
  side.add(navigation);

  // Freien Platz unterhalb der Navigation lassen
  side.add(Box.createVerticalGlue());

  return side;
 }


 // ==================================================
 // 7. NAVIGATIONSBUTTON ERSTELLEN
 // ==================================================

 private JButton nav(String text, Runnable action) {

  JButton button = new JButton(text);

  // Text links ausrichten
  button.setHorizontalAlignment(SwingConstants.LEFT);

  // Größe des Buttons
  button.setPreferredSize(new Dimension(250, 54));

  button.setMaximumSize(
          new Dimension(Integer.MAX_VALUE, 54)
  );

  // Ausrichtung
  button.setAlignmentX(Component.LEFT_ALIGNMENT);

  // Design
  button.setBackground(RED);
  button.setForeground(Color.WHITE);

  button.setFont(
          new Font("SansSerif", Font.BOLD, 15)
  );

  button.setBorder(
          new EmptyBorder(12, 12, 12, 6)
  );

  button.setFocusPainted(false);

  // Aktion beim Anklicken
  button.addActionListener(e -> action.run());

  return button;
 }


 // ==================================================
 // 8. HAUPTANSICHT AUFBAUEN
 // ==================================================

 private void showOrder() {

  // Alte Inhalte entfernen
  mainContent.removeAll();

  // Banner oben
  mainContent.add(header(), BorderLayout.NORTH);


  // ---------- Hauptbereich ----------

  JPanel body = new JPanel(
          new BorderLayout(15, 0)
  );

  body.setBackground(BG);

  body.setBorder(
          new EmptyBorder(20, 18, 20, 18)
  );


  // ---------- Mittlerer Bereich ----------

  JPanel center = new JPanel(new GridBagLayout());
  center.setOpaque(false);

  GridBagConstraints g = new GridBagConstraints();

  g.insets = new Insets(0, 0, 15, 15);
  g.fill = GridBagConstraints.BOTH;
  g.weighty = 1;


  // Pizzaauswahl
  g.gridx = 0;
  g.gridy = 0;
  g.weightx = 0.59;
  g.gridheight = 2;

  center.add(pizzaPanel(), g);


  // Größenauswahl
  g.gridx = 1;
  g.gridy = 0;
  g.weightx = 0.41;
  g.gridheight = 1;

  center.add(sizePanel(), g);


  // Extras
  g.gridy = 1;

  center.add(extrasPanel(), g);


  // Pizza-Bild
  g.gridx = 0;
  g.gridy = 2;
  g.weightx = 0.59;
  g.weighty = 0.9;

  center.add(imagePanel(), g);


  // Anzahl
  g.gridx = 1;
  g.weightx = 0.41;

  center.add(quantityPanel(), g);


  body.add(center, BorderLayout.CENTER);


  // ---------- Warenkorb ----------

  JPanel right = cartPanel();

  right.setPreferredSize(
          new Dimension(340, 0)
  );

  body.add(right, BorderLayout.EAST);


  // ---------- Scrollbereich ----------

  JScrollPane scroll = new JScrollPane(body);

  scroll.setBorder(null);

  scroll.getVerticalScrollBar()
          .setUnitIncrement(18);

  mainContent.add(scroll, BorderLayout.CENTER);


  // Oberfläche aktualisieren
  mainContent.revalidate();
  mainContent.repaint();

  updateHero();
  refreshCart();
 }


 // ==================================================
 // 9. BANNER ERSTELLEN
 // ==================================================

 private JComponent header() {

  JPanel banner = new JPanel(new BorderLayout());

  banner.setPreferredSize(new Dimension(0, 165));
  banner.setBackground(DARK);


  // Bannerbild
  JLabel image = new JLabel(
          icon("banner.png", 470, 165)
  );

  banner.add(image, BorderLayout.WEST);


  // Textbereich
  JPanel words = new JPanel();

  words.setOpaque(false);

  words.setLayout(
          new BoxLayout(words, BoxLayout.Y_AXIS)
  );

  words.setBorder(
          new EmptyBorder(25, 15, 15, 25)
  );


  JLabel heading = new JLabel(
          "Willkommen beim PizzaManager!"
  );

  heading.setFont(
          new Font(
                  "SansSerif",
                  Font.BOLD | Font.ITALIC,
                  34
          )
  );

  heading.setForeground(Color.WHITE);
  heading.setAlignmentX(Component.CENTER_ALIGNMENT);

  words.add(heading);
  words.add(Box.createVerticalStrut(9));


  JLabel subtitle = new JLabel(
          "Stelle deine Lieblingspizza zusammen."
  );

  subtitle.setForeground(Color.WHITE);

  subtitle.setFont(
          new Font("SansSerif", Font.PLAIN, 21)
  );

  subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

  words.add(subtitle);

  banner.add(words, BorderLayout.CENTER);

  return banner;
 }


 // ==================================================
 // 10. WIEDERVERWENDBARE KARTE
 // ==================================================

 private JPanel card(String title) {

  JPanel panel = new JPanel(new BorderLayout());

  panel.setBackground(Color.WHITE);

  panel.setBorder(
          new CompoundBorder(
                  new LineBorder(
                          new Color(222, 222, 228),
                          1,
                          true
                  ),
                  new EmptyBorder(0, 0, 0, 0)
          )
  );


  // Überschrift
  JLabel heading = new JLabel(title);

  heading.setOpaque(true);
  heading.setBackground(RED);
  heading.setForeground(Color.WHITE);

  heading.setFont(
          new Font("SansSerif", Font.BOLD, 20)
  );

  heading.setBorder(
          new EmptyBorder(11, 14, 11, 12)
  );

  panel.add(heading, BorderLayout.NORTH);

  return panel;
 }


 // ==================================================
 // 11. PIZZA-AUSWAHL
 // ==================================================

 private JPanel pizzaPanel() {

  JPanel card = card("1. Pizza auswählen");

  JPanel list = new JPanel();

  list.setBackground(Color.WHITE);

  list.setLayout(
          new BoxLayout(list, BoxLayout.Y_AXIS)
  );

  list.setBorder(
          new EmptyBorder(10, 10, 10, 10)
  );

  pizzas.clear();


  // Alle Pizzasorten durchlaufen
  for (Pizza pizza : Pizza.values()) {

   // RadioButton
   JRadioButton radio = new JRadioButton();

   radio.setOpaque(false);

   pizzaGroup.add(radio);
   pizzas.put(pizza, radio);


   // Zeile für eine Pizza
   JPanel line = new JPanel(
           new BorderLayout(10, 0)
   );

   line.setBackground(Color.WHITE);

   line.setBorder(
           new EmptyBorder(10, 2, 10, 4)
   );

   line.add(radio, BorderLayout.WEST);


   // Pizza-Bild
   JLabel pizzaImage = new JLabel(
           icon(pizza.image, 82, 66)
   );

   line.add(pizzaImage, BorderLayout.CENTER);


   // Name und Beschreibung
   JPanel words = new JPanel();

   words.setOpaque(false);

   words.setLayout(
           new BoxLayout(words, BoxLayout.Y_AXIS)
   );


   JLabel name = new JLabel(
           pizza.title + "   " + money(pizza.price)
   );

   name.setFont(
           new Font("SansSerif", Font.BOLD, 16)
   );


   JLabel description = new JLabel(
           "<html><div style='width:185px'>" +
                   pizza.description +
                   "</div></html>"
   );

   description.setFont(
           new Font("SansSerif", Font.PLAIN, 11)
   );


   words.add(name);
   words.add(Box.createVerticalStrut(4));
   words.add(description);

   line.add(words, BorderLayout.EAST);


   // Zeilenhöhe
   line.setMaximumSize(
           new Dimension(Integer.MAX_VALUE, 105)
   );

   list.add(line);


   // Trennlinie
   if (pizza != Pizza.VEGETARIA) {
    list.add(new JSeparator());
   }


   // Bild beim Auswählen aktualisieren
   radio.addActionListener(e -> updateHero());
  }


  // Standardpizza auswählen
  pizzas.get(Pizza.MARGHERITA).setSelected(true);

  card.add(list, BorderLayout.CENTER);

  return card;
 }


 // ==================================================
 // 12. GRÖSSENAUSWAHL
 // ==================================================

 private JPanel sizePanel() {

  JPanel card = card("2. Größe wählen");
  JPanel list = vertical();

  String[] labels = {
          "Klein (Ø 24 cm)       + 0,00 €",
          "Mittel (Ø 28 cm)      + 1,50 €",
          "Groß (Ø 32 cm)         + 2,50 €"
  };


  for (String label : labels) {

   JRadioButton radio = new JRadioButton(label);

   radio.setBackground(Color.WHITE);

   radio.setFont(
           new Font("SansSerif", Font.PLAIN, 16)
   );

   radio.setBorder(
           new EmptyBorder(7, 3, 7, 3)
   );

   sizeGroup.add(radio);
   list.add(radio);


   // Erste Größe vorauswählen
   if (sizeGroup.getButtonCount() == 1) {
    radio.setSelected(true);
   }
  }


  card.add(list, BorderLayout.CENTER);

  return card;
 }


 // ==================================================
 // 13. EXTRAS AUSWÄHLEN
 // ==================================================

 private JPanel extrasPanel() {

  JPanel card = card("3. Extras (optional)");
  JPanel list = vertical();

  extras.clear();


  for (int i = 0; i < extraNames.length; i++) {

   String name = extraNames[i];
   double price = extraPrices[i];

   JCheckBox checkbox = new JCheckBox(
           name + "                         + " +
                   money(price)
   );

   checkbox.setBackground(Color.WHITE);

   checkbox.setFont(
           new Font("SansSerif", Font.PLAIN, 14)
   );

   checkbox.setBorder(
           new EmptyBorder(5, 2, 5, 2)
   );

   extras.put(name, checkbox);
   list.add(checkbox);
  }


  card.add(list, BorderLayout.CENTER);

  return card;
 }


 // ==================================================
 // 14. VERTIKALES PANEL ERSTELLEN
 // ==================================================

 private JPanel vertical() {

  JPanel panel = new JPanel();

  panel.setBackground(Color.WHITE);

  panel.setLayout(
          new BoxLayout(panel, BoxLayout.Y_AXIS)
  );

  panel.setBorder(
          new EmptyBorder(12, 12, 12, 12)
  );

  return panel;
 }


 // ==================================================
 // 15. GROSSES PIZZA-BILD
 // ==================================================

 private JPanel imagePanel() {

  JPanel panel = new JPanel(new BorderLayout());

  panel.setBackground(Color.WHITE);

  panel.setBorder(
          new LineBorder(new Color(222, 222, 228))
  );

  heroPicture.setHorizontalAlignment(
          SwingConstants.CENTER
  );

  panel.add(heroPicture);

  return panel;
 }


 // ==================================================
 // 16. ANZAHL UND HINZUFÜGEN-BUTTON
 // ==================================================

 private JPanel quantityPanel() {

  JPanel card = card("4. Anzahl");
  JPanel inside = vertical();


  // Anzahl
  quantity.setMaximumSize(
          new Dimension(110, 40)
  );

  quantity.setFont(
          new Font("SansSerif", Font.BOLD, 18)
  );

  quantity.setAlignmentX(
          Component.CENTER_ALIGNMENT
  );

  inside.add(Box.createVerticalStrut(10));
  inside.add(quantity);
  inside.add(Box.createVerticalGlue());


  // Button
  JButton add = actionButton(
          "🛒  Zur Bestellung hinzufügen",
          new Color(36, 131, 47)
  );

  add.setAlignmentX(Component.CENTER_ALIGNMENT);

  add.addActionListener(e -> addToCart());

  inside.add(add);
  inside.add(Box.createVerticalStrut(10));


  card.add(inside, BorderLayout.CENTER);

  return card;
 }


 // ==================================================
 // 17. WARENKORB-PANEL
 // ==================================================

 private JPanel cartPanel() {

  JPanel card = card("🛒  Warenkorb");


  // Warenkorbpositionen
  cartRows.setLayout(
          new BoxLayout(cartRows, BoxLayout.Y_AXIS)
  );

  cartRows.setBackground(Color.WHITE);


  JScrollPane scroll = new JScrollPane(cartRows);

  scroll.setBorder(
          new EmptyBorder(10, 10, 10, 10)
  );

  card.add(scroll, BorderLayout.CENTER);


  // Unterer Bereich
  JPanel bottom = vertical();

  JPanel sum = new JPanel(new BorderLayout());
  sum.setBackground(Color.WHITE);


  JLabel title = new JLabel("Gesamt:");

  title.setFont(
          new Font("SansSerif", Font.BOLD, 21)
  );

  totalLabel.setFont(
          new Font("SansSerif", Font.BOLD, 22)
  );

  sum.add(title, BorderLayout.WEST);
  sum.add(totalLabel, BorderLayout.EAST);

  bottom.add(sum);
  bottom.add(Box.createVerticalStrut(22));


  // Bestellung abschließen
  JButton checkout = actionButton(
          "▣  Bestellung abschließen",
          new Color(191, 39, 43)
  );

  checkout.addActionListener(e -> checkout());

  bottom.add(checkout);
  bottom.add(Box.createVerticalStrut(10));


  // Warenkorb leeren
  JButton clear = actionButton(
          "Warenkorb leeren",
          new Color(103, 106, 114)
  );

  clear.addActionListener(e -> {
   cart.clear();
   refreshCart();
  });

  bottom.add(clear);

  card.add(bottom, BorderLayout.SOUTH);

  return card;
 }


 // ==================================================
 // 18. ALLGEMEINEN AKTIONSBUTTON ERSTELLEN
 // ==================================================

 private JButton actionButton(String text, Color color) {

  JButton button = new JButton(text);

  button.setFont(
          new Font("SansSerif", Font.BOLD, 15)
  );

  button.setBackground(color);
  button.setForeground(Color.WHITE);

  button.setFocusPainted(false);

  button.setBorder(
          new EmptyBorder(15, 10, 15, 10)
  );

  button.setMaximumSize(
          new Dimension(Integer.MAX_VALUE, 55)
  );

  return button;
 }


 // ==================================================
 // 19. AUSGEWÄHLTE PIZZA ERMITTELN
 // ==================================================

 private Pizza selectedPizza() {

  for (Map.Entry<Pizza, JRadioButton> entry
          : pizzas.entrySet()) {

   if (entry.getValue().isSelected()) {
    return entry.getKey();
   }
  }

  return Pizza.MARGHERITA;
 }


 // ==================================================
 // 20. GROSSES PIZZA-BILD AKTUALISIEREN
 // ==================================================

 private void updateHero() {

  heroPicture.setIcon(
          icon(selectedPizza().image, 470, 260)
  );
 }


 // ==================================================
 // 21. PIZZA ZUM WARENKORB HINZUFÜGEN
 // ==================================================

 private void addToCart() {

  // Pizza ermitteln
  Pizza pizza = selectedPizza();


  // Größe ermitteln
  int size = 0;
  int index = 0;

  Enumeration<AbstractButton> buttons =
          sizeGroup.getElements();

  while (buttons.hasMoreElements()) {

   AbstractButton button = buttons.nextElement();

   if (button.isSelected()) {
    size = index;
    break;
   }

   index++;
  }


  // Größen und Aufpreise
  String[] sizes = {
          "Klein",
          "Mittel",
          "Groß"
  };

  double[] sizePrices = {
          0.00,
          1.50,
          2.50
  };


  // Grundpreis berechnen
  double price = pizza.price + sizePrices[size];


  // Bestelltext erstellen
  StringBuilder label = new StringBuilder();

  label.append(pizza.title);
  label.append(" (");
  label.append(sizes[size]);
  label.append(")");


  // Extras berücksichtigen
  for (int i = 0; i < extraNames.length; i++) {

   String extraName = extraNames[i];
   JCheckBox checkbox = extras.get(extraName);

   if (checkbox.isSelected()) {

    label.append(" + ");
    label.append(extraName);

    price += extraPrices[i];
   }
  }


  // Anzahl berücksichtigen
  int count = (Integer) quantity.getValue();

  double finalPrice = moneyValue(price * count);


  // Warenkorbposition erstellen
  CartLine line = new CartLine(
          count + "× " + label,
          finalPrice
  );

  cart.add(line);

  refreshCart();
 }


 // ==================================================
 // 22. PREISE FORMATIEREN
 // ==================================================

 private double moneyValue(double value) {

  return Math.round(value * 100) / 100.0;
 }


 private static String money(double value) {

  return String.format(
          Locale.GERMANY,
          "%.2f €",
          value
  );
 }


 // ==================================================
 // 23. GESAMTPREIS BERECHNEN
 // ==================================================

 private double total() {

  double sum = 0;

  for (CartLine item : cart) {
   sum += item.price;
  }

  return sum;
 }


 // ==================================================
 // 24. WARENKORB AKTUALISIEREN
 // ==================================================

 private void refreshCart() {

  cartRows.removeAll();


  for (CartLine item : new ArrayList<>(cart)) {

   JPanel row = new JPanel(
           new BorderLayout(8, 0)
   );

   row.setBackground(
           new Color(249, 249, 251)
   );

   row.setBorder(
           new CompoundBorder(
                   new LineBorder(
                           new Color(225, 225, 230)
                   ),
                   new EmptyBorder(12, 8, 12, 8)
           )
   );


   // Produkt und Preis
   JLabel text = new JLabel(
           "<html><div style='width:190px'>" +
                   item.label +
                   "<br><b>" +
                   money(item.price) +
                   "</b></div></html>"
   );

   row.add(text, BorderLayout.CENTER);


   // Entfernen-Button
   JButton remove = new JButton("✕");

   remove.addActionListener(e -> {
    cart.remove(item);
    refreshCart();
   });

   row.add(remove, BorderLayout.EAST);

   row.setMaximumSize(
           new Dimension(Integer.MAX_VALUE, 92)
   );

   cartRows.add(row);
   cartRows.add(Box.createVerticalStrut(7));
  }


  // Gesamtpreis aktualisieren
  totalLabel.setText(money(total()));

  cartRows.revalidate();
  cartRows.repaint();
 }


 // ==================================================
 // 25. BESTELLUNG ABSCHLIESSEN
 // ==================================================

 private void checkout() {

  // Leeren Warenkorb verhindern
  if (cart.isEmpty()) {

   JOptionPane.showMessageDialog(
           this,
           "Bitte zuerst eine Pizza zum Warenkorb hinzufügen."
   );

   return;
  }


  try {

   // Bestelltexte sammeln
   List<String> labels = new ArrayList<>();

   for (CartLine item : cart) {
    labels.add(item.label);
   }


   // Bestellung speichern
   store.save(labels, total());


   // Erfolgsmeldung
   JOptionPane.showMessageDialog(
           this,
           "Bestellung gespeichert! Gesamt: " +
                   money(total())
   );


   // Warenkorb leeren
   cart.clear();
   refreshCart();

  } catch (Exception ex) {

   JOptionPane.showMessageDialog(
           this,
           "Die Bestellung konnte nicht gespeichert werden:\n" +
                   ex.getMessage(),
           "Dateifehler",
           JOptionPane.ERROR_MESSAGE
   );
  }
 }


 // ==================================================
 // 26. GESPEICHERTE BESTELLUNGEN ANZEIGEN
 // ==================================================

 private void showHistory() {

  try {

   // Dateiinhalt lesen
   String history = store.read();


   // Textbereich
   JTextArea area = new JTextArea(history);

   area.setEditable(false);

   area.setFont(
           new Font("Monospaced", Font.PLAIN, 13)
   );


   // Scrollbereich
   JScrollPane scroll = new JScrollPane(area);

   scroll.setPreferredSize(
           new Dimension(850, 460)
   );


   // Dialog anzeigen
   JOptionPane.showMessageDialog(
           this,
           scroll,
           "Gespeicherte Bestellungen",
           JOptionPane.PLAIN_MESSAGE
   );

  } catch (Exception ex) {

   JOptionPane.showMessageDialog(
           this,
           "Datei konnte nicht gelesen werden: " +
                   ex.getMessage(),
           "Fehler",
           JOptionPane.ERROR_MESSAGE
   );
  }
 }


 // ==================================================
 // 27. BILDER LADEN UND SKALIEREN
 // ==================================================

 private ImageIcon icon(
         String filename,
         int width,
         int height
 ) {

  try (
          var input = getClass().getResourceAsStream(
                  "/images/" + filename
          )
  ) {

   // Prüfen, ob das Bild existiert
   if (input == null) {

    throw new IllegalArgumentException(
            "Bild fehlt: " + filename
    );
   }


   // Bild einlesen
   BufferedImage image = ImageIO.read(input);

   if (image == null) {

    throw new IllegalArgumentException(
            "Ungültiges Bild: " + filename
    );
   }


   // Bild skalieren
   Image scaledImage = image.getScaledInstance(
           width,
           height,
           Image.SCALE_SMOOTH
   );

   return new ImageIcon(scaledImage);

  } catch (Exception ex) {

   System.err.println(
           "Bildfehler: " + ex.getMessage()
   );


   // Ersatzbild erstellen
   BufferedImage placeholder = new BufferedImage(
           width,
           height,
           BufferedImage.TYPE_INT_RGB
   );

   Graphics2D graphics = placeholder.createGraphics();

   graphics.setColor(new Color(236, 225, 225));
   graphics.fillRect(0, 0, width, height);

   graphics.setColor(RED);

   graphics.drawString(
           "Bild nicht verfügbar",
           10,
           height / 2
   );

   graphics.dispose();

   return new ImageIcon(placeholder);
  }
 }
}
