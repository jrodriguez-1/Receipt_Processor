<h1>Receipt Processor</h1>

<h2>Project Overview</h2>
*** 
<p>This Spring Boot application processes receipts by calculating reward points based on predefined rules. 
It generates a receipt ID and returns the total points earned. A complete Docker setup is included, with 
instructions provided below.</p>

<h3>Requirements:</h3>
- Java 17 or higher
- Docker and Docker Compose 

<h3>Docker:</h3>
All Docker configurations are located in the following 3 files
- ReceiptProcessor(Root): Dockerfile, docker-compose.yml
- src/main/resources: application-docker.properties

<h2>Application Setup</h2>
***
<h3>To Run the Application:</h3>
<p>1. Clone the Repository</p>
<p>2. Navigate to project directory</p>
<p>3. Build and run with Docker command: docker-compose up / To stop: docker-compose down</p>
<p>4. Application should be available on port http://localhost:8080</p>

<h2>API Endpoints</h2>
*** 
<h3>Process Receipt</h3> 
- **URL:** "/receipts/process":
- **Method:** POST
- **Description:** Submits a receipt for processing.
- **Example Request Body:**
```json
{
 "retailer": "Target",
 "purchaseDate": "2022-01-01",
 "purchaseTime": "13:01",
 "items": [
   {
     "shortDescription": "Mountain Dew 12PK",
     "price": "6.49"
   },
   {
     "shortDescription": "Emils Cheese Pizza",
     "price": "12.25"
   }
 ],
 "total": "18.74"
}
```
**Response Example:**
```json
{
  "id": "7fb1377b-b223-49d9-a31a-5a02701dd310"
}
```
<h3>Get Points</h3>
- **URL:** "/receipts/{id}/points":
- **Method:** GET
- **Description:** Returns points awarded for receipt.

**Response Example:**
```json
{
  "points": 28
}
```
<h3>Rules:</h3>
- One point for every alphanumeric character in the retailer name.
- 50 points if the total is a round dollar amount with no cents.
- 25 points if the total is a multiple of 0.25.
- 5 points for every two items on the receipt.
- If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned.
- 6 points if the day in the purchase date is odd.
- 10 points if the time of purchase is after 2:00pm and before 4:00pm.
