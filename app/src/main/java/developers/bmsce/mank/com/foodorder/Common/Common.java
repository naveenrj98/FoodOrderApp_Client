package developers.bmsce.mank.com.foodorder.Common;

import developers.bmsce.mank.com.foodorder.Model.User;
import developers.bmsce.mank.com.foodorder.Remote.APIService;
import developers.bmsce.mank.com.foodorder.Remote.RetrofitClient;

public class Common {


public  static User currentUser;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMClient()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);

    }

    public static final String converCodeToStatus(String status) {

        if(status.equals("0"))
        {
            return  "Placed";
        }else if(status.equals("1")){
            return "On my Way";


        }else {

            return "Shipped";
        }


    }
}
