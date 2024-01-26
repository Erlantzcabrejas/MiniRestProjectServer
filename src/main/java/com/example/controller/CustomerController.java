package com.example.controller;

import com.example.models.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable long id) {

       try {
           Customer customer = null;
           Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/customers?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", "root", "6433");   // For MySQL only

           Statement stmt = conn.createStatement();
           String strSelect = "select * from customers where reference = " + id;
           System.out.println("The SQL statement is: " + strSelect + "\n");

           ResultSet rset = stmt.executeQuery(strSelect);

           while (rset.next()) {   // Repeatedly process each row
               long reference = rset.getLong("reference");
               String name = rset.getString("name");
               String ad1 = rset.getString("address1");
               String ad2 = rset.getString("address2");
               String town = rset.getString("town");
               String county = rset.getString("county");
               String country = rset.getString("country");
               String postcode = rset.getString("postcode");

               customer = new Customer(reference, name, ad1, ad2, town, county, country, postcode);
           }

           if(customer == null){
               return ResponseEntity.notFound().build();
           }

           return ResponseEntity.ok(customer);
       }catch (Exception e){
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while searching for the customer", e);
       }
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<ArrayList<Customer>> addCustomers(@RequestBody ArrayList<Customer> customers) throws Exception {
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/customers?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "6433");   // For MySQL only

            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into customers values ( ?, ?, ?, ?, ?, ?, ? ,?)");

            for (Customer customer : customers) {
                pstmt.setLong(1, customer.getReference());
                pstmt.setString(2, customer.getName());
                pstmt.setString(3, customer.getAddress1());
                pstmt.setString(4, customer.getAddress2());
                pstmt.setString(5, customer.getTown());
                pstmt.setString(6, customer.getCounty());
                pstmt.setString(7, customer.getCountry());
                pstmt.setString(8, customer.getPostcode());
                pstmt.addBatch();
            }
            int[] updateCounts = pstmt.executeBatch();
            System.out.println(updateCounts+" rows were added");

            return ResponseEntity.ok().build();

        }catch (BatchUpdateException e){
            ArrayList<Customer> notUploadedCustomers = new ArrayList<>();
            int[] batchCodes = e.getUpdateCounts();
            int countOfRows = batchCodes.length;
            int uploadedRows = 0;


            for(int i = 0; i < countOfRows; i++){
                if(batchCodes[i] == -3){
                    System.out.println("The costumer with reference "+customers.get(i).getReference()+" already exists...");
                }
                else if(batchCodes[i] == 1){
                    uploadedRows++;
                }
            }

            System.out.println(uploadedRows+" rows were uploaded...");

            if(uploadedRows > 0){
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.internalServerError().build();
        }
    }
}
