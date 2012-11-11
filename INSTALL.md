# Installation

## Bauen der Anwendung

Die Anwendung benutzt das Build-Werkzeug *Maven3*. Zu Bauen der Anwendung muss dieses auf dem Computer installiert sein. Es kann hier heruntergeladen werden:
`http://maven.apache.org/download.html`

Nun wechselt man in das Verzeichnis der Applikation (dort wo sich die Datei pom.xml befindet) und führ auf der Kommandozeile den Befehl `mvn clean install` aus.
Maven beginnt nun mit dem bauen der Anwendung. Das umfasst das herunterladen der 
nötigen Frameworks, das kompilieren der Klassen sowie das Ausführen der Tests.
Dies kann beim ersten Ausführen teilweise ein paar Minuten dauern, da erst alle
Frameworks heruntergeladen werden müssen.

Wenn alles erfolgreich gebaut werden konnte, erscheint die Meldung "BUILD SUCCESSFUL".

Die WAR-Datei der Applikation befindet sich nun im Verzeichnis 'target' und 
hat den Dateinamen 'tmpmail-0.0.1-SNAPSHOT.war'.

## Installieren von Tomcat und Starten der Anwendung

Die Anwendung setzt einen installierten Tomcat 7 Server voraus. 
Dieser kann hier heruntergeladen werden: 
http://mirror.softaculous.com/apache/tomcat/tomcat-7/v7.0.32/bin/apache-tomcat-7.0.32.zip

1. Das heruntergeladene Tomcat-Archiv entpacken
2. Das war-Archiv der Anwendung in das verzeichnis "webapps" von tomcat kopieren
3. Tomcat mit /bin/startup.sh bwz. /bin/startup.bat starten
4. Nachdem Tomcat hochgefahren ist sollte die Anwendung unter der URL:
http://localhost:8080/tmpmail-0.0.1-SNAPSHOT/ erreichbar sein. 


