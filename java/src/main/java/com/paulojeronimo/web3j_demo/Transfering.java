package com.paulojeronimo.web3j_demo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

public class Transfering {
    public static void main(String[] args) throws IOException, 
        CipherException, InterruptedException,
        ExecutionException, TransactionTimeoutException {

        if (args.length < 3)
            throw new RuntimeException("You must enter the address and the password");

        String account1File = args[0];
        String account1Password = args[1];
        String account2 = args[2];

        Web3j web3 = Web3j.build(new HttpService());
        Credentials credentials = WalletUtils.loadCredentials(account1Password, account1File);
        TransactionReceipt transactionReceipt = Transfer
            .sendFundsAsync(web3, credentials, "0x" + account2, BigDecimal.valueOf(0.01), Convert.Unit.ETHER)
            .get();

        System.out.println("Funds transfer completed...");
        System.out.println("Hash: " + transactionReceipt.getBlockHash());
        System.out.println("Block number: " + transactionReceipt.getBlockNumber());
    }
}
