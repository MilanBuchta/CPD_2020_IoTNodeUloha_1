package sk.fri.uniza;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sk.fri.uniza.api.WeatherStationService;
import sk.fri.uniza.model.WeatherData;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class IotNode {
    private final Retrofit retrofit;
    private final WeatherStationService weatherStationService;

    public IotNode() {

        retrofit = new Retrofit.Builder()
                // Url adresa kde je umietnená WeatherStation služba
                .baseUrl("http://localhost:9000/")
                // Na konvertovanie JSON objektu na java POJO použijeme
                // Jackson knižnicu
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        // Vytvorenie inštancie komunikačného rozhrania
        weatherStationService = retrofit.create(WeatherStationService.class);

    }

    public WeatherStationService getWeatherStationService() {
        return weatherStationService;
    }

    public Optional<Double> getAverageTemperature(String station, String from, String to) {
        Call<List<WeatherData>> historyData =
                weatherStationService.getHistoryWeather(station,from,to,  List.of("airTemperature"));

        try {
            Response<List<WeatherData>> response = historyData.execute();

            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                //Získanie údajov vo forme Zoznam lokacií
                List<WeatherData> data = response.body();
                 System.out.println(data);
                //data.stream().mapToDouble(historyData -> historyData.getAirTemperature().doubleValue()).sum();

                return Optional.of(data.stream().mapToDouble(weatherData -> weatherData.getAirTemperature().doubleValue()).sum()/data.size());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
