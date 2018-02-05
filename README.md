# Task 2 - Java/Spring - Interconnecting Flights

Spring MVC based RESTful API. 

## Description

The application responses to a request with given query parameters:

```
http://<HOST>/ryanairtask/interconnections?departure={departure}&arrival={arrival}&depa rtureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime} 
```

It returns a list of flights departing from a given departure airport not earlier than the specified departure date time and arriving to a given arrival airport no later than the specified arrival date time.

Airports are expressed in IATA format and datetimes in ISO format.

For interconnected flights the difference between the arrival and the next departure is 2 hours or greater. 

The data returned is on JSON format.


### Example

For the following client request:

```
http://localhost:8080/ryanairtask/interconnections?departure=DUB&arrival=WRO&departureDateTime=2018-03-01T12:00&arrivalDateTime=2018-03-01T23:00
```

The application returns the flights list of the following form:

```
[
   {
      "stops":0,
      "legs":[
         {
            "departureAirport":"DUB",
            "arrivalAirport":"WRO",
            "departureDateTime":"2018-03-01T19:10",
            "arrivalDateTime":"2018-03-01T22:45"
         }
      ]
   },
   {
      "stops":1,
      "legs":[
         {
            "departureAirport":"DUB",
            "arrivalAirport":"EDI",
            "departureDateTime":"2018-03-01T12:35",
            "arrivalDateTime":"2018-03-01T13:45"
         },
         {
            "departureAirport":"EDI",
            "arrivalAirport":"WRO",
            "departureDateTime":"2018-03-01T17:00",
            "arrivalDateTime":"2018-03-01T20:15"
         }
      ]
   }
]
```

## Deployment

The project war file is located in this root folder as ryanairtask.war

## Built With

* [Spring Web MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management

## Author

* **Jose Angel Hernandez Garcia-Gango** 


