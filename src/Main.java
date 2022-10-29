import java.sql.SQLException;

public class Main {
    public static void main(String[] args)throws ClassNotFoundException, SQLException {
        Connect.Conn();
        Connect.CreateDB();
        Connect.insert_type("Абиссинская кошка");
        Connect.insert_type("Австралийский мист");
        Connect.insert_type("Американская жесткошерстная");
        Connect.ReadDB();
        Connect.CloseDB();
    }
}