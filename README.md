[![BCH compliance](https://bettercodehub.com/edge/badge/douggass/sales-file-processor?branch=master)](https://bettercodehub.com/)

# Service to process file

This project contains a sales file processor.

## Requirements

1. Java - 1.8.x

2. Maven - 3.x.x


## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/douggass/sales-file-processor.git
```

**2. Build and run the app using maven**

```bash
cd sales-file-processor
mvn package
java -jar target/file-processor-1.0.0.jar
```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

## Exploring the service

The application will create the folders in the HOMEPAH directory:

```
1. HOMEPAH/data/in - Folder to be listened to and if a file is added it will be processed and deleted

2. HOMEPAH/data/out - Folder with the result of processing the files
```

## File Format
The archvi must contain lines in the format:

**Seller Data**
The seller data has the identifier 001 and follows the following format: 

```
001çCPFçNameçSalary
```


**Client's data**
The customer data has the identifier 002 and follows the following format: 

```
002çCNPJçNameçBusiness Area
```


**Selling Data**
The sales data has the identifier 003 and follows the following format: 

```
003çSale IDç [Item ID-Item Quantity-Item Price] çSeller Name
```


## Running tests

The project also contains tests for all the rules. For running the tests, go to the root directory of the project and type `mvn test` in your terminal.
