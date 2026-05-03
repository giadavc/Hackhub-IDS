# Hackhub-IDS

## Descrizione

**Hackhub-IDS** è una piattaforma web di gestione per hackathon e competizioni di programmazione, sviluppata in **Spring Boot** con Java 21. 

Fornisce un ecosistema completo per:
- **Organizzatori**: creare hackathon, definire cronologie, gestire iscrizioni di team e assegnare mentor
- **Partecipanti**: formare team, inviare submission, ricevere mentoring e supporto tecnico
- **Giudici**: valutare le submission secondo criteri definiti
- **Mentori**: invia call di supporto e segnalazioni.

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

Questa sezione descrive i pattern applicativi principali usati nel progetto: Adapter e Builder.

### Adapter 
- Descrizione: separa il dominio dalle integrazioni esterne tramite una `port` (interfaccia) e un `adapter` (implementazione concreta).
- Esempio nel progetto: `CalendarPort` (port) e `CalendarClient` (adapter) per la gestione delle call di mentoring.
- Dove guardare: [Service/MentoringService](src/main/java/org/example/hackhubids/Service/MentoringService.java) usa la `CalendarPort`.
- Vantaggi: riduce il coupling, migliora la testabilità e permette di sostituire facilmente mock con provider reali.

### Builder
- Descrizione: consente la costruzione fluente di oggetti complessi, utile quando sono presenti molti campi opzionali.
- Esempi nel progetto: `TeamInvitationBuilder` e molte entità annotate con Lombok `@Builder` (es. `Hackathon`, `Payment`).
- Dove guardare: [Domain/TeamInvitationBuilder.java](src/main/java/org/example/hackhubids/Domain/TeamInvitationBuilder.java) e altre classi in `Domain/`.
- Vantaggi: migliora la leggibilità del codice di inizializzazione e riduce errori dovuti a costruttori con molti parametri.


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


## Collection Postman

File importabile: `Hackhub-IDS.postman_collection.json`

Importa su Postman per testare tutti gli endpoint con variabili pre-configurate.

## Note Architetturali

- **Validation**: Utilizzo di `jakarta.validation.@Valid` sui DTO
- **Error Handling**: Eccezioni custom (`IllegalStateException`, `IllegalArgumentException`)
- **Mock Esterni**: Calendar e Payment provider implementati come mock in-memory per testing
- **Transazionalità**: Operazioni critiche protette da `@Transactional`
