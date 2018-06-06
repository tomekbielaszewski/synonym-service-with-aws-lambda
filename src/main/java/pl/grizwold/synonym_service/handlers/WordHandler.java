package pl.grizwold.synonym_service.handlers;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import pl.grizwold.synonym_service.ApiGatewayResponse;
import pl.grizwold.synonym_service.service.SynonymService;

public class WordHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	private static final Logger log = Logger.getLogger(WordHandler.class);

	private SynonymService synonymService = new SynonymService();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		String word = (String) input.get("body");
		long currentTime = System.currentTimeMillis();

		String synonym = synonymService.getSynonymWord(word);
		log.info("Calling external API took(ms): " + (System.currentTimeMillis() - currentTime));

		log.info("Word \"" + word + "\" transformed to \"" + synonym + "\"");

		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(synonym)
				.build();
	}
}
