package developers.bmsce.mank.com.foodorder.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.ListView;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import developers.bmsce.mank.com.foodorder.Model.Order;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "order.db";
    private static final int DB_VER= 1;




    public Database(Context context, String name, String storageDirectory, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, storageDirectory, factory, version);
    }

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {



        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qp = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductId","ProductName","Quantity","Price","Discount"};
        String sqlTable = "OrderDetail";

        qp.setTables(sqlTable);
        Cursor c = qp.query(db, sqlSelect, null, null, null, null, null);
        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst()){


            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                ));


            } while (c.moveToNext());


        }
return result;

    }

    public void addToCart(Order order){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());

        db.execSQL(query);


    }

    public void cleanCart(){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");

        db.execSQL(query);


    }

    //favorites

    public void addToFavorites(String foodID) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(FoodId) VALUES('%s');",foodID);
        db.execSQL(query);

    }

    public void removeFromFavorites(String foodID) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FoodId='%s';",foodID);
        db.execSQL(query);

    }

    public boolean isFavorites(String foodID) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE FoodId='%s';",foodID);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() < 0) {

            cursor.close();
            return false;
        }
       cursor.close();
        return true;

    }
}
