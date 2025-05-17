# Instructions
## Project Setup
Before following following steps please make sure that your java version is equal or greater then 21.
To setup a project you must perform following steps.
1. Clone the repository using
   git clone https://github.com/pppp.git
2. Build maven project todo
3. Run the project todo

todo what if port is already used

****
## REST API Endpoints
Whole app encompasses 8 endpoints that allow manipulating data of fundraising events.
Each endpoint after successfully setting up project should begin with http://localhost:8080/api.
If the port ... it may wary

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
You can access the examples of requests to all of the endpoints in following postman workspace:
https://www.postman.com/restless-crescent-793224/fundraising/overview
If you don't want to use it each description contains example curl command for cmd. 
#### 1. Event registration (POST) /api/event
Endpoint is capable of registering event with user specified name and currency code.
All of the following parameters are mandatory and must be passed in request body in JSON format.
- eventName (can't be blank, can't exceed 255 characters)
- currencyCode (must follow ISO 4217 format)
##### Example curl
```cmd
curl -L "http://localhost:8080/api/event" -H "Content-Type: application/json" -d "{\"eventName\": \"New Event\" , \"currencyCode\": \"EUR\"  }", 
```
##### Example response after successful operation
```json
{"id":1,"currency":"EUR","balance":0.0,"name":"New Event"}
```
****

#### 2. Financial report (GET) /api/event/financialReport
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

#### 3. Collection box registration (POST) /api/collectionBoxes
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
####  4. List collection boxes (POST) /api/collectionBoxes/listAll
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
#### 5. Unregister collection box (DELETE) /api/collectionBoxes/{id}
Endpoint is capable of registering event, which basically leads to losing whole data about its balance . Use in case of destruction/loss of the collection box.
All of the path parameters are mandatory .
- {id} (id of the assigned collection box, natural number)
##### Example curl
```cmd
curl -L -X DELETE "http://localhost:8080/api/collectionBoxes/1"
```

****



####  6. Event assignment (PATCH) /collectionBoxes/{id}/event/{eventId}
Endpoint is capable of assigning collection boxes to events.
All of the path parameters are mandatory .
- {id}  (id of the assigned collection box, natural number)  
- {eventId} (id of the event, natural number)
  
##### Example curl
```cmd
curl -L -X PATCH "http://localhost:8080/api/collectionBoxes/1/event/1"
```
##### TODO (think about adding response) Example response after successful operation
```json
```
****



####  7. Cash addition (PATCH) /api/collectionBoxes/{id}/addCash
Endpoint is capable of increasing balance of collection box event with user specified name and currency code. Each currencies typ balance is counted separately.
All of the following parameters are mandatory and must be passed in request body in JSON format.
- cashAmount (text representation of a number starting with 1 to 11 digits, followed by a period, and ending with exactly 2 digits)
- currencyCode (must follow ISO 4217 format)

##### Example curl
```shell
curl -L -X PATCH "http://localhost:8080/api/collectionBoxes/1/addCash" -H "Content-Type: application/json" -d "{ \"currencyCode\": \"PLN\", \"cashAmount\": \"01.00\" }"
```
##### TODO (think about adding response) Example response after successful operation
```json
```
****


#### 8. Box emptying (PATCH) /api/collectionBoxes/{id}/emptyBox
Endpoint exchanges the money stored in collection box to the currency specified in the assigned event, after it the money is transferred to the assigned events balance.
All of the path parameters are mandatory .
- {id}  (id of the assigned collection box, natural number)
##### Example curl (cmd)
```cmd
curl -L -X PATCH "http://localhost:8080/api/collectionBoxes/1/emptyBox"
```
##### TODO (think about adding response) Example response after successful operation
```json
{"id":1,"currency":"EUR","balance":0.0,"name":"New Event"}
```

****
