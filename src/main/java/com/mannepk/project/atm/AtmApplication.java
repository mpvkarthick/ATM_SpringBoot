package com.mannepk.project.atm;

import com.mannepk.project.atm.exception.ATMException;
import com.mannepk.project.atm.model.Transaction;
import com.mannepk.project.atm.service.ATM;
import com.mannepk.project.atm.store.GlobalStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

@EnableAutoConfiguration
@SpringBootApplication
public class AtmApplication implements ApplicationRunner {
	
	Logger logger =  LoggerFactory.getLogger(AtmApplication.class);

    BufferedReader br;

    @Value("${currency}")
    private String allowedCurrency;

    @Bean
    public GlobalStore globalStore() {
        return new GlobalStore(this.allowedCurrency);
    }

    @Autowired
    private ATM atm;

    public static void main(String[] args) {
        SpringApplication.run(AtmApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Scanner s = new Scanner(System.in);
        br = new BufferedReader(new InputStreamReader(System.in));
        String dInput;
        Transaction transaction;
        System.out.println("Welcome User, please choose your action - ");
        while (true) {
            System.out.println("Please choose your action - ");
            System.out.println("Choose 1 for Deposit");
            System.out.println("Choose 2 for Withdraw");
            System.out.println("Choose 3 for EXIT");
            System.out.println("Enter the operation");
            int n = Integer.parseInt(br.readLine());
            switch (n) {
                case 1:
                    System.out.println("Enter money to be Deposited:");
                    dInput = br.readLine();
                    transaction = new Transaction();
                    try {
                        atm.deposit(dInput, transaction);
                        logger.debug("Transaction Status "+transaction.getStatus());
                        System.out.println(transaction.toString());

                    } catch (ATMException e) {
                    	logger.error("Transaction Details | Transaction ID : "+transaction.getId()+" | Transaction Type : "+transaction.getType()+"| Input : "+transaction.getInput()+" | Reason for Faiulre : "+ e.getReason());
                        transaction.setStatus("Failed");
                        System.out.println(e.getReason());
                    } catch (Exception e) {
                        transaction.setStatus("Failed");
                        logger.error("Transaction Details | Transaction ID : "+transaction.getId()+" | Transaction Type : "+transaction.getType()+"| Input : "+transaction.getInput()+" | Reason for Faiulre : Invalid Input");
                        System.out.println("Invalid Input");
                    }
                    System.out.println("");
                    break;

                case 2:
                    System.out.println("Enter money to be Withdrawn:");
                    dInput = br.readLine();
                    transaction = new Transaction();
                    try {
                        atm.withdraw(Double.parseDouble(dInput), transaction);
                        
                        System.out.println(transaction.toString());

                    } catch (ATMException e) {
                        transaction.setStatus("Failed");
                        logger.error("Transaction Details | Transaction ID : "+transaction.getId()+" | Transaction Type : "+transaction.getType()+"| Input : "+transaction.getInput()+" | Reason for Faiulre : "+ e.getReason());
                        System.out.println(e.getReason());
                    } catch (Exception e) {
                        transaction.setStatus("Failed");
                        logger.error("Transaction Details | Transaction ID : "+transaction.getId()+" | Transaction Type : "+transaction.getType()+"| Input : "+transaction.getInput()+" | Reason for Faiulre : Invalid Input");
                        System.out.println("Invalid Input");
                    }
                    System.out.println("");
                    break;

                case 3:

                    s.close();
                    br.close();
                    System.exit(0);
            }
        }

    }

}
