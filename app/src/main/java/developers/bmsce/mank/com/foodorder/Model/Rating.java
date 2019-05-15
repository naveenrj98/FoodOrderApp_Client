package developers.bmsce.mank.com.foodorder.Model;

public class Rating {


    private  String userPhone,foodId,ratevalue,comment;


    public Rating() {
    }

    public Rating(String userPhone, String foodId, String ratevalue, String comment) {
        this.userPhone = userPhone;
        this.foodId = foodId;
        this.ratevalue = ratevalue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRatevalue() {
        return ratevalue;
    }

    public void setRatevalue(String ratevalue) {
        this.ratevalue = ratevalue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "userPhone='" + userPhone + '\'' +
                ", foodId='" + foodId + '\'' +
                ", ratevalue='" + ratevalue + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
