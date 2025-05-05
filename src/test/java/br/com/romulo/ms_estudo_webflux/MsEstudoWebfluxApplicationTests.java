package br.com.romulo.ms_estudo_webflux;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MsEstudoWebfluxApplicationTests {

	@Autowired
	private WebTestClient webTestClient;//Para construir testes numa API reativa, usaremos o Web Test Client, que o WebFlux fornece. Faremos um @Autowired para injetar o WebTestClient

	@Test
	void cadastraNovoEvento() {
		EventoDto dto = new EventoDto(null, TipoEvento.SHOW, "Católico",
				LocalDate.parse("2025-01-01"), "Show da melhor banda católica que existe");

		//para testar a requisicao
		webTestClient.post().uri("/eventos").bodyValue(dto)
				.exchange()
				.expectStatus().isCreated()//padrap 201
				.expectBody(EventoDto.class)//verifica o body deveolvido, devendo um evento dto.class, é usado para comparar os dados do evento cadastrado com os dados retornados na resposta da requisição.
				.value(response -> {
					assertNotNull(response.id());//se cadastrou e atribui o id, se foi criado o id no banco
					assertEquals(dto.tipo(), response.tipo());//verifica se o tipo do evento foi cadastrado corretamente
					assertEquals(dto.nome(), response.nome());
					assertEquals(dto.data(), response.data());
					assertEquals(dto.descricao(), response.descricao());
				});
	}

	@Test
	void buscarEvento() {
		EventoDto dto = new EventoDto(13L, TipoEvento.SHOW, "The Weeknd",
                LocalDate.parse("2025-11-02"), "Um show eletrizante ao ar livre com muitos efeitos especiais.");

		webTestClient.get().uri("/eventos")
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBodyList(EventoDto.class)
				.value(response -> {
					EventoDto eventoResponse = response.get(12);
					assertEquals(dto.id(), eventoResponse.id());
					assertEquals(dto.tipo(), eventoResponse.tipo());
					assertEquals(dto.nome(), eventoResponse.nome());
					assertEquals(dto.data(), eventoResponse.data());
					assertEquals(dto.descricao(), eventoResponse.descricao());
				});
	}


}
