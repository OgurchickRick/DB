import java.sql.SQLException;

public class Main {
    public static void main(String[] args)throws ClassNotFoundException, SQLException {
        Connect.Conn();
        Connect.CreateDB();
        Connect.WriteDB();
        Connect.ReadDB();
        Connect.CloseDB();
    }
}