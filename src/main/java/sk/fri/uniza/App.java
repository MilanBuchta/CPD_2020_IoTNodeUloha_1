package sk.fri.uniza;


import retrofit2.Call;
import retrofit2.Response;
import sk.fri.uniza.model.Location;
import sk.fri.uniza.model.SaveDataRequest;
import sk.fri.uniza.model.SaveDataResponse;
import sk.fri.uniza.model.WeatherData;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.*;

/**
 * Hello IoT!
 */
public class App {
    public static void main(String[] args) {
        IotNode iotNode = new IotNode();
        SaveData saveData = new SaveData();
        // Vytvorenie požiadavky na získanie údajov o aktuálnom počasí z
        // meteo stanice s ID: station_1


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Call<Map<String, String>> currentWeather =
                        iotNode.getWeatherStationService()
                                .getCurrentWeatherAsMap("station_1",
                                        List.of("totalRain", "humidity","date","time",
                                                "airTemperature"));


                try {
                    // Odoslanie požiadavky na server pomocou REST rozhranie
                    Response<Map<String, String>> response = currentWeather.execute();

                    if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                        //Získanie údajov vo forme Mapy stringov
                        Map<String, String> body = response.body();
                        System.out.println(body);

                        String fullDate= body.get("Date").replace(".","/") +" "+ body.get("Time");

                        SaveDataRequest request = new SaveDataRequest();
                        request.setDateTime(fullDate);
                        request.setValue(body.get("Air Temperature"));
                        request.setType("double");

//                        System.out.println(request.getDateTime());
//                        System.out.println(request.getValue());
//                        System.out.println(request.getType());

                        SaveDataRequest request2 = new SaveDataRequest();
                        request2.setDateTime(fullDate);
                        request2.setValue(body.get("Humidity"));
                        request2.setType("integer");

                        SaveDataRequest request3 = new SaveDataRequest();
                        request3.setDateTime(fullDate);
                        request3.setValue(body.get("Total Rain"));
                        request3.setType("integer");

                        Call<SaveDataResponse> saveDataResponseCall = saveData.getSaveDataService().saveData("1","airTemp",request);


                        try {
                            Response<SaveDataResponse> saveDataResponseResponse = saveDataResponseCall.execute();

                            if (saveDataResponseResponse.isSuccessful()) { // Dotaz na server bol neúspešný
                                //Získanie údajov vo forme Mapy stringov
                                SaveDataResponse result2 = saveDataResponseResponse.body();
                                System.out.println(result2.getId());
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        Call<SaveDataResponse> saveDataResponseCall2 = saveData.getSaveDataService().saveData("1","windSpeed",request2);

                        try {
                            Response<SaveDataResponse> saveDataResponseResponse2 = saveDataResponseCall2.execute();

                            if (saveDataResponseResponse2.isSuccessful()) { // Dotaz na server bol neúspešný
                                //Získanie údajov vo forme Mapy stringov
                                SaveDataResponse result3 = saveDataResponseResponse2.body();
                                System.out.println(result3.getId());
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }


                        Call<SaveDataResponse> saveDataResponseCall3 = saveData.getSaveDataService().saveData("1","weather",request3);

                        try {
                            Response<SaveDataResponse> saveDataResponseResponse3 = saveDataResponseCall3.execute();

                            if (saveDataResponseResponse3.isSuccessful()) { // Dotaz na server bol neúspešný
                                //Získanie údajov vo forme Mapy stringov
                                SaveDataResponse result4 = saveDataResponseResponse3.body();
                                System.out.println(result4.getId());
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, 100, 10000);//wait 0 ms before doing the action and do it evry 1000ms (1second)




//        // Vytvorenie požiadavky na získanie údajov o všetkých meteo staniciach
//        Call<List<Location>> stationLocations =
//                iotNode.getWeatherStationService().getStationLocations();
//
//        try {
//            Response<List<Location>> response = stationLocations.execute();
//
//            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
//                //Získanie údajov vo forme Zoznam lokacií
//                List<Location> body = response.body();
//
//                System.out.println(body);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        // Vytvorenie požiadavky na získanie údajov o aktuálnom počasí z
//        // meteo stanice s ID: station_1
//        Call<WeatherData> currentWeatherPojo =
//                iotNode.getWeatherStationService()
//                        .getCurrentWeather("station_1");
//
//
//        try {
//            // Odoslanie požiadavky na server pomocou REST rozhranie
//            Response<WeatherData> response = currentWeatherPojo.execute();
//
//            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
//                //Získanie údajov vo forme inštancie triedy WeatherData
//                WeatherData body = response.body();
//                System.out.println(body);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        // Vytvorenie požiadavky na získanie údajov o všetkých meteo staniciach
//        Call<List<WeatherData>> historyData =
//                iotNode.getWeatherStationService().getHistoryWeather("station_1","19/01/2021 15:00","21/01/2021 15:00",  List.of("time", "date",
//                        "airTemperature"));
//
//        try {
//            Response<List<WeatherData>> response = historyData.execute();
//
//            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
//                //Získanie údajov vo forme Zoznam lokacií
//                List<WeatherData> body = response.body();
//
//                System.out.println(body);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        Optional<Double> data = iotNode.getAverageTemperature("station_1","19/01/2021 15:00","21/01/2021 15:00");
//        data.ifPresent(vysledok -> System.out.println(vysledok));



    }
}
