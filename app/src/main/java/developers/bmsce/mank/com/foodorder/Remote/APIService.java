package developers.bmsce.mank.com.foodorder.Remote;

import developers.bmsce.mank.com.foodorder.Model.MyResponse;
import developers.bmsce.mank.com.foodorder.Model.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {


    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAfc5bUc0:APA91bEsoMJOK2nn1LJipSic8CNObINP06I0kYoZVZx9Nbw_FdQSBk2utKv68UlCE4z3w75bYkLoAlnXz0alKdV3wclhKbFVTahJ5Kf95yI-aslGhPB3rwXXZnVmRrVGP5-jJxfRLgh8"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
