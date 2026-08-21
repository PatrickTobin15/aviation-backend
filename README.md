# Airport Arrivals & Departures — Semester 4 Final Sprint

A full stack app for tracking arrivals and departures across multiple airports, built for my Semester 4 Final Sprint project. Backend is Spring Boot + MySQL, frontend is React to which users can easily switch between airports to see that airport's arrival/departure board, and there's an admin section to add airports, airlines, gates, and flights.

I worked on this solo, so I'm the sole contributor on both the backend and frontend  architecture, entity design, API, UI, testing, Docker setup, and deployment were all done by me.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3.3, Spring Data JPA, Spring Security (Basic Auth), MySQL
- **Frontend:** React 18 (Vite)
- **Database:** MySQL (local via Docker, or AWS RDS for deployment)
- **Testing:** JUnit 5 + Mockito (service layer), Spring MockMvc (controller layer)
- **Containerization:** Docker + docker-compose

## Entities & Relationships

- **Airport** — code, name, city, country
- **Airline** — name, IATA code
- **Gate** — gate number, belongs to one Airport
- **Flight** — flight number, belongs to one Airline, has an origin Airport, a destination Airport, an optional Gate, a scheduled/actual time, and a status

Relationships: an Airport has many many Gates and many Flights as the origin or the destination); an Airline has many Flights; a Gate has many Flights.

## User Stories

- As a traveler I would really like to select a airport so that I can see its arrivals and their departures.
- As a traveler I want to be able to see which gate a flight is assigned to so that I know where to go.
- As a traveler I really want to be able to see a flight's status (scheduled, boarding, delayed, cancelled, etc.) so I know if my flight is on time or not.
- As an admin I want to have the ability add a new airport so that travelers can look up flights there.
- As an admin I would like to be able to add a new airline so that I can assign it to flights.
- As an admin I want to add a new gate at any specific airport.
- As an admin I would like to to add a new flight, assigning it an airline, origin airport, destination airport, gate, and scheduled time.
- As an admin/developer I want the API protected behind a username and password so it isn't publicly writable.

## Manual Testing Scenarios (Frontend)

1. Load the app the departures board loads for the first airport in the list.
2. Switch airports using the dropdown — the board updates to show that airport's flights.
3. Switch between the Departures and Arrivals tabs — the correct flight set displays.
4. Go to Admin -> Airports, add a new airport — confirm it appears in the airport dropdown.
5. Go to Admin -> Airlines, add a new airline — confirm it appears as an option when creating a flight.
6. Go to Admin -> Gates, add a gate tied to an airport — confirm it can be selected as a flight's gate.
7. Go to Admin -> Flights, add a flight with a future scheduled time — confirm it shows up on the correct airport's departures board.
8. Submit a form with a required field blank — confirm the browser blocks submission (HTML5 required validation).

## Project Structure

```
aviation-app/
├── backend/          # Spring Boot API
│   ├── src/main/java/com/gracie/aviation/
│   │   ├── model/         # Airport, Airline, Gate, Flight, FlightStatus
│   │   ├── repository/    # Spring Data JPA repositories
│   │   ├── service/       # Business logic
│   │   ├── controller/    # REST endpoints
│   │   └── config/        # Security, CORS, exception handling
│   └── src/test/java/...  # JUnit + Mockito + MockMvc tests
├── frontend/          # React (Vite) app
│   └── src/
│       ├── api.js               # API client with basic auth
│       ├── App.jsx              # Main app shell
│       └── components/          # AirportSelector, FlightTable, AdminPanel
└── docker-compose.yml # MySQL + backend + frontend
```

## API Endpoints

All endpoints require HTTP Basic Auth (default: `admin` / `aviation123`, set in `application.properties`).

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/airports` | List all airports |
| POST | `/api/airports` | Create an airport |
| PUT | `/api/airports/{id}` | Update an airport |
| DELETE | `/api/airports/{id}` | Delete an airport |
| GET | `/api/airlines` | List all airlines |
| POST | `/api/airlines` | Create an airline |
| GET | `/api/gates/airport/{airportId}` | List gates for an airport |
| POST | `/api/gates` | Create a gate |
| GET | `/api/flights/departures/{airportId}` | Departures for an airport |
| GET | `/api/flights/arrivals/{airportId}` | Arrivals for an airport |
| POST | `/api/flights` | Create a flight |
| PUT | `/api/flights/{id}` | Update a flight |
| DELETE | `/api/flights/{id}` | Delete a flight |

## Running Locally (without Docker)

**Backend:**
1. Create a MySQL database, or point `application.properties` at your own instance.
2. `cd backend`
3. `mvn spring-boot:run`
4. API runs on `http://localhost:8080`

**Frontend:**
1. `cd frontend`
2. `npm install`
3. `npm run dev`
4. App runs on `http://localhost:5173`

## Running with Docker

**Local testing (no AWS needed)** — spins up MySQL, backend, and frontend together:

```bash
docker-compose -f docker-compose.local.yml up --build
```

**AWS deployment** — uses your RDS instance instead of a local MySQL container:

```bash
cp .env.example .env
# edit .env with your real RDS endpoint, credentials, and deployed URLs
docker-compose up --build
```

## Running Tests

```bash
cd backend
mvn test
```

Tests use an in-memory H2 database, so MySQL doesn't need to be running.

## Deployment (AWS)

1. **RDS:** Create a MySQL instance in the AWS Academy Learner Lab (DB name `aviation_db`), note the endpoint.
2. **Security groups:** Allowing the port of 3306 into RDS from your EC2 instance, and port 8080 into the EC2 from anywhere the frontend needs to reach it.
3. **EC2:** Install Docker + docker compose or copy this repo and run it there directly since Docker's already set up.
4. **Configure:** `cp .env.example .env` and fill in your real RDS endpoint/credentials, plus the URLs your app will actually be served from once deployed (`VITE_API_BASE`, `FRONTEND_URL`).
5. **Deploy:** `docker-compose up --build` on the EC2 instance this builds the backend against RDS and the frontend against your live backend URL.
6. **Verify:** open the frontend's public URL and confirm the arrivals/departures boards load and the admin panel can create data — this is what goes in the demo video.

## Reflection

*(Fill this in with your own notes for submission. since I worked solo, this covers my full role on the project.)*

- **Role:** Sole developer — backend, frontend, database design, testing, Docker, and deployment.
- **What went well:** I got the full stack running end to end on AWS and talking to a real RDS database.
- **Challenges:** Trickiest part was a bug I hit while testing the admin panel when I was trying to create a gate it kept failing with a null pointer exception so I had to isolate it by testing the API directly with a curl instead of going through the UI which then told me it was the backend itself that was the problem and not the front end.
It turned out to be a @JsonIgnore annotation on one of my entity fields that was silently blocking any incoming data from being read so once I had discovered that I removed it and had to rebuild the container to which I then confirmed the fixc worked both through curl and the acual UI
- **What I'd do differently on a team:** If I were doing this with a team I would have split up the entity design and the admin panmel forms so we could work in parallel instead of building the whole stack sequentially.
