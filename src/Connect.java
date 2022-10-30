import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class Connect {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void Conn() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:My_cats.s3db");

        System.out.println("CONNECT!");
    }

    // --------Создание таблицы--------
    public static void CreateDB() throws ClassNotFoundException, SQLException {

        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'types' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'type' text);");
        statmt.execute("CREATE TABLE if not exists 'cat' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'type_id' INTEGER, 'age' INTEGER, 'weight' double, FOREIGN KEY(type_id) REFERENCES types(id));");

        System.out.println("TABLE CREATE!");
    }

    // --------Заполнение таблицы--------
    public static void insert_type(String type) throws SQLException {
        String request = String.format("INSERT INTO 'types' ('type') VALUES ('%s')", type);
        statmt.execute(request);
    }

    public static void add_all_types(String[] types) throws SQLException {
        for (String type:types){
            String request = String.format("INSERT INTO 'types' ('type') VALUES ('%s')", type);
            statmt.execute(request);
        }
    }

    public static void delete_type(int id) throws SQLException{
        String request = String.format("DELETE FROM 'types' WHERE id = %s;", id);
        statmt.execute(request);
    }

    public static void update_type(int id, String new_type) throws SQLException{
        String request = ("UPDATE 'types' SET type = ? WHERE id = ?");
        PreparedStatement preparedStatement = conn.prepareStatement(request);
        preparedStatement.setString(1, new_type);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }


    // -------- Вывод таблицы--------
    public static void get_all_types() throws ClassNotFoundException, SQLException {
        resSet = statmt.executeQuery("SELECT * FROM types");

        while(resSet.next())
        {
            int id = resSet.getInt("id");
            String type = resSet.getString("type");
            System.out.println( "ID = " + id );
            System.out.println( "type = " + type );
            System.out.println();
        }

        System.out.println("TABLE INPUT");
    }

    public static String get_type(int id) throws SQLException{
        String request = "SELECT * FROM types WHERE id = ";
        request += id;
        resSet = statmt.executeQuery(request);

        return resSet.getString("type");
    }

    public static void get_type_where(String where) throws SQLException{
        String request = "SELECT * FROM types WHERE  ";
        request += where;

        resSet = statmt.executeQuery(request);

        while(resSet.next())
        {
            int id = resSet.getInt("id");
            String type = resSet.getString("type");
            System.out.println( "ID = " + id );
            System.out.println( "type = " + type );
            System.out.println();
        }

        System.out.println("TABLE INPUT");
    }


    // --------Закрытие--------
    public static void CloseDB() throws ClassNotFoundException, SQLException {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("CONNECT CLOSE");
    }

}

