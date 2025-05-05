package br.com.romulo.ms_estudo_webflux;

import java.util.List;

///"translations": [
//{
//    "detected_source_language": "EN",
//    "text": "Olá, mundo!"
//  } o json vira assim


//https://developers.deepl.com/docs   
//tem que realizar o cadastro do usuqrio para conseguir uma chave, para teste utilizar a versao freerg


///WebClient, um cliente HTTP reativo fornecido pelo Spring WebFlux. Em vez de usar a biblioteca pronta fornecida pela API, 
//utilizaremos o WebClient do próprio WebFlux, 
//que já está preparado para realizar chamadas de forma não bloqueante.

public record Traducao(List<Texto> translations) {
	public String getTexto() {
        return translations.get(0).text();
    }
}
