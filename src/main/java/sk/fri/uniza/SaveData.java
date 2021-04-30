package sk.fri.uniza;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sk.fri.uniza.api.SaveDataService;
import sk.fri.uniza.api.WeatherStationService;

public class SaveData {
    private final Retrofit retrofit;
    private final SaveDataService saveDataService;

    public SaveData() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        // Vytvorenie inštancie komunikačného rozhrania
        saveDataService = retrofit.create(SaveDataService.class);

    }

    public SaveDataService getSaveDataService() {
        return saveDataService;
    }
}
