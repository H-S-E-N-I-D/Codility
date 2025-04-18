package com.codingdojo.pancakelab.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class PancakeLabClientAppTest {

    private PancakeLabClientApp clientApp;

    @BeforeEach
    void setUp() {
        clientApp = new PancakeLabClientApp();

        // Inject mock logger
        Logger clientLogger = Logger.getLogger(PancakeLabClientApp.class.getName());
        clientLogger.addHandler(new java.util.logging.Handler() {
            @Override
            public void publish(LogRecord record) {
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        });
    }

    @Test
    void run_ShouldProcessOrdersSuccessfully() throws Exception {
        // Verify the run method runs without throwing exceptions
        assertDoesNotThrow(() -> clientApp.run());

    }

}

