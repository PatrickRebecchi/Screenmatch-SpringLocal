package br.com.alura.screenmatchSpring.repository;

import br.com.alura.screenmatchSpring.service.SerieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class SerieRepositoryTest {

    @Mock
    private SerieRepository repository;

    @InjectMocks
    private SerieService service;

    @Test
    void Top5Series() {
    }

    @Test
    void  findByGenero(){

    }

}