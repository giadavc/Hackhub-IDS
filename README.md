# Hackhub-IDS

## Descrizione

Hackhub-IDS è un'applicazione Spring Boot che gestisce hackathon, team, submissions, valutazioni, mentoring e segnalazioni di violazioni. Sistema completo per organizzare e monitorare competizioni di programmazione.

## Stack Tecnologico

- **Framework**: Spring Boot 4.0.3
- **Language**: Java 21
- **Database**: H2 (in-memory)
- **ORM**: Hibernate / JPA
- **Build**: Maven
- **Utilities**: Lombok, Jakarta Validation


## Struttura del Progetto

```
src/main/java/org/example/hackhubids/
├── Controller/       # Gestione delle richieste HTTP
├── Service/          # Logica di business
├── Repository/       # Accesso ai dati (JPA)
├── Domain/           # Entità JPA e Enum
```


## Design Pattern Utilizzati

### 1. **Adapter Pattern (Hexagonal Architecture)**
Interfaccia per integrare un servizio esterno senza legare la logica di business a un’implementazione concreta.

- **CalendarPort** → astrazione del servizio calendar
- **CalendarClient** → adapter concreto che simula il calendar in-memory

Uso nel progetto:
- [MentoringService](src/main/java/org/example/hackhubids/Service/MentoringService.java) usa `CalendarPort`
- [CalendarClient](src/main/java/org/example/hackhubids/Service/CalendarClient.java) implementa l’adapter

Vantaggi:
- disaccoppia il dominio dal servizio esterno
- rende il codice più testabile
- permette di sostituire facilmente il calendar simulato con uno reale


### 2. **Factory Pattern**
Centralizzazione della creazione di oggetti con una logica di costruzione specifica.

- **StaffMemberFactory** → crea gli oggetti StaffMember in base al ruolo

Uso nel progetto:
- [StaffService](src/main/java/org/example/hackhubids/Service/StaffService.java) delega la creazione a `StaffMemberFactory`

Vantaggi:
- evita duplicazione di logica di creazione
- rende più pulita la gestione dei ruoli
- facilita future estensioni nella costruzione dello staff

### 3. **Builder Pattern**
Usato sulle entità del dominio per costruire oggetti in modo leggibile e fluente.

Esempio:
- [Hackathon](src/main/java/org/example/hackhubids/Domain/Hackathon.java)
- [Payment](src/main/java/org/example/hackhubids/Domain/Payment.java)
- [CallProposal](src/main/java/org/example/hackhubids/Domain/CallProposal.java)

È implementato tramite Lombok con annotazione Builder.




## API REST Endpoints

| Metodo | Endpoint | Descrizione |
|--------|----------|-------------|
| **POST** | `/api/users/register` | Registrazione utente |
| **POST** | `/api/users/login` | Login |
| **POST** | `/api/staff/assign-role` | Assegna ruolo (ORGANIZER, JUDGE, MENTOR) |
| **POST** | `/api/teams` | Crea team |
| **POST** | `/api/teams/{teamId}/invite` | Invita utente al team |
| **POST** | `/api/teams/invitations/{id}/accept` | Accetta invito |
| **POST** | `/api/teams/invitations/{id}/reject` | Rifiuta invito |
| **GET** | `/api/hackathons` | Lista hackathon pubblici |
| **POST** | `/api/hackathons` | Crea hackathon (ORGANIZER) |
| **POST** | `/api/hackathons/{id}/register-team` | Iscrivi team |
| **POST** | `/api/hackathons/{id}/assign-mentor` | Assegna mentor |
| **POST** | `/api/submissions` | Invia submission |
| **POST** | `/api/evaluations` | Valuta submission (JUDGE) |
| **POST** | `/api/mentoring/support-requests` | Richiedi supporto |
| **POST** | `/api/mentoring/support-requests/{id}/propose-call` | Proponi call |
| **POST** | `/api/violations` | Segnala violazione |
| **POST** | `/api/violations/{id}/resolve` | Risolvi violazione |

## Avvio dell'Applicazione

```bash
mvn spring-boot:run
```

L'app parte su `http://localhost:8080`

### H2 Console
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (blank)

## Collection Postman

File importabile: `Hackhub-IDS.postman_collection.json`

Importa su Postman per testare tutti gli endpoint con variabili pre-configurate.

## Note Architetturali

- **Validation**: Utilizzo di `jakarta.validation.@Valid` sui DTO
- **Error Handling**: Eccezioni custom (`IllegalStateException`, `IllegalArgumentException`)
- **Mock Esterni**: Calendar e Payment provider implementati come mock in-memory per testing
- **Transazionalità**: Operazioni critiche protette da `@Transactional`
