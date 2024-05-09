# Quarkus Financial

This project made with Quarkus and OpenJdk21, provides a simple API to store and retrieve stock price data. It allows users to store stock prices by symbol and retrieve a list of stored stock data.

### System Requirements

- Requires <b>JDK 21</b> installed in your OS to compile.
- Requires <b>Maven installed</b> in your OS to compile.
- Requires <b>Docker installed</b> and working in your operating system.
- Requires <b>Git installed</b> in your operating system.

### Downloading the Project

To download the project and accessing to the working directory, use:
```shell script
  $ git clone https://github.com/jpcastro087/quarkus-financial.git
```
```shell script
  $ cd quarkus-financial
```

### Package and Testing

First, we need to package and test the application.:

```shell script
$ mvn clean install
```

### Building Image with Docker

In order to be able to run the application independently of the operating system we are on, we will create an image of it with Docker.
```shell script
$ docker build -f src/main/docker/Dockerfile.jvm -t quarkus/quarkus-financial-jvm .
```
If you are on Windows is:
```shell script
$ docker build -f src\main\docker\Dockerfile.jvm -t quarkus/quarkus-financial-jvm .
```


### Running Image created with Docker

Now we indicate that we want to run the image we created earlier, and then we will have the application deployed at http://localhost:8080
```shell script
$ docker run -i --rm -p 8080:8080 quarkus/quarkus-financial-jvm
```

### URL Swagger Interface

Once the application is up you can access to tests the functionallity on:
```shell script
http://localhost:8080/q/swagger/
```

### Endpoints

After the application is running, endpoints can be tested via Swagger UI at `http://localhost:8080/q/swagger`.

- `/stock/create` (POST): This endpoint requires a JSON body with the following structure:
```json
{
  "symbol": "AAPL"
}
```
If the symbol is found, data is stored in an H2 database and a response with the following structure is returned:
```json
{
  "id": 3,
  "symbol": "AAPL",
  "closePrice": 182.6665,
  "absoluteChange": 0.9565,
  "percentageChange": 0.5264,
  "highPrice": 184.9,
  "lowPrice": 182.31,
  "openPrice": 183.4,
  "previousClosePrice": 181.71,
  "timestamp": 1715094870
}
```
If the symbol is not found, no data is stored and the following response is returned:
```json
{
  "error_message": "The symbol 'APPLE' appears to not exist"
}
```

- `/stock/list` (GET): This endpoint retrieves a list of stocks stored by the `/stock/create` endpoint. The response structure is as follows:
```json
[
  {
    "id": 1,
    "symbol": "AAPL",
    "closePrice": 182.75,
    "absoluteChange": 1.04,
    "percentageChange": 0.5723,
    "highPrice": 184.9,
    "lowPrice": 182.31,
    "openPrice": 183.4,
    "previousClosePrice": 181.71,
    "timestamp": 1715094827
  },
  {
    "id": 2,
    "symbol": "AAPL",
    "closePrice": 182.6665,
    "absoluteChange": 0.9565,
    "percentageChange": 0.5264,
    "highPrice": 184.9,
    "lowPrice": 182.31,
    "openPrice": 183.4,
    "previousClosePrice": 181.71,
    "timestamp": 1715094870
  },
  {
    "id": 3,
    "symbol": "AAPL",
    "closePrice": 182.6665,
    "absoluteChange": 0.9565,
    "percentageChange": 0.5264,
    "highPrice": 184.9,
    "lowPrice": 182.31,
    "openPrice": 183.4,
    "previousClosePrice": 181.71,
    "timestamp": 1715094870
  }
]
```

### Test Scenarios covered with @QuarkusTest

1. **testCreateValidSymbol**: This test scenario verifies that when a valid symbol is provided to the `/stock/create` endpoint, the application successfully stores the stock data and returns a status code of 200.
   
2. **testCreateSymbolNotFound**: This test scenario verifies that when an invalid symbol is provided to the `/stock/create` endpoint, the application returns a status code of 404, indicating that the symbol was not found.

3. **testGetStockMarketActions**: This test scenario verifies that the `/stock/list` endpoint returns a list of stock market actions when called. It checks that the response status code is always OK (200).


