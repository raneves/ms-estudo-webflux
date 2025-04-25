package br.com.romulo.ms_estudo_webflux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface EventoRepository extends ReactiveCrudRepository<Evento, Long>{
	Flux<Evento> findByTipo(TipoEvento tipoEvento);
	
	//CrudRepository é a interface mais básica do Spring Data e fornece métodos genéricos para operações CRUD (Create, Read, Update, Delete). É o ponto de partida para criar repositórios para qualquer entidade de domínio.
    //ReactiveCrudRepository é parte do Spring Data Reactive, proporcionando operações CRUD de maneira reativa. É especialmente útil para aplicações que precisam de alta escalabilidade e baixa latência.
	//PagingAndSortingRepository estende CrudRepository e adiciona funcionalidades para paginação e ordenação, sendo muito útil quando é preciso lidar com grandes conjuntos de dados.
	//JpaRepository estende PagingAndSortingRepository e adiciona métodos específicos do JPA, fornecendo uma interface mais rica para interagir com bancos de dados relacionais usando JPA.
}
