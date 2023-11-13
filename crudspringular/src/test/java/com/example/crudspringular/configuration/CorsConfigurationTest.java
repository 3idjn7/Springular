package com.example.crudspringular.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
public class CorsConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void corsHeadersShouldBeSetForMoviesEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                        .header("Origin", "http://localhost:4200"))
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }

    @Test
    public void corsHeadersShouldBeSetForPutMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/movies/1")
                        .header("Origin", "http://localhost:4200"))
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }

    @Test
    public void corsHeadersShouldBeSetForDeleteMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/movies/1")
                        .header("Origin", "http://localhost:4200"))
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }

    @Test
    public void corsHeadersShouldBeSetForGenresEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/genres")
                        .header("Origin", "http://localhost:4200"))
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }


    @Test
    public void corsHeadersShouldBeSetForActorsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/actors")
                        .header("Origin", "http://localhost:4200"))
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }
}
