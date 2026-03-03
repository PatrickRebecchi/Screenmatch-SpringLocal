package br.com.alura.screenmatchSpring.repository;

import br.com.alura.screenmatchSpring.model.Categoria;
import br.com.alura.screenmatchSpring.model.Episodio;
import br.com.alura.screenmatchSpring.model.Serie;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    List<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    Optional<Serie> findByTitulo(String titulo);

    //List<Serie> findById(long idSerie);
    // usando a JPQL para puxar o id

    Optional<Serie> findById(long idSerie);


    @Query("SELECT s FROM Serie s WHERE s.Id = :id")
    List<Serie> buscarPorId(int id);


    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double avaliacao);

    @Query("SELECT s FROM Serie s WHERE LOWER(s.atores) LIKE LOWER(CONCAT('%', :nome, '%')) AND s.avaliacao >= :avaliacao")
   //@Query("SELECT s FROM Serie s WHERE s.atores ILIKE %:nome% AND s.avaliacao >= :avaliacao")
    List<Serie> fingBuscarPotAtoresEAvaliacao(String nome, double avaliacao);

    List<Serie> findByGenero(Categoria genero);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    @Query("SELECT s FROM Serie s ORDER BY s.avaliacao DESC")
    List<Serie> Top5Series(Pageable pageble);

//    @Query("SELECT e FROM Serie e ORDER BY e.avaliacao DESC")
//    List<Episodio> Top5Episodios(Pageable pageble);


    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalDeTemporada, double avalicao);

    @Query("select s from Serie s Where s.totalTemporadas <= :totalDeTemporada AND s.avaliacao >= :avalicao")
    List<Serie> seriesPorTemporadaEAvaliacao(Integer totalDeTemporada, double avalicao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> Top5Episodios(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id ORDER BY e.avaliacao DESC")
    List<Episodio> topEpisodiosPorSerie(@Param("id") Long id, Pageable pageable);


    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC")
    List<Episodio> findtopEpisodiosPorSerie(Serie serie, Pageable pageable);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s= :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> findEpisodioPorSerieEAano(Serie serie, int anoLancamento);

    @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> findTop5ByOrderByEpisodiosDataLancamentoDesc();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND  e.temporada = :numero")
    List<Episodio> obterEpisodioPorTemporada(long id, long numero);


    boolean existsByTitulo(String titulo);

}
