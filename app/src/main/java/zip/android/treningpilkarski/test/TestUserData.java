package zip.android.treningpilkarski.test;

import java.util.ArrayList;
import java.util.StringTokenizer;

import zip.android.treningpilkarski.logika.PasswordEncrypter;

/**
 * POWIEDZMY ZE DOKUMENTACJA:
 *
 * budowa tablicy cwiczenia
 *  - _is_cw[0] - calosc
 *  - _is_cw[1] - ostatnio
 *  - _is_cw[2] - ktory dzien
 *
 * pompki -     id = 0;
 * brzuszki -   id = 1;
 * przysiady -  id = 2;
 * drazek -     id = 3;
 */

public class TestUserData
{
    //dane logowania
    private String login;
    private String password;
    private String password_backup;     //dla bezpieczenstwa!

    private ArrayList<int[]> alist_exercises = new ArrayList<>();

    private int i_dzien_ogolny;

    private boolean[] zrobione_cwiczenia;

    TestUserData(String savedString)
    {
        StringTokenizer tokenizer = new StringTokenizer(savedString, "\n");
        String workingLine = tokenizer.nextToken();

        //dodanie loginu
        StringTokenizer working = new StringTokenizer(workingLine, ":");
        working.nextToken();    //pominiecie L
        login = working.nextToken();

        //dodanie hashu hasla
        workingLine = tokenizer.nextToken();
        working = new StringTokenizer(workingLine, ":");
        working.nextToken(); //pominiecie P_HASH
        password = working.nextToken();

        //dodanie plain hasla
        workingLine = tokenizer.nextToken();
        working = new StringTokenizer(workingLine, ":");
        working.nextToken(); //pominiecie P_PLAIN
        password_backup = working.nextToken();

        //dodanie cwiczen
        workingLine = tokenizer.nextToken();
        while(workingLine.substring(0, 4).equals("EXER"))
        {
            working = new StringTokenizer(workingLine, ":");
            working.nextToken();    //pomijanie EXER_NRCWICZENIA
            working = new StringTokenizer(working.nextToken(), ";");
            int howmanytokens = working.countTokens();
            int[] table = new int[howmanytokens];
            for(int i = 0; i < howmanytokens; i++)
            {
                table[i] = Integer.parseInt(working.nextToken());
            }

            alist_exercises.add(table);
            workingLine = tokenizer.nextToken();
        }//while(workingLine.substring(0, 4).equals("EXER"))

        //dodanie dnia ogolnego
        working = new StringTokenizer(workingLine, ":");
        working.nextToken(); //pominiecie DZIENOGOLNY
        i_dzien_ogolny = Integer.parseInt(working.nextToken());


    }//TestUserData(String savedString)

    TestUserData(String login, String password)
    {
        this.login = login;
        this.password = PasswordEncrypter.computeSHA256Hash(password);
        this.password_backup = password;

        zrobione_cwiczenia = new boolean[4];

        //tworzymy 4 tablice dla cwiczen
        for (int i = 0; i < 4; i++) {
            alist_exercises.add(new int[3]);
            zrobione_cwiczenia[i] = false;
        }

        //inicjalizujemy kazda z tablic cwiczen wartosciami
        for(int[] tab : alist_exercises)
        {
            tab[0] = 0;
            tab[1] = 0;
            tab[2] = 1;
        }

        i_dzien_ogolny = 0;
    }

    public void kolejnyDzienOgolny()
    {
        i_dzien_ogolny++;
    }

    public void kolejnyDzien(int idCwiczenia)
    {
        for(int i = 0; i < 4; i++)
        {
            zrobione_cwiczenia[i] = false;
        }
        alist_exercises.get(idCwiczenia)[2]++;
    }

    public boolean czyZrobione(int idCwiczenia)
    {
        return zrobione_cwiczenia[idCwiczenia];
    }

    /* GETTERS AND SETTERS */
    public String getPlaintextPassword()
    {
        return password_backup;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getDzienTreningu(int idCwiczenia)
    {
        return alist_exercises.get(idCwiczenia)[2];
    }

    public void setDzienTreningu(int idCwiczenia, int dzien)
    {
        alist_exercises.get(idCwiczenia)[2] = dzien;
    }

    //gettery i settery: cwiczenia
    public int getOstatnie(int idCwiczenia)
    {
        return alist_exercises.get(idCwiczenia)[1];
    }

    public void setOstatnie(int idCwiczenia, int ile)
    {
        //zwiekszamy calosc
        alist_exercises.get(idCwiczenia)[0] += ile;
        //ustawiamy ostatnie
        alist_exercises.get(idCwiczenia)[1] = ile;
    }

    public int getCalosc(int idCwiczenia)
    {
        return alist_exercises.get(idCwiczenia)[0];
    }

    public int getDzienOgolny()
    {
        return i_dzien_ogolny;
    }
    /* GETTERS AND SETTERS END */
    
    public String getObjectString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("L:").append(login).append("\n");
        builder.append("P_HASH:").append(password).append("\n");
        builder.append("P_PLAIN:").append(password_backup).append("\n");
        for (int index = 0; index < alist_exercises.size(); index++) {
            builder.append("EXER").append(index).append(":");
            for(int value : alist_exercises.get(index))
            {
                builder.append(value).append(";");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("\n");
        }
        builder.append("DZIENOGOLNY:").append(i_dzien_ogolny).append("\n");

        return builder.toString();
    }
}
