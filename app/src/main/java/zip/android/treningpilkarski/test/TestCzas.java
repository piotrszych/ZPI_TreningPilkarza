package zip.android.treningpilkarski.test;

public class TestCzas
{
    private static int _i_dzien_tygodnia = 1;      //zakres 1-7
    private static int _i_dzien_treningu = 1;      //od 1 w gore

    private static int getDzienTreningu()
    {
        return _i_dzien_treningu;
    }

    private static void setDzienTreningu(int dzien)
    {
        if(dzien >=1)
        {
            _i_dzien_treningu = dzien;
        }
    }//private static void setDzienTreningu(int dzien)

    public static String dzienTygodnia()
    {
        switch(_i_dzien_tygodnia)
        {
            case 1:
                return "Poniedzialek";
            case 2:
                return "Wtorek";
            case 3:
                return "Sroda";
            case 4:
                return "Czwartek";
            case 5:
                return "Piatek";
            case 6:
                return "Sobota";
            case 7:
                return "Niedziela";
            default:
                return "Zla wartosc dnia: " + _i_dzien_tygodnia;
        }//switch(_i_dzien_tygodnia)
    }//public static String dzienTygodnia()

}//public class TestCzas
