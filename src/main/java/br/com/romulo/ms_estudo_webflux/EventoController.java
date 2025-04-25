package br.com.romulo.ms_estudo_webflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/eventos")
public class EventoController {
	 private final EventoService servico;
	 
	 private final Sinks.Many<EventoDto> eventoSink;// um ponto de entrada para enviar o novo evento cadastrado e unificá-lo à fonte de dados que estamos enviando para o cliente.
      //para utilizarmos o sink, deixamos como final e removemos autowride
	 
	  public EventoController(EventoService servico) {
	        this.servico = servico;
	        this.eventoSink = Sinks.many().multicast().onBackpressureBuffer();
	        //sincronizaremos vários novos eventos para o cliente. Quando usamos a palavra multicast,
	        //estamos dizendo que ele fará um broadcast, enviando para todos os clientes conectados. Se várias pessoas estiverem conectadas ao servidor, 
	        //ele enviará a mesma propagação de mudanças para todos, o que é necessário no caso da fila de ingressos
	        
	        //O multicast significa que enviará para todos os novos clientes conectados.
	        
	        //Muitas vezes, o poder de envio do servidor é maior do que o poder de consumo do cliente. Com o WebFlux, conseguimos controlar na programação reativa como isso acontecerá de forma equilibrada, evitando sobrecarga.
	    }


	@GetMapping //(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDto> obterTodos() {
        return servico.obterTodos();
    }
	
	@GetMapping(value = "/categoria/{tipo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDto> obterPorTipo(@PathVariable String tipo) {
        return Flux.merge(servico.obterPorTipo(tipo), eventoSink.asFlux())   //merge ele mistura o que enviava com o sink
                .delayElements(Duration.ofSeconds(4)); //delay de 4 segundos
    }

    @GetMapping("/{id}")
    public Mono<EventoDto> obterPorId(@PathVariable Long id) {
        return servico.obterPorId(id);
    }

//    @PostMapping
//    public Mono<EventoDto> cadastrar(@RequestBody EventoDto dto) {
//        return servico.cadastrar(dto);
//    }

    @PostMapping
    public Mono<EventoDto> cadastrar(@RequestBody EventoDto dto) {
        return servico.cadastrar(dto)
                .doOnSuccess(e -> eventoSink.tryEmitNext(e));   //enviar para o sink, assim que cadastrar, se teve sucesso no cadastro, enviar para o sink
    }
    
    @DeleteMapping("/{id}")
    public Mono<Void> excluir(@PathVariable Long id) {
        return servico.excluir(id);

    }

    @PutMapping("/{id}")
    public Mono<EventoDto> alterar(@PathVariable Long id, @RequestBody EventoDto dto){
        return servico.alterar(id, dto);
    }
}
