# High-Level Design: Weather Reporting System

*Author: Atul Kumar*
*Backend Software Developer @ FPL Technologies | Driving Scalable FinTech Innovation & Seamless Customer Onboarding with Java, Microservices & AWS.*
*Date: September 6, 2025*

---

## 1. System Requirements

### Functional Requirements:
- Show weather for a specific location.
- Handle real-time weather updates.
- Support for multi-location and regional weather display.

### Non-Functional Requirements:
- **Availability:** The system must be highly available.
- **Scalability:** The system must be able to scale to handle load.
- **Usability:** The system should be easy to use.
- **Quick Response:** The system must provide fast response times.

---

## 2. Capacity Estimation

- **Number of Users:** 100,000 during peak hours.
- **API Request Rate:** 5 requests per session.
- **Data Size:** 5KB per response.

### Estimation Snapshot:
- **Peak Hour Requests:** 100,000 users * 5 requests/session = 500,000 requests during peak.
- **Data Transfer during Peak:** 500,000 requests * 5KB = 2.5GB.
- **Database Operations:** 
    - 400,000 read operations.
    - 100,000 write operations.
- **Yearly Storage Requirement:** ~365GB.

---

## 3. High-Level Design

In this high-level design, we will create a **Weather Service** that orchestrates the data flow. The Weather Service will call a **Third-Party Service**, which in turn fetches data from a **Vendor**. The Weather Service will then cache the data in **Redis** for quick access and persist it in a **Database** for historical records. Redis will use latitude and longitude for keys to serve recent weather pulls quickly.

For real-time updates, the Weather Service will publish events to **Kafka** based on vendor webhooks. A **Notification Service** will consume these events and send alerts about bad weather to users.

---

## 4. Request Flow

1.  **Client Request:** The client initiates a request with latitude and longitude. The request is routed through an **API Gateway** to the **Weather Service**.
2.  **Orchestration:** The Weather Service orchestrates the flow by calling a **Third-Party Service** to fetch weather data from a vendor.
3.  **Data Storage & Response:** The Weather Service stores the fetched data in both a **NoSQL database** for historical records and **Redis** for fast, recent data retrieval before responding to the client.

---

## 5. Detailed Component Design

### a. Weather-Service

This service's main responsibility is to orchestrate weather reporting functionalities.

- **Caching:** The response from the API is stored in Redis with a Time-to-Live (TTL) to reduce vendor API calls. This also supports cache invalidation strategies.
- **Real-time Data Publishing:** It is responsible for publishing events to Kafka for real-time processing and notifications.
- **Scalability via Kubernetes:** The Weather-Service is containerized and uses Kubernetes for auto-scaling, handling varying loads efficiently.
- **Outage Handling:** During vendor outages, if data is available in Redis, it returns the cached data. If not, it can queue requests for later processing.
- **Workflow:**
    1.  **Cache Check:** When a request for weather data is received, the service first checks the Redis cache.
    2.  **Vendor API Call:** If data is not found in the cache, the service calls the third-party vendor API.

### b. Notification-Service

- **Responsibilities and Workflow:**
    - **Subscribe to Kafka:** Listens to Kafka topics published by the Weather-Service for weather updates.
    - **Message Processing:** Processes messages to generate real-time notifications.
    - **Notification Dispatch:** Utilizes third-party services like Firebase, SMS gateways, and email services to send alerts to users.
    - **Scalability:** Can leverage Kubernetes or other container orchestration frameworks to scale based on message load.

### c. Third-Party Service

- **Responsibilities and Workflow:**
    - **Vendor Integration:** Handles API integrations, making the system vendor-agnostic.
    - **Factory Design Pattern:** Utilizes this pattern to dynamically select the appropriate vendor implementation at runtime.
    - **Data Transformation:** Converts vendor-specific data into a standardized format used internally.
    - **Circuit Breaker Implementation:** Ensures system reliability by preventing cascading failures from vendor outages.

---

## 6. API Design

### Fetch Weather API

- **Endpoint:** `GET /api/weather/fetch`
- **Query Parameters:**
    - `lat` (latitude)
    - `long` (longitude)
- **Example Response:**

```json
{
    "coord": { "lon": 7.367, "lat": 45.133 },
    "weather": [ { "id": 501, "main": "Rain", "description": "moderate rain", "icon": "10d" } ],
    "base": "stations",
    "main": {
        "temp": 284.2,
        "feels_like": 282.93,
        "temp_min": 283.06,
        "temp_max": 286.82,
        "pressure": 1021,
        "humidity": 60,
        "sea_level": 1021,
        "grnd_level": 910
    },
    "visibility": 10000,
    "wind": { "speed": 4.09, "deg": 121, "gust": 3.47 },
    "rain": { "1h": 2.73 },
    "clouds": { "all": 83 },
    "dt": 1726660758,
    "sys": { "type": 1, "id": 6736, "country": "IT", "sunrise": 1726636384, "sunset": 1726680975 },
    "timezone": 7200,
    "id": 3165523,
    "name": "Province of Turin",
    "cod": 200
}
```

---

## 7. Database Design

A NoSQL database will be used to store vendor API or webhook responses. The data will be stored with latitude, longitude, and a timestamp for historical retrieval.

### Data Model Example (MongoDB):

```json
{
    "_id": "5553a998e4b02cf7151190c9",
    "location": {
        "city": "New York City",
        "state": "NY",
        "country": "USA",
        "latitude": 40.7128,
        "longitude": -74.0060
    },
    "current_weather": {
        "timestamp": "2025-09-06T11:00:00Z",
        "temperature": 20.5,
        "humidity": 65,
        "wind_speed": 15,
        "wind_direction": "NW",
        "condition": "Partly Cloudy",
        "icon_code": "02d"
    },
    "daily_forecasts": [
        {
            "date": "2025-09-07",
            "temp_max": 22,
            "temp_min": 15,
            "condition": "Rainy",
            "icon_code": "09d"
        },
        {
            "date": "2025-09-08",
            "temp_max": 25,
            "temp_min": 17,
            "condition": "Sunny",
            "icon_code": "01d"
        }
    ],
    "hourly_forecasts": [
        {
            "timestamp": "2025-09-06T12:00:00Z",
            "temperature": 21.0,
            "condition": "Cloudy",
            "icon_code": "03d"
        },
        {
            "timestamp": "2025-09-06T13:00:00Z",
            "temperature": 21.5,
            "condition": "Cloudy",
            "icon_code": "03d"
        }
    ],
    "historical_data": [
        {
            "timestamp": "2025-09-05T11:00:00Z",
            "temperature": 18.2,
            "humidity": 60,
            "condition": "Cloudy"
        }
    ]
}
