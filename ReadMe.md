# Toll Parking API App
SpringBoot Application that demonstrates REST API Development using Spring MVC, Spring Data JPA using Java 11 features

## Description

This application has various REST end-points that provide Toll-parking and billing capabilities, categorized as:.

1. Parking Pricing Policy APIs
2. Parking Slot APIs
3. Parking Billing APIs

All end points are implemented with Async operations, using CompletableFuture.

#### Capabilities

All above APIs translate into below app features - 
1. Maintenance of Pricing Policies for various Parking Slots, i.e., `STANDARD`,`CAR_20KW_ELECTRIC`,`CAR_50KW_ELECTRIC`
2. Every new parking request will be associated to a specific Parking Slot, Pricing Policy
3. On exiting the parking, the association mentioned in step#2, is used to calculate the bill; based on hourly/ hourly+fixed pricing on a particular parked parking slot
4. New Pricing Policies can be created using `Parking Pricing Policy APIs`
5. New Parking Slots can be created using `Parking Slot APIs`. Bulk creation API is also provided, where one can upload a csv file into a specific format, e.g., refer `bulkParkingSlots.csv` present in `com/tollparking/lib/app/data/`

####### Note: Bulk API could be tricky to invoke from swagger, therefore please use `postman` tool or cURL command - `curl --location --request POST 'http://localhost:8082/parking/api/v1/bulkCreateParkingSlot' --header 'Content-Type: multipart/form-data; boundary=--------------------------406159848933623384591131' --form 'file=@/D:/t2020/parking-app/src/main/java/com/tollparking/lib/app/data/bulkParkingSlots.csv'`

##### Assumption

A Pricing Policy can't be deleted, if it is actively assigned to a parking slot


#####  Basic Validations
a. While creating new Pricing Policies, if policy is hourly based, then hour price input is mandatory

b. A vehicle with a valid vehicle number, once parked in the system; can't be re-parked until it exits and re-enters.

c. Other Business Validations are handled using custom Exceptions

##### A set of Pricing Policies and Parking Slots are pre-populated in the DB on App start up
```
PP_ID  	PRICING_POLICY_NAME  	            IS_HOURLY  	FIXED_PRICE  	HOUR_PRICE  	CLEANING_SERVICE_CHARGE  	OTHER_EXTRA_CHARGES  
1	Hourly-STANDARD	                    TRUE	12.00	        15.00	        0.00	                        0.00
2	Fixed+Hourly-STANDARD	            FALSE	10.00	        8.00	        0.00	                        0.00
3	Hourly-CAR_20KW_ELECTRIC	    TRUE	15.00	        18.00	        0.00	                        0.00
4	Fixed+Hourly-CAR_20KW_ELECTRIC	    FALSE	18.00	        16.00	        0.00	                        0.00
5	Hourly-CAR_50KW_ELECTRIC	    TRUE	18.00	        20.00	        0.00	                        0.00
6	Fixed+Hourly-CAR_50KW_ELECTRIC	    FALSE	20.00	        15.00	        0.00	                        0.00

```
```
PS_ID  	PARKING_TYPE  	        IS_AVAILABLE  	PRICING_POLICY_ID  
1	STANDARD	        TRUE	        1
2	STANDARD	        TRUE	        1
3	STANDARD	        TRUE	        1
4	STANDARD	        TRUE	        1
5	STANDARD	        TRUE	        1
6	STANDARD	        TRUE	        2
7	STANDARD	        TRUE	        2
8	STANDARD	        TRUE	        2
9	STANDARD	        TRUE	        2
10	STANDARD	        TRUE	        2
11	CAR_20KW_ELECTRIC	TRUE	        3
12	CAR_20KW_ELECTRIC	TRUE	        3
13	CAR_20KW_ELECTRIC	TRUE	        3
14	CAR_20KW_ELECTRIC	TRUE	        4
15	CAR_20KW_ELECTRIC	TRUE	        4
16	CAR_20KW_ELECTRIC	TRUE	        4
17	CAR_50KW_ELECTRIC	TRUE	        5
18	CAR_50KW_ELECTRIC	TRUE	        5
19	CAR_50KW_ELECTRIC	TRUE	        5
20	CAR_50KW_ELECTRIC	TRUE	        6
21	CAR_50KW_ELECTRIC	TRUE	        6
22	CAR_50KW_ELECTRIC	TRUE	        6
```

```
Once the App comes up, one can directly invoke below URLs for making Parking request and exit Parking request respectively:

1. curl -X POST "http://localhost:8082/toll/api/v1/makeParkingBooking/CAR_20KW_ELECTRIC?isHourly=true&vehicleNumber=ABC%2024%20Z" -H  "accept: */*"
2. curl -X POST "http://localhost:8082/toll/api/v1/exitParking/ABC%2024%20Z" -H  "accept: */*"

For other various types of available REST APIs, please refer Swagger Documention.
```

## Technologies used

1. Java 11 (Programming Language)
2. Spring Boot (Application Platform)
3. Spring Data JPA (Data persistence)
4. H2 (Database)
5. Flyway

## Getting Started

The source code can be checked out to your local; build and run the application either from your IDE after importing it as a maven project, or just from a command line. Follow these steps for the command-line option:  

### Prerequisites
1. Java 11
2. Maven 3
3. Git


### Installing & Running

####  Build using maven 
	
```
mvn clean install
```
	
#### Start the app
	
```
mvn spring-boot:run
```

## API Documentation and Integration Testing 

API documentation can be accessed, once app is up **locally**, via [Swagger UI](http://localhost:8082/swagger-ui.html) 


## Database

This application is using H2 in-memory database, which (database as well as data) will be removed from memory when the application goes down.
Database Console can also be accessed when App is running at - [H2 Console](http://localhost:8082/h2c/)
```
Credentials:
- userName: sa
- password: 
```

## Data pre-loading

Sample data is pre-loaded via flyway

## Test Strategy

1. UTs are implemented, covering most of the scenarios for APIs, Service Later, and Persistence Layer; via Mockito. 
2. BDD Tests, using cucumber, can also be added to test each API end-to-end. // TO-DO


## Added Basic configuration for Docker

Dockerfile contains the configuration. Not Tested.

## CI/CD Configuration

Jenkinsfile contains the proposal(draft state at present, will be completed once the Dockerization is tested successfully) to set up CI/CD for the application
