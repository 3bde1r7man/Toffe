import java.sql.*;
// import java.sql.ResultSet;
// import java.beans.Statement;
// import java.sql.Connection;
// import java.sql.Connection;
public class App {
    public static void main(String[] args) throws Exception {
        //System.out.println("Hello, World!");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:/D:\\SQLite\\sqlite-tools-win32-x86-3410200\\usersdb.db");
        String sql = "SELECT * FROM Items";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        System.out.println("category_id" + "     " + "category" + "     " +"name" + "       " +"price" + "   " );
        while(result.next())
        {
            String category_id = result.getString("category_id");
            String category = result.getString("category");
            String name = result.getString("name");
            String price = result.getString("price");
            System.out.print("\n");
            System.out.println("     "+category_id + "           " + category + "      " + name + "     " + price + " ");
            System.out.print("\n");
        }

    }
}
