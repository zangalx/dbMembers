# dbMembers
dbMembers ist ein Programm um Datenbloecke aus mehreren Java-Arrays 
* auszulesen, 
* hinzuzufuegen, 
* zu bearbeiten oder 
* zu loeschen,

und soll eine reele `Blockspeicherung` immitieren.

# Ausfuehrung
Zur Ausfuehrung kommen zwei Moeglichkeiten in Betracht. Zunaechst die Variante eines unsortierten Arrays, indem die Main Methode der Java-Klasse "StartMitgliederDB" ausgefuehrt wird, 
oder die sortierte Variante, indem das Programm ueber die Java-Klasse "StartMitgliederDBOrdererd" gestartet wird.

# StartMitgliederDB
Die Testfaelle unter `StartMitgliederDB` lauten wie folgt:
* Lesen eines Eintrages anhand seiner Position: Die Datenbank sucht nach dem Eintrag mit der Position und gibt diesen aus - Gesonderte Ausgabe falls Position nicht vorhanden ist
* Lesen eines Eintrages anhand der Mitgliedsnummer: Die Datenbank sucht nach dem Eintrag mit der Mitgliedsnummer und gibt diesen aus - Gesonderte Ausgabe falls Mitglied nicht gefunden wird
* Einfuegen eines Eintrages: Das Programm durchlaeuft die Datenbank und fügt den Eintrag an erstmoeglicher Position ein (genuegend Stellen sind vorhanden) - Alternativ am Ende der Datenbank
* Bearbeiten eines Eintrages: Das Programm sucht nach dem zu bearbeitenden Eintrag (= ID) und wechselt den Eintrag an Ort und Stelle (selber Position) aus, sofern die Attribute keine Mehrlänge aufweisen und/ oder der noetige Platz besteht. Ist der neu bearbeitete Eintrag laenger als dessen altes Gegenstueck, wird der alte Eintrag verworfen und ein Neuer in der Datenbank an der bisherigen Stelle eingefuegt. Das Programm raeumt dabei Bloecke automatisch auf und drueckt Eintraege selbststaendig in den naechsten Block, sofern der Platz nicht ausreicht.
* Loeschen eines Eintrages: Die Anwendung findet den Eintrag anhand der uebergebenen ID und loescht diesen. 

# StartMitgliederDBOrdered
Die Testfaelle unter `StartMitgliederDBOrdered` sind analog deren in `StartMitgliederDB`. Auf die binaere Suche wurde gemaess Aufgabenstellung verzichtet. Ebenfalls werden keine neuen Eintraege hinzuegfuegt.
* Lesen eines Eintrages anhand seiner Position
* Lesen eines Eintrages anhand der Mitgliedsnummer
* Bearbeiten eines Eintrages
* Loeschen eines Eintrages

# Besonderheiten
Bei der Ausfuehrung wurden Thread-Sleep Pausen eingefuegt, um eine uebersichtliche Ausgabe zu gewaehrleisten
