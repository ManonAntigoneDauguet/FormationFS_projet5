package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    MessageResponse messageResponse;

    @BeforeEach
    void setUp() {
        messageResponse = new MessageResponse("Message");
    }

    @Test
    void setMessage() {
        messageResponse.setMessage("newMessage");
        assertEquals("newMessage", messageResponse.getMessage());
    }
}