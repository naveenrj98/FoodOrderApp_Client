package developers.bmsce.mank.com.foodorder.Model;

import java.util.List;

public class MyResponse {

    public long multicast_id;
    public int success;
    public int failure;
    public int canonical_ids;
    public List<Result> results;
}
