# Token auth - template

##1. Wstęp

Projekt jest szkieletem aplikacji o autentkacji opartej na tokenach, użytkownikach i rolach.

##2. Przygotowanie

Wykonaj następujące kroki

*  Wykonaj: git clone https://github.com/tomasz0zwierzynski/tokenauth-template.git

* Utwórz bazę danych

* Wykonaj skrypt  *src/main/resources/db/init.sql* na bazie

* W pliku *application.properties* ustaw namiary na bazę

* W pliku *security.properties* ustaw role dostępne w aplikacji oraz endpointy autoryzacji

* *ManageController* oraz *ResourceController* to przykładowe kontrolery dostepne dla różnych ról