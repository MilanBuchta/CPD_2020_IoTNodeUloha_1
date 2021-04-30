package sk.fri.uniza.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sk.fri.uniza.model.SaveDataRequest;
import sk.fri.uniza.model.SaveDataResponse;

public interface SaveDataService {
    @POST("household/{householdID}/{fieldID}")
    Call<SaveDataResponse> saveData(@Path("householdID") String householdID,@Path("fieldID") String fieldID, @Body SaveDataRequest request);
}
