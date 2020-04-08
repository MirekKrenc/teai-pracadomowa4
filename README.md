# teai-pracadomowa3
Kurs Spring Boot 2 - praca domowa - tydzien 3

Praca domowa po przerobieniu materiału z tygodnia 3. kursu:
"Kurs Spring Boot 2 - Efektywne Aplikacje Internetowe."

Autorem kursu jest Przemek Bykowki

Praca polegała na napisaniu RESTowej aplikacji MVC.

JsonPatch
https://tools.ietf.org/html/rfc6902
Dodanie obsługi PATCH przy zastosowaniu JsonPatch wymaga przy testach ustawienia w nagłówku
Context-Type: application/json-patch+json

Sam request ma postać np.
[{"op":"replace","path":"/model","value":"Sienaa" }]
Jest to tabela z operacją, ścieżką i w przypadku replace z wartością na któą ma być pole zmienione

