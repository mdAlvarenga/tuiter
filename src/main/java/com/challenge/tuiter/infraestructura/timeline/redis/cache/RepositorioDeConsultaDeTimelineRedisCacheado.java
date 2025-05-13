package com.challenge.tuiter.infraestructura.timeline.redis.cache;

import com.challenge.tuiter.dominio.timeline.RepositorioDeConsultaDeTimeline;
import com.challenge.tuiter.dominio.tuit.RepositorioDeBusquedaTuits;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.timeline.dto.TuitDto;
import com.challenge.tuiter.infraestructura.timeline.redis.ClaveDeTipo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class RepositorioDeConsultaDeTimelineRedisCacheado implements RepositorioDeConsultaDeTimeline {
  private static final int MAX_RESULTADOS = 50;

  private final StringRedisTemplate redis;
  private final RepositorioDeBusquedaTuits repoPostgres;
  private final ObjectMapper objectMapper;

  public RepositorioDeConsultaDeTimelineRedisCacheado(StringRedisTemplate redis, RepositorioDeBusquedaTuits repoPostgres, ObjectMapper objectMapper) {
    this.redis = redis;
    this.repoPostgres = repoPostgres;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<Tuit> timelineDe(Usuario usuario) {
    var ids = obtenerIdsDeTimeline(usuario);
    if (ids.isEmpty())
      return List.of();

    var claves = ids.stream().map(ClaveDeTipo.TUIT::con).toList();
    var idsList = new ArrayList<>(ids);

    List<String> jsons = Optional.ofNullable(redis.opsForValue().multiGet(claves))
                                 .orElse(Collections.nCopies(claves.size(), null));

    var resultado = procesarJsonsConFallback(jsons, idsList);
    var tuits = new ArrayList<>(resultado.tuits());

    if (resultado.existenIdsFaltantes()) {
      var desdeDb = repoPostgres.buscarTodosPorId(resultado.idsFaltantes());
      tuits.addAll(desdeDb);
      cachearTuits(desdeDb);
    }

    var mapa = tuits.stream().collect(Collectors.toMap(Tuit::getId, t -> t));
    return ids.stream().map(UUID::fromString).map(mapa::get).filter(Objects::nonNull).toList();
  }

  private ResultadoJsonProcessing procesarJsonsConFallback(List<String> jsons, List<String> idsList) {
    var tuits = new ArrayList<Tuit>();
    var idsFaltantes = new ArrayList<UUID>();

    for (int i = 0; i < jsons.size(); i++) {
      var json = jsons.get(i);
      if (json != null) {
        deserializarTuit(json).ifPresent(tuits::add);
      } else {
        idsFaltantes.add(UUID.fromString(idsList.get(i)));
      }
    }
    return new ResultadoJsonProcessing(tuits, idsFaltantes);
  }

  private Set<String> obtenerIdsDeTimeline(Usuario usuario) {
    var clave = ClaveDeTipo.TIMELINE.con(usuario.id());
    var ids = redis.opsForZSet().reverseRange(clave, 0, MAX_RESULTADOS - 1);
    return ids != null ? ids : Set.of();
  }

  private Optional<Tuit> deserializarTuit(String json) {
    try {
      return Optional.of(objectMapper.readValue(json, TuitDto.class).aTuit());
    } catch (JsonProcessingException e) {
      log.warn("Error al deserializar tuit cacheado", e);
      return Optional.empty();
    }
  }

  private void cachearTuits(List<Tuit> tuits) {
    tuits.forEach(t -> {
      try {
        redis.opsForValue().set(ClaveDeTipo.TUIT.con(t.getId().toString()),
          objectMapper.writeValueAsString(TuitDto.desdeTuit(t)));
      } catch (JsonProcessingException e) {
        log.warn("No se pudo cachear tuit {}", t.getId(), e);
      }
    });
  }

  private record ResultadoJsonProcessing(List<Tuit> tuits, List<UUID> idsFaltantes) {
    public boolean existenIdsFaltantes() {
      return !idsFaltantes.isEmpty();
    }
  }
}
