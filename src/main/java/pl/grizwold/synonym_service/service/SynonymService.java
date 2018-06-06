package pl.grizwold.synonym_service.service;

import pl.grizwold.synonym_service.model.DatamuseWord;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SynonymService {
    private final DatamuseAPI datamuseAPI;

    public SynonymService() {
        this.datamuseAPI = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl("https://api.datamuse.com")
                .build().create(DatamuseAPI.class);
    }

    public String getSynonymSentence(String sentence) {
        return Stream.of(sentence.split(" "))
                .parallel()
                .map(this::getSynonymWord)
                .reduce((a, b) -> a + " " + b)
                .orElse(sentence);
    }

    public String getSynonymWord(String word) {
        String synonym = "";
        try {
            synonym = datamuseAPI.getSynonyms(word)
                    .execute()
                    .body()
                    .stream()
                    .sorted(Comparator.comparing(DatamuseWord::getScore))
                    .map(DatamuseWord::getWord)
                    .filter(_word -> !_word.contains(word))
                    .filter(_word -> !_word.contains(" "))
                    .findFirst()
                    .orElse(word);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return synonym;
    }

    interface DatamuseAPI {
        @GET("/words")
        Call<List<DatamuseWord>> getSynonyms(@Query("rel_syn") String word);
    }
}
