package com.paulojeronimo.web3j_demo;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import com.paulojeronimo.web3j_demo.generated.HelloWorld;

public class HelloWorldClient {
    public static void main(String[] args) throws IOException, CipherException, ExecutionException, InterruptedException {
        String walletSource = args[0];
        String walletFilePassword = args[1];
        Utf8String message;
        HelloWorld contract;
            
        Web3j web3j = Web3j.build(new HttpService());

        Credentials credentials = WalletUtils.loadCredentials(walletFilePassword, walletSource);

        if (args.length == 3) {
            String address = args[2];

            contract = HelloWorld.load(address, web3j, credentials, ManagedTransaction.GAS_PRICE,
                Contract.GAS_LIMIT);
            message = contract.getMessage().get();
            System.out.printf("getMessage(): %s\n", message.getValue());
            return;
        }

        contract = HelloWorld.deploy(web3j, credentials, ManagedTransaction.GAS_PRICE,
            Contract.GAS_LIMIT, BigInteger.ZERO).get();

        System.out.printf("contract transaction mined!\naddress: %s\n", contract.getContractAddress());
        message = contract.getMessage().get();
        System.out.printf("getMessage(): %s\n", message.getValue());

        String newMessage = "Hello, Paulo Jeronimo, from Ethereum (from Java)!";
        TransactionReceipt transactionReceipt = contract.setMessage(new Utf8String(newMessage)).get();
        System.out.printf("setMessage(\"%s\") transaction: %s\n", newMessage, transactionReceipt.getTransactionHash());
    }
}
