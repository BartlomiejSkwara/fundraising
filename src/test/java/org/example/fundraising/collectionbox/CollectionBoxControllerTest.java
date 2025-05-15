package org.example.fundraising.collectionbox;

import org.example.fundraising.event.EventController;
import org.example.fundraising.event.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CollectionBoxController.class)
public class CollectionBoxControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CollectionBoxService collectionBoxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerBoxTest() throws Exception {
        mockMvc.perform(post("/api/collectionBox"))
                .andExpect(status().isOk());
        verify(collectionBoxService,times(1)).registerCollectionBox();
    }
}
