package com.app.bikerental.util;

import com.app.bikerental.model.Payment;

import java.io.*;
import java.util.*;

public class PaymentUtil {
    private static final String FILE = "E:\\Y1S2\\OOP\\Payment\\src\\main\\webapp\\txt\\payment_history.txt";

    public static void savePayment(Payment p) {
        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write(p.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllPayments() {
        List<String> payments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                payments.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payments;
    }


    public static void deletePaymentByCard(String last4Digits) {
        File inputFile = new File("E:\\Y1S2\\OOP\\Payment\\src\\main\\webapp\\txt\\payment_history.txt");
        File tempFile = new File("E:\\Y1S2\\OOP\\Payment\\src\\main\\webapp\\txt\\temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("Card: ****" + last4Digits)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }
    }

}
