package pl.grizwold.synonym_service.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.Logger;
import pl.grizwold.synonym_service.ApiGatewayResponse;
import pl.grizwold.synonym_service.service.SynonymService;

import java.util.Date;
import java.util.Map;

public class SentenceHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger log = Logger.getLogger(SentenceHandler.class);

    private SynonymService synonymService = new SynonymService();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        String sentence = (String) input.get("body");
        long currentTime = System.currentTimeMillis();

        String synonymSentence = synonymService.getSynonymSentence(sentence);
        log.info("Calling external API took(ms): " + (System.currentTimeMillis() - currentTime));

        log.info("Sentence \"" + sentence + "\" transformed to \"" + synonymSentence + "\"");

        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(synonymSentence)
                .build();
    }
}
