package br.com.alura.screenmatchSpring.service;

import br.com.alura.screenmatchSpring.dto.EpisodioDTO;
import br.com.alura.screenmatchSpring.dto.SerieDTO;
import br.com.alura.screenmatchSpring.model.*;
import br.com.alura.screenmatchSpring.repository.SerieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obterTodasAsSeries(){
        return converteDados(repository.findAll());
    }

    public List<SerieDTO> top5Series(){
        //return repository.findTop5ByOrderByAvaliacaoDesc()
        return converteDados(repository.Top5Series(PageRequest.of(0,5)));

    }
    public List<SerieDTO> obeterSeriePorTitulo(String name){
        return converteDados(repository.findByTituloContainingIgnoreCase(name));
    }

    public  SerieDTO obterPorId(long id){
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse());
        }
        return null;
    }
    private List<SerieDTO> converteDados(List<Serie> series){
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repository.findTop5ByOrderByEpisodiosDataLancamentoDesc());
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(long id, long numero) {
        return repository.obterEpisodioPorTemporada(id, numero).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeriePorCategoria(String nomeGenero) {
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        return converteDados(repository.findByGenero(categoria));
    }

//    public List<EpisodioDTO> obterTop5Episodios(long id) {
//        Serie serie = repository.findById(id).get();
//
//        return repository.Top5Episodios(serie)
//                .stream()
//                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
//                .collect(Collectors.toList());
//
//    }

    public List<EpisodioDTO> obterTopEpisodios(Long id) {
        return repository.topEpisodiosPorSerie(id, PageRequest.of(0,5))
                .stream()
                .map(e -> new EpisodioDTO(
                        e.getTemporada(),
                        e.getNumeroEpisodio(),
                        e.getTitulo()
                ))
                .toList();
    }

    final String ENDERECO = "https://www.omdbapi.com/?t=";
    final String API_KEY = "&apikey=6585022c";
    ConsumoAPI consumo = new ConsumoAPI();
    ConverteDados conversor = new ConverteDados();

//    public SerieDTO importarSerie(String titulo) {
//
//        var json = consumo.obterDados(ENDERECO + titulo.replace(" ", "+") + API_KEY);
//        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
//
//        Serie serie = new Serie(dados);
//
//        repository.save(serie);
//
//        return new SerieDTO(
//                serie.getId(),
//                serie.getTitulo(),
//                serie.getTotalTemporadas(),
//                serie.getAvaliacao(),
//                serie.getGenero(),
//                serie.getAtores(),
//                serie.getPoster(),
//                serie.getSinopse()
//        );
//    }
//
//    public void buscarEpisodiosPorSerie(Long id) {
//        Serie serie = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Série não encontrada"));
//
//        List<DadosTemporada> temporadas = new ArrayList<>();
//
//        for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
//
//            var json = consumo.obterDados(
//                    ENDERECO +
//                            serie.getTitulo().replace(" ", "+") +
//                            "&season=" + i +
//                            API_KEY
//            );
//
//            DadosTemporada dadosTemporada =
//                    conversor.obterDados(json, DadosTemporada.class);
//
//            temporadas.add(dadosTemporada);
//        }
//
//        List<Episodio> episodios = temporadas.stream()
//                .flatMap(d -> d.episodios().stream()
//                        .map(e -> new Episodio(d.numero(), e)))
//                .collect(Collectors.toList());
//
//        serie.setEpisodios(episodios);
//
//        repository.save(serie);
//    }

    public SerieDTO importarSerie(String titulo) {

        // uso para buscar dados da série
        var json = consumo.obterDados(ENDERECO + titulo.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        Serie serie = new Serie(dados);

        // buscar temporadas e episódios
        List<Episodio> episodios = new ArrayList<>();

        for (int i = 1; i <= serie.getTotalTemporadas(); i++) {

            var jsonTemp = consumo.obterDados(
                    ENDERECO +
                            serie.getTitulo().replace(" ", "+") +
                            "&season=" + i +
                            API_KEY
            );

            DadosTemporada dadosTemporada =
                    conversor.obterDados(jsonTemp, DadosTemporada.class);

            episodios.addAll(
                    dadosTemporada.episodios().stream()
                            .map(e -> new Episodio(dadosTemporada.numero(), e))
                            .collect(Collectors.toList())
            );
        }

        // Vincular episódios à série
        serie.setEpisodios(episodios);


        repository.save(serie);


        return new SerieDTO(
                serie.getId(),
                serie.getTitulo(),
                serie.getTotalTemporadas(),
                serie.getAvaliacao(),
                serie.getGenero(),
                serie.getAtores(),
                serie.getPoster(),
                serie.getSinopse()
        );
    }

    public void deletarSerie(long id) {

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Série não encontrada");
        }

        repository.deleteById(id);
    }
}
