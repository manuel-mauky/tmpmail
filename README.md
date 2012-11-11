# TmpMail

## Einführung

Das Programm "TmpMail" wurde für den Programmierwettbewerb "http://www.coding-contest.de/", Kategorie Java 2 entwickelt.

Der Nutzer der Webanwendung kann sich mit seiner privaten Email-Adresse
registrieren und anschließend beliebig viele Temporäre Email-Adressen erstellen,
die er z.B. für die Registrierung in Online-Foren, Shops oder ähnliches
verwenden kann. Damit muss er nicht seine private Email-Adresse im Internet bekannt
machen. 
Wird eine Email an die Temporäre Email-Adresse gesendet, so wird diese an die private Email-Adresse des Nutzers weitergeleitet.

Die Anwendung befindet sich in einem Alpha-Stadium und sollte nicht produktiv 
genutzt werden!

In der aktuellen Konfiguration wird eine In-Memory-Datenbank eingesetzt. Das bedeutet, dass alle gespeicherten Daten nach dem Herunterfahren der Anwendung verloren gehen. 

## Konfiguration

Neue Domains, die die Anwendung verwalten soll, lassen sich im Admin-Backend hinzufügen. Dieses ist erreichbar unter http://localhost:8080/tmpmail-0.0.1-SNAPSHOT/adminbackend. 
Ausserdem kann im Admin-Backend die Konfiguration des Ausgehenden SMTP-Servers 
vorgenommen werden. 

Leider werden in der aktuellen Alpha-Version die Einstellungen noch nicht dauerhaft gespeichert. Nach einem Neustarten der Anwendung müssen die Daten leider wieder neu eingegeben werden.

## Architektur

Die Applikation bringt einen eigenen SMTP-Server mit, der auf dem Port 25000 lauscht. Leider ist der Port aktuell nicht dynamisch konfigurierbar. Es ist die Aufgabe des Administrators, das Netzwerk so zu konfigurieren, dass eingehende Emails, die weitergeleitet werden sollen, bei der Anwendung auf diesem Port ankommen. 

Beim SMTP-Server ankommende Emails werden mit den in der Datenbank abgespeicherten Domains abgeglichen. 
Anschließend wird geschaut, ob ein Nutzer den local-part der Email-Adresse reserviert hat. In diesem Fall wird die Email an die hinterlegte private Email-Adresse des Nutzers weitergeleitet.
Zur Weiterleitung wird ein externer SMTP-Server benötigt. Dies hat zwei Gründe:
Zum ist die Konfiguration dieses externen SMTP-Servers damit unabhängig von dieser Anwendung. Vor allem aber kann damit das Problem umgangen werden,
dass privat betriebene SMTP-Server in der Regel auf Spam-Sperrlisten stehen und somit weitergeleitete Emails entweder garnicht ankommen oder im Spam-Ordner landen. Mit der Verwendung eines etablierten SMTP-Servers kann dieses Problem umgangen werden. Es könnte hier also z.B. der GMail-SMTP-Server verwendet werden.

Die Passwörter der Nutzer werden selbstverständlich nicht im Klartext abgespeichert sondern als Hash. Um die Generierung des Hashs sicher zu gestalten wurde zusätzlich ein `Salt` benutzt sowie `Key stretching` implementiert. Die Hashes sind mit SHA512 berechnet.

## Lokales Testen der Anwendung

Wenn die Anwendung auf dem Lokalen Rechner ausgeführt wird, kann auch ohne Konfiguration eines DNS-Servers getestet werden.
Dafür muss im Email-Programm der SMTP-Server der Anwendung eingetragen werden. 

Als "Server" muss dann "localhost" gewählt werden und als "Port" 25000. 
Nun kann man direkt Emails an die Anwendung schicken und die Weiterleitung ausprobieren.
 
## Frameworks

#### Backend

Das Backend baut auf JavaEE-Technologien auf. Die Persistence wurde mit `JPA2/Eclipselink` umgesetzt. Als Datenbank kommt aktuell eine `HSQLDB`-Inmemory-Datenbank zum Einsatz. In der `persistence.xml` können aber auch andere Datenbanken konfiguriert werden.

Als Dependency-Injection-Framework wurde `CDI/Weld` benutzt. Dieses wurde um die Library `Seam Solder` erweitert, damit auf Events des Servlet-Containers reagiert werden kann. Dies war notwendig um den SMTP-Server beim Start der Anwendung ebenfalls zu starten.

Als SMTP-Server wird `SubethaSMTP` benutzt. 

#### Frontend

Im Frontend kommt `JSF2.1 / myFaces` zum Einsatz. Dieses wurde um einige Libraries erweitert:
`PrettyFaces` dient dazu, die URLs der Anwendung "hübsch" gestalten zu können und verhindert lange, Parameter-gefüllte URLs, die sonst bei JSF üblich sind.
`PrimeFaces` wurde für einige Oberflächen-Widgets benutzt.


#### Testing Frameworks

Um vernünftige Unit- und Integrations-Tests schreiben zu können wurden einige Testing-Libraries benutzt:
`JUnit` zum Ausführen der Tests. `FEST-Assert` wurde als Assertation-Library verwendet um die Lesbarkeit der Tests zu erhöhen. Als Mocking-Framework wurde `Mockito` benutzt. Um die Korrektheit der Equals/HashCode-Methoden zu verifizieren wurde `EqualsVerifier` eingesetzt. 
Und zu guter letzt wurde zum Testen der CDI-Funktionalitäten wie DependencyInjection, Producer-Methoden usw. `Weld-SE` in den Tests eingesetzt.


#### Sonstige

Das Logging wird mit `SLF4J / Logback` realisiert.

`Apache Commons Codec` und `Apache Commons Lang` kamen bei der Implementierung
des Hash-Generators zum sicheren Abspeichern der Passwörter zum Einsatz.

Als Validierungs-Framework kommt `BeanValidation / Hibernate-Validator` zum Einsatz. 


## Status der Implementierung

Da für die Implementierung nur eine Woche Zeit war und ich innerhalb dieser Woche 
nur wenig Zeit investieren konnte, musste ich die Implementierung im wesentlichen an einem Wochenende durchführen. Daher konnte ich leider nicht alle Anforderungen des Wettbewerbs erfüllen.

### Anforderungen, die nicht mehr Implementiert werden konnten:

+ Der Nutzeraccount besteht nur aus Email-Adresse und Passwort, Vor- und Nachname fehlen
+ Account löschen, Daten anpassen, Passwort ändern und wiederherstellen fehlen leider
+ Es wird kein Vorschlag für eine neue Email-Adresse unterbreitet
+ Die Gültigkeitsdauer der Temporären Email-Adressen fehlen leider
+ Man kann nur Temporäre Email-Adressen anlegen, nicht löschen oder bearbeiten
+ HTTPS funktionert noch nicht

### Nötige Verbesserungen

Einige Dinge sind noch Verbesserungswürdig:

+ Konfigurations-Parameter für den Ausgehenden SMTP-Server werden nicht gespeichert
sondern sind nach einem Neustard des Servers verloren
+ Die Datenbank lässt sich noch nicht einfach konfigurieren. Stattdessen muss die
`persistence.xml` per Hand angepasst werden.
+ Das Admin-Backend ist nicht abgesichert. Ein gesonderter Admin-Account wäre hier notwendig.
+ Der Eingehende SMTP-Server muss besser konfigurierbar sein. z.B. sollte auch 
Verschlüsselung einstellbar sein. 
+ Die Verschlüsselung des Ausgehenden SMTP-Servers sollte besser konfigurierbar sein, z.B. SSL statt STARTTSL.

