# Instructions
## Project Setup
Before proceeding with following steps, please ensure that your java version is equal to or greater than 21.
You can verify it with ```java --version```. To set up a project, you must perform the steps below.


#### Warning !!!
Project setup commands were tested on Windows 11 cmd. Other command line interfaces like  powershell, bash etc. may have a slightly different syntax.

### Running project
### 


1. Clone the repository 
```
git clone https://github.com/BartlomiejSkwara/fundraising
```
2. Enter repos directory
```
cd fundraising
```
3. Run maven project using maven wrapper
```
mvnw clean spring-boot:run
```

### Optional (building jar):
If you want to create a jar file for later deployment first follow steps 1 and 2  (cloning repo and entering directory).

Then build the jar using this command (it may take slightly longer due to packaging and tests being run)
```
mvnw clean package
```
Now jar file can be run using following command
```
java -jar target\fundraising-0.1.0.jar
```

### Issues with used port
By default, application runs  on port 8080 so if the running process fails make sure that it is not in use. 
If You want to it is possible to change the used port in following ways:

For spring:boot run:
```
mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

For jar file:
```
java -jar target/fundraising-0.1.0.jar --server.port=8081
```
## Currencies
Usable currencies are limited to following subset: PLN, EUR, USD.

During startup app will try to fetch current exchange rates from free open-source Frankfurter api.

https://frankfurter.dev/

If the app encounters issues during fetching it will use default hardcoded exchange rate values. 
```
PLN (1.0)
EUR (0.23474)
USD (0.26277)
```
After fetching or failing to fetch app will log the exchange rates, you can see the example of log after successful fetch below.
```
2025-05-20T07:59:19.533+02:00  INFO 2420 --- [fundraising] [           main] o.e.f.common.ExchangeRateService         : Successfully fetched exchange rates from frankfurter api {EUR=0.23426, PLN=1.00, USD=0.26382}
```



## REST API Endpoints
App consists of 8 endpoints that allow manipulation of fundraising events data.

Endpoints are relative to /api under the current host. It means that by default they should start with http://localhost:8080/api, unless the user changed the port, in which case replace "8080" with user specified port.

### List of all endpoints 

| Method | Endpoint                              | Description                                                             |
|--------|---------------------------------------|-------------------------------------------------------------------------|
| POST   | /event                                | Registers new event                                                     |
| GET    | /event/financialReport                | Returns current balance of each event                                   |
| POST   | /collectionBoxes                      | Registers new collection box                                            |
| GET    | /collectionBoxes/listAll              | Returns information about each collection box                           |
| DELETE | /collectionBoxes/{id}                 | Unregisters collection box in case of its destruction/loss              |
| PATCH  | /collectionBoxes/{id}/event/{eventId} | Assigns collection box to specified event                               |
| PATCH  | /collectionBoxes/{id}/addCash         | Increases balance of collection box by amount of specified currency     |
| PATCH  | /collectionBoxes/{id}/emptyBox        | Removes all funds allocated in box and transfers them to assigned event |
****
### Detailed descriptions
You can access the example requests through the following postman workspace:

https://www.postman.com/restless-crescent-793224/fundraising/overview

If you prefer not to use postman the following text contains detailed description of the endpoints and example curl commands for cmd.

#### Warning !!!
Example curl commands were tested on Windows 11 cmd. They  may not work on powershell, bash or other command line interfaces due to differences in syntax.  
****
#### 1. Event registration (POST) /event
Endpoint is capable of registering event with user specified name and currency code.
All the following parameters are mandatory and must be passed through request body in JSON format.
- eventName (can't be blank, can't exceed 255 characters)
- currencyCode (must follow ISO 4217 format, right now currencies are limited to PLN, EUR and USD)
##### Example curl
```cmd
curl -L "http://localhost:8080/api/event" -H "Content-Type: application/json" -d "{\"eventName\": \"New Event\" , \"currencyCode\": \"EUR\"  }", 
```
##### Example response after successful operation
```json
{"id":1,"currency":"EUR","balance":0.0,"name":"New Event"}
```
****

#### 2. Financial report (GET) /event/financialReport
Endpoint is capable of retrieving financial report for fundraising events.
##### Example curl
```cmd
curl -L "http://localhost:8080/api/event/financialReport"
```
##### Example response after successful operation
```json
[{"currency":"EUR","eventName":"New Event1","amount":"0.94"},{"currency":"EUR","eventName":"New Event2","amount":"0.00"}]
```
****

#### 3. Collection box registration (POST) /collectionBoxes
Endpoint is capable of registering new blank collection boxes.

##### Example curl
```cmd
curl -L -X POST "http://localhost:8080/api/collectionBoxes"
```
##### Example response after successful operation
```json
 {"id":2,"event":null,"currencies":null}
```
****
####  4. List collection boxes (POST) /collectionBoxes/listAll
Endpoint is capable of listing out all collection boxes and giving information about their assignment state and if they are empty.
##### Example curl
```cmd
curl -L "http://localhost:8080/api/collectionBoxes/listAll"
```
##### Example response after successful operation
```json
[{"empty":true,"id":2,"assignedToEvent":false},{"empty":true,"id":3,"assignedToEvent":false},{"empty":false,"id":1,"assignedToEvent":true}]
```
****
#### 5. Unregister collection box (DELETE) /collectionBoxes/{id}
Endpoint is capable of unregistering event, which basically leads to removing it while losing data about its balance . Use in case of destruction/loss of the collection box.
All the path parameters are mandatory .
- {id} (id of the assigned collection box, natural number)
##### Example curl
```cmd
curl -L -X DELETE "http://localhost:8080/api/collectionBoxes/1"
```

****



####  6. Event assignment (PATCH) /collectionBoxes/{id}/event/{eventId}
Endpoint is capable of assigning collection boxes to events.
All the path parameters are mandatory .
- {id}  (id of the assigned collection box, natural number)  
- {eventId} (id of the event, natural number)
  
##### Example curl
```cmd
curl -L -X PATCH "http://localhost:8080/api/collectionBoxes/1/event/1"
```



****
####  7. Cash addition (PATCH) /collectionBoxes/{id}/addCash
Endpoint is capable of increasing balance of collection box event by user specified amount of currency, while keeping the balance of each currency separately.
All the following parameters are mandatory and must be passed in request body in JSON format.
- cashAmount (text representation of a number starting with 1 to 7 digits, followed by a period, and ending with exactly 2 digits, it must be greater than 0 )
- currencyCode (must follow ISO 4217 format, right now currencies are limited to PLN, EUR and USD)

##### Example curl
```cmd
curl -L -X PATCH "http://localhost:8080/api/collectionBoxes/1/addCash" -H "Content-Type: application/json" -d "{ \"currencyCode\": \"PLN\", \"cashAmount\": \"01.00\" }"
```
****

#### 8. Box emptying (PATCH) /collectionBoxes/{id}/emptyBox
Endpoint exchanges the money stored in collection box to the currency specified in the assigned event, after it the money is transferred to the assigned events balance.
All the path parameters are mandatory .
- {id}  (id of the assigned collection box, natural number)
##### Example curl (cmd)
```cmd
curl -L -X PATCH "http://localhost:8080/api/collectionBoxes/1/emptyBox"
```

****
