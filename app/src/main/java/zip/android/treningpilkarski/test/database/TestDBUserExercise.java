package zip.android.treningpilkarski.test.database;

import java.util.Date;
import java.util.GregorianCalendar;

public class TestDBUserExercise
{
    private int fk_idUser;
    private int fk_idExercise;

    private GregorianCalendar dzienWykonania;
    private int lacznieIle;
    private int atrybut_1;
    private int atrybut_2;
    private int atrybut_3;
    private int atrybut_4;
    private int byloPominiete;

    TestDBUserExercise(
            int user,
            int exercise,
            int lacznieIle,
            int atrybut_1,
            int atrybut_2,
            int atrybut_3,
            int atrybut_4,
            int byloPominiete,
            GregorianCalendar dzienWykonania)
    {
        this.fk_idUser = user;
        this.fk_idExercise = exercise;
        this.dzienWykonania = dzienWykonania;
        this.lacznieIle = lacznieIle;
        this.atrybut_1 = atrybut_1;
        this.atrybut_2 = atrybut_2;
        this.atrybut_3 = atrybut_3;
        this.atrybut_4 = atrybut_4;
        this.byloPominiete = byloPominiete;
    }

    TestDBUserExercise(int user, int exercise)
    {
        this(user, exercise, 0, 0, 0, 0, 0, 0, null);
    }

    public int getIdUser()
    {
        return fk_idUser;
    }

    public int getIdExercise()
    {
        return fk_idExercise;
    }


    public GregorianCalendar getDzienWykonania() {
        return dzienWykonania;
    }

    public void setDzienWykonania(GregorianCalendar dzienWykonania) {
        this.dzienWykonania = dzienWykonania;
    }

    public int getLacznieIle() {
        return lacznieIle;
    }

    public void setLacznieIle(int lacznieIle) {
        this.lacznieIle = lacznieIle;
    }

    public int getAtrybut_1() {
        return atrybut_1;
    }

    public void setAtrybut_1(int atrybut_1) {
        this.atrybut_1 = atrybut_1;
    }

    public int getAtrybut_2() {
        return atrybut_2;
    }

    public void setAtrybut_2(int atrybut_2) {
        this.atrybut_2 = atrybut_2;
    }

    public int getAtrybut_3() {
        return atrybut_3;
    }

    public void setAtrybut_3(int atrybut_3) {
        this.atrybut_3 = atrybut_3;
    }

    public int getAtrybut_4() {
        return atrybut_4;
    }

    public void setAtrybut_4(int atrybut_4) {
        this.atrybut_4 = atrybut_4;
    }

    public int getByloPominiete() {
        return byloPominiete;
    }

    public void setByloPominiete(int byloPominiete) {
        this.byloPominiete = byloPominiete;
    }
}
