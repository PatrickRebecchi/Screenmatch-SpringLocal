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
        Serie serie = new Serie();
        serie.setId(1L);
        serie.setTitulo("Breaking Bad");
        serie.setTotalTemporadas(5);
        serie.setAvaliacao(9.5);
        serie.setGenero(Categoria.DRAMA);
        serie.setAtores("Bryan Cranston");
        serie.setPoster("poster.jpg");
        serie.setSinopse("Um professor vira produtor de metanfetamina.");

        Serie serie1 = new Serie();
        serie1.setId(2L);
        serie1.setTitulo("The Office");
        serie1.setTotalTemporadas(10);
        serie1.setAvaliacao(8.9);
        serie1.setGenero(Categoria.COMEDIA);
        serie1.setAtores("Steve Carell");
        serie1.setPoster("poster.jpg");
        serie1.setSinopse("Um documentário simulado sobre um grupo de típicos funcionários de escritório.");

        when(repository.findById(1L)).thenReturn(Optional.of(serie));
        when(repository.findById(2L)).thenReturn(Optional.of(serie1));

        SerieDTO resultado = service.obterPorId(1L);
        SerieDTO resultado1 = service.obterPorId(2L);

        assertNotNull(resultado1);
        assertEquals("The Office", resultado1.titulo());
        verify(repository).findById(2L);

        assertNotNull(resultado);
        assertEquals("Breaking Bad", resultado.titulo());
        verify(repository).findById(1L);
    }

    @Test
    void lancarExcecaoQuandoSerieNaoExiste() {

        when(repository.findById(1L)).thenReturn(Optional.empty());
        ScreenmatchException exception =
                assertThrows(ScreenmatchException.class,
                        () -> service.obterPorId(1L));
        assertEquals("Série não encontrada!", exception.getMessage());
        verify(repository).findById(1L);
    }

    @Test
    void deveRetSerieQuandoIdExiste(){
        Serie serie = new Serie();
        serie.setId(1L);
        serie.setTitulo("Teste");

        when(repository.findById(1L)).thenReturn(Optional.of(serie));

        SerieDTO resultado = service.obterPorId(1L);

        assertEquals("Teste", resultado.titulo());
        verify(repository).findById(1L);
    }
}