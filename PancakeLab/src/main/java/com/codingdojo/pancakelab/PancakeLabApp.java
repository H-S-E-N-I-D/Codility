package com.codingdojo.pancakelab;

import com.codingdojo.pancakelab.client.PancakeLabClientApp;

public class PancakeLabApp {

    public static void main(String[] args) {
        // Skip actual execution during tests
        if (System.getProperty("testing") != null) {
            return;
        }
        PancakeLabClientApp clientApp = new PancakeLabClientApp();
        clientApp.run();
    }
}
