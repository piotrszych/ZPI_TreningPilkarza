package zip.android.treningpilkarski.test.database;

public class TestDBUser
{
    private int id;
    private String login;
    private String password;

    TestDBUser(int id, String login, String password)
    {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public int getID()
    {
        return this.id;
    }

    public String getLogin()
    {
        return login;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
