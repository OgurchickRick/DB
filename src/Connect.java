import com.sun.tools.javac.Main;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Connect {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    static String[] names = {"Гарфилд", "Том", "Гудвин", "Рокки", "Ленивец",
            "Пушок", "Спорти", "Бегемот", "Пират", "Гудини",
            "Зорро", "Саймон", "Альбус", "Базилио", "Леопольд",
            "Нарцисс", "Атос", "Каспер", "Валлито", "Оксфорд",
            "Бисквит", "Соня", "Клеопатра", "Цунами", "Забияка",
            "Матильда", "Кнопка", "Масяня", "Царапка", "Серсея",
            "Ворсинка", "Амели", "Наоми", "Маркиза", "Изольда",
            "Вальс", "Несквик", "Златан", "Баскет", "Изюм",
            "Цукат", "Мокко", "Месси", "Кокос", "Адидас",
            "Бейлиз", "Тайгер", "Зефир", "Мохи", "Валенсия",
            "Баунти", "Свити", "Текила", "Ириска", "Карамель",
            "Виски", "Кукуруза", "Гренка", "Фасолька", "Льдинка",
            "Китана", "Офелия", "Дайкири", "Брусника", "Аватар",
            "Космос", "Призрак", "Изумруд", "Плинтус", "Яндекс",
            "Крисмас", "Метеор", "Оптимус", "Смайлик", "Цельсий",
            "Краска", "Дейзи", "Пенка", "Веста", "Астра",
            "Эйприл", "Среда", "Бусинка", "Гайка", "Елка",
            "Золушка", "Мята", "Радость", "Сиам", "Оскар",
            "Феликс", "Гарри", "Байрон", "Чарли", "Симба",
            "Тао", "Абу", "Ватсон", "Енисей", "Измир", "Кайзер",
            "Васаби", "Байкал", "Багира", "Айрис", "Диана",
            "Мими", "Сакура", "Индия", "Тиффани", "Скарлетт",
            "Пикси", "Лиззи", "Алиса", "Лило", "Ямайка",
            "Пэрис", "Мальта", "Аляска"
    };

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
        statmt.execute("CREATE TABLE if not exists 'types' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'type' text NOT NULL UNIQUE);");
        statmt.execute("CREATE TABLE if not exists 'cats' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text NOT NULL, 'type_id' INTEGER NOT NULL, 'age' INTEGER NOT NULL, 'weight' double NOT NULL, FOREIGN KEY(type_id) REFERENCES types(id));");

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
        String request = String.format("DELETE FROM 'types' WHERE id = %d;", id);
        statmt.execute(request);
    }

    public static void update_type(int id, String new_type) throws SQLException{
        String request = ("UPDATE 'types' SET type = ? WHERE id = ?");
        PreparedStatement preparedStatement = conn.prepareStatement(request);
        preparedStatement.setString(1, new_type);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }

    public static void insert_cat(String name, String type, int age, Double weight) throws SQLException {
        try {
            String request = ("INSERT INTO 'cats' ('name', 'type_id', 'age', 'weight') VALUES (?,(SELECT id FROM types WHERE type IN (?) AND type IS NOT NULL), ?, ?)");
            PreparedStatement preparedStatement = conn.prepareStatement(request);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, age);
            preparedStatement.setDouble(4, weight);
            preparedStatement.execute();
        } catch (SQLException e){
            insert_type(type);
            String request = ("INSERT INTO 'cats' ('name', 'type_id', 'age', 'weight') VALUES (?,(SELECT id FROM types WHERE type IN (?) AND type IS NOT NULL), ?, ?)");
            PreparedStatement preparedStatement = conn.prepareStatement(request);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, age);
            preparedStatement.setDouble(4, weight);
            preparedStatement.execute();
        }

    }

    public static void add_more_cats(int n) throws SQLException{
        resSet = statmt.executeQuery("SELECT * FROM types");
        ArrayList <String> type_list = new ArrayList<>();
        while(resSet.next())
        {
            String type = resSet.getString("type");
            type_list.add(type);
        }


        for (int i = 0; i < n; i++){
            String name = names[(int) (Math.random() * names.length)];
            String type = type_list.get((int) (Math.random() * type_list.size()));
            int age = (int) (Math.random() * 10);
            Double weigh = ( 1 + Math.random() * 10);
            insert_cat(name, type, age, weigh);
        }
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

    public static void get_all_cats() throws ClassNotFoundException, SQLException {
        resSet = statmt.executeQuery("SELECT * FROM cats");

        while(resSet.next())
        {
            int id = resSet.getInt("id");
            String name = resSet.getString("name");
            int type_id = resSet.getInt("type_id");
            int age = resSet.getInt("age");
            Double weight = resSet.getDouble("weight");
            System.out.println( "ID = " + id );
            System.out.println( "name = " + name );
            System.out.println( "type_id = " + type_id );
            System.out.println( "age = " + age );
            System.out.println( "weight = " + weight );
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

