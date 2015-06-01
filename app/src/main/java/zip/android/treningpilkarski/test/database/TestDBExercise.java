package zip.android.treningpilkarski.test.database;

public class TestDBExercise
{
    private int id;
    private String name;
    private String opis;
    private Object obrazek;     //TODO jak przechowujemy obrazek?

    TestDBExercise(int id, String name, String opis, Object obrazek)
    {
        this.id = id;
        this.name = name;
        this.opis = opis;
        this.obrazek = obrazek;
    }

    TestDBExercise(int id, String name, String opis)
    {
        this(id, name, opis, null);
    }

    public int getIdExercise()
    {
        return id;
    }

    public String getName()
    {
        return name;

    }

    public String getOpis()
    {
        return opis;
    }

    public Object getObrazek()
    {
        return obrazek;
    }
}
