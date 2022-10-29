import java.sql.*;


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
    public static void ReadDB() throws ClassNotFoundException, SQLException {
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

    // --------Закрытие--------
    public static void CloseDB() throws ClassNotFoundException, SQLException {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("CONNECT CLOSE");
    }

}

