package br.com.alura.screenmatchSpring.service;


import br.com.alura.screenmatchSpring.dto.SerieDTO;
import br.com.alura.screenmatchSpring.exception.ScreenmatchException;
import br.com.alura.screenmatchSpring.model.Categoria;
import br.com.alura.screenmatchSpring.model.Serie;
import br.com.alura.screenmatchSpring.repository.SerieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SerieServiceTest {

    @Mock
    private SerieRepository repository;

    @InjectMocks
    private SerieService service;

    @Test
    void deveRetornarSerieQuandoIdExiste() {

        // Arrange
        Serie serie = new Serie();
        serie.setId(1L);
        serie.setTitulo("Breaking Bad");
        serie.setTotalTemporadas(5);
        serie.setAvaliacao(9.5);
        serie.setGenero(Categoria.DRAMA);
        serie.setAtores("Bryan Cranston");
        serie.setPoster("poster.jpg");
        serie.setSinopse("Um professor vira produtor de metanfetamina.");

        when(repository.findById(1L)).thenReturn(Optional.of(serie));

        // Act
        SerieDTO resultado = service.obterPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Breaking Bad", resultado.titulo());
        verify(repository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoSerieNaoExiste() {

        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        ScreenmatchException exception =
                assertThrows(ScreenmatchException.class,
                        () -> service.obterPorId(1L));

        assertEquals("Série não encontrada!", exception.getMessage());

        verify(repository).findById(1L);
    }
}