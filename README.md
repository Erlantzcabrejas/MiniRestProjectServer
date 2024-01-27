# Server side of the MiniRestProject:

This project implements a REST API to store and retrieve customer information data from an SQL database. The REST API is implemented using the SpringBoot framework.

## Endpoints

CustomerController contains the GET and POST endpoints.

**GET /api/customers/{ref}**: Will return the data of the provided customer id in JSON format if the data is found on the database.

**POST /api/customers:** Expects an array of customers in JSON format, with the following fields.

1. Customer Reference
2. Customer Name
3. Address1
4. Address2
5. Town
6. County
7. Country
8. Postcode

The received customers will be stored into the SQL database using a batch operation. Input example:

> [{"reference": 1234,
"name": "John",
"address1": "ad123",
"address2": "adress321",
"town": "Richmond",
"county": "somewhere",
"country": "UK",
"postcode": "654321"
},
{
"reference": 2452,
"name": "Jack",
"address1": "address721",
"address2": "address127",
"town": "greenford",
"county": "Ealing",
"country": "UK",
"postcode": "123456"
}
]

If some customers can't be added due to duplicated id or any other reason, the catch will process the rows to specify how many new rows have
been uploaded and which ones were already in the table, displaying their "reference" value.

## Database
The program expects a SQL database, which requires the following table to be created previously: 

> CREATE TABLE `customers` (
`reference` int NOT NULL,
`name` varchar(45) DEFAULT NULL,
`address1` varchar(45) DEFAULT NULL,
`address2` varchar(45) DEFAULT NULL,
`town` varchar(45) DEFAULT NULL,
`county` varchar(45) DEFAULT NULL,
`country` varchar(45) DEFAULT NULL,
`postcode` varchar(45) DEFAULT NULL,
PRIMARY KEY (`reference`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

The REST service uses JDBC to connect to the database. The address, port, user and pass needs to be specified.

> Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/customers?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "6433");

## Potential Improvements

- Separating the endpoints from the database processes.
- Control the database access credentials using a config file.
- Adding a script that creates the database automatically.
- Adding tests to verify all possible cases for the right functionality of the whole program.