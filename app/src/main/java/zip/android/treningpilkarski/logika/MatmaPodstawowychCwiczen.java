package zip.android.treningpilkarski.logika;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.Random;

/**
 * ZROBIĆ randomowo dodawnaie do powotorzen takie sztuczne przeszkoczenie
 *
 * */

public class MatmaPodstawowychCwiczen {

    private int powtorzenia;
    private int wczesniejsze_powtorzenia;
    private int powtorzenia_do_wykonania;
    private int rozmiar_tablicy;
    private int[] powtorzenia_seria_5;
    private int[] powtorzenia_seria_6;
    private int[] powtorzenia_seria_7;
    private int[] powtorzenia_seria_8;
    private int[] powtorzenia_seria_9;
    private int[] powtorzenia_seria_10;
    public int seria[];
    public MatmaPodstawowychCwiczen() {
        powtorzenia_seria_5 = new int[5];
        powtorzenia_seria_6 = new int[6];
        powtorzenia_seria_7 = new int[7];
        powtorzenia_seria_8 = new int[8];
        powtorzenia_seria_9 = new int[9];
        powtorzenia_seria_10 = new int[10];
    }

    public int set_powtorzenia(int set) {
        return powtorzenia = set;
    }

    public int getPowtorzenia_do_wykonania(){
        return powtorzenia_do_wykonania;
    }

    public int getRozmiar_tablicy(){
        return rozmiar_tablicy;
    }
    private int[] losuj_serie_pompki(int seria[]) {
        double CONSTANCE = 1.2;
        int temp = powtorzenia; // powiedzmy ze zrobil 120
        /**
         * opracowanie matmy sratmy 120/5 => .. seria[0] = 24 => 24 seria[1] =
         * 24 => 26 seria[2] = 24 => 21 seria[3] = 24 => 21 seria[4] = 24 => 28
         * */
        if (powtorzenia > 10) {

            if (seria.length == 5) {
                // int temp_5[] = new int [5];

                seria[0] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 2;
                seria[1] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 1;
                seria[2] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 4;
                seria[3] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 4;
                seria[4] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE);

            } else if (seria.length == 8) {
                // int temp_8[] = new int [8];
                seria[0] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 4;
                seria[1] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 1;
                seria[2] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
                seria[3] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
                seria[4] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 5;
                seria[5] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 5;
                seria[6] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 6;
                seria[7] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) + 10;
            } else if (seria.length == 9) {
                seria[0] = 30;
                seria[1] = 30;
                seria[2] = 45;
                seria[3] = 45;
                seria[4] = 32;
                seria[5] = 32;
                seria[6] = 28;
                seria[7] = 25;
                seria[8] = 60;
            }
        } else {
            /** Dane na sztywno gdy ilosc powtorzen mniejsza od 10 */
            // int seria[] = new int [5];
            seria[0] = 5;
            seria[1] = 6;
            seria[2] = 4;
            seria[3] = 4;
            seria[4] = 8;
        }
        return seria;
    }

    private int[] losuj_serie_przysiady(int seria[]) {
        double CONSTANCE = 1.4;
        int temp = powtorzenia; // powiedzmy ze zrobil 120
        /**
         * opracowanie matmy sratmy 120/5 => .. seria[0] = 24 => 24 seria[1] =
         * 24 => 26 seria[2] = 24 => 21 seria[3] = 24 => 21 seria[4] = 24 => 28
         * */
        if (powtorzenia > 20) {
            if (seria.length == 5) {
                // int temp_5[] = new int [5];

                seria[0] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 1;
                seria[1] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 2;
                seria[2] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
                seria[3] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 4;
                seria[4] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) + 2;

            } else if (seria.length == 7) {
                // temp_7[] = new int [7];
                seria[0] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 2;
                seria[1] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE);
                seria[2] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
                seria[3] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
                seria[4] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 5;
                seria[5] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 5;
                seria[6] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) + 5;

            }
        } else {
            /** Dane na sztywno gdy ilosc powtorzen mniejsza od 10 */
            // int seria[] = new int [5];
            seria[0] = 8;
            seria[1] = 8;
            seria[2] = 6;
            seria[3] = 6;
            seria[4] = 10;
        }
        return seria;
    }

    private int[] losuj_serie_brzuszki(int seria[]) {
        double CONSTANCE = 1.1;
        int temp = powtorzenia; // powiedzmy ze zrobil 120
        /**
         * opracowanie matmy sratmy 120/5 => .. seria[0] = 24 => 24 seria[1] =
         * 24 => 26 seria[2] = 24 => 21 seria[3] = 24 => 21 seria[4] = 24 => 28
         * */
        if (powtorzenia > 10) {
            if (seria.length == 6) {
                // int temp_5[] = new int [5];

                seria[0] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 1;
                seria[1] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 2;
                seria[2] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
                seria[3] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
                seria[4] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 5;
                seria[5] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) + 5;
            }
            if (seria.length == 8) {
                // int temp_7[] = new int [7];
                seria[0] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 1;
                seria[1] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE);
                seria[2] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
                seria[3] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
                seria[4] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 4;
                seria[5] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 5;
                seria[6] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 5;
                seria[7] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) + 5;

            } else if (seria.length == 10) {
                seria[0] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
                seria[1] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE);
                seria[2] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE);
                seria[3] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 4;
                seria[4] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 4;
                seria[5] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 5;
                seria[6] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 5;
                seria[7] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 5;
                seria[8] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) + 1;
                seria[9] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) + 10;
            }
        } else {
            /** Dane na sztywno gdy ilosc powtorzen mniejsza od 10 */
            // int seria[] = new int [5];
            seria[0] = 2;
            seria[1] = 3;
            seria[2] = 3;
            seria[3] = 2;
            seria[4] = 2;
            seria[5] = 4;
        }
        return seria;
    }

    private int[] losuj_serie_drazek(int seria[]) {
        double CONSTANCE = 1.1;
        int temp = powtorzenia; // powiedzmy ze zrobil 120
        /**
         * opracowanie matmy sratmy 120/5 => .. seria[0] = 24 => 24 seria[1] =
         * 24 => 26 seria[2] = 24 => 21 seria[3] = 24 => 21 seria[4] = 24 => 28
         * */
        if (powtorzenia >= 4) {

            // int temp_5[] = new int [5];

            seria[0] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 2;
            seria[1] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 1;
            seria[2] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
            seria[3] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) - 3;
            seria[4] = (int) (Math.log1p(temp) * Math.log1p(temp) * CONSTANCE) + 1;

        } else {
            /** Dane na sztywno gdy ilosc powtorzen mniejsza od 10 */
            // int seria[] = new int [5];
            seria[0] = 4;
            seria[1] = 9;
            seria[2] = 6;
            seria[3] = 6;
            seria[4] = 9;
        }
        return seria;
    }

    /** To jest stare nie modyfikowane */
    private void inicjuj_dzien(int ktory, int[] seria) {
        ktory = ktory % 6 + 1;
        System.out.print("Dzien " + ktory);
        for (int i = 0; i < seria.length; i++) {
            switch (ktory) {
                case 1:
                    seria[i] += 1;
                    break;
                case 2:
                    seria[i] += 2;
                    break;
                case 3:
                    seria[i] += 3;
                    break;
                case 4:
                    seria[i] += 4;
                    break;
                case 5:
                    seria[i] += 5;
                    break;
                case 6:
                    seria[i] += 7;
                    break;
                default:
                    break;
            }
        }// for
    }

    private int[] oblicz(int rodzaj_cwiczenia, int iloscPowtorzen,int wczesniejsze_powtorzenia ) {

        Random rand = new Random();
        switch (rodzaj_cwiczenia) {

            case 1:
                if(wczesniejsze_powtorzenia<powtorzenia){
                    set_powtorzenia((int) (Math.sqrt(iloscPowtorzen)
                            * Math.log1p(iloscPowtorzen) * 3));
                }
                else{
                    set_powtorzenia((int) (Math.sqrt(iloscPowtorzen)
                            * Math.log1p(iloscPowtorzen) * 3)+rand.nextInt(20) );
                }
                if (powtorzenia >= 290) {
                    seria = new int[9];
                    seria = losuj_serie_pompki(powtorzenia_seria_9);
                    // inicjuj_dzien(dzien,powtorzenia_seria_9);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                } else if (powtorzenia > 141) { // pompki
                    seria = new int[8];
                    seria = losuj_serie_pompki(powtorzenia_seria_8);
                    // inicjuj_dzien(dzien,powtorzenia_seria_8);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                } else {
                    seria = new int[5];
                    seria = losuj_serie_pompki(powtorzenia_seria_5);
                    // inicjuj_dzien(dzien,powtorzenia_seria_5);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                }
                return seria;

            case 2:
                if(wczesniejsze_powtorzenia<powtorzenia){
                    set_powtorzenia((int) (Math.sqrt(iloscPowtorzen)
                            * Math.log1p(iloscPowtorzen) * 5));
                }
                else{
                    set_powtorzenia((int) (Math.sqrt(iloscPowtorzen)
                            * Math.log1p(iloscPowtorzen) * 5)+rand.nextInt(20) );
                }
                if (powtorzenia > 490) {
                    seria = new int[7];
                    seria = koniec_przysiady(seria);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                } else if (powtorzenia > 301) { // przysaidy
                    seria = new int[7];
                    seria = losuj_serie_przysiady(powtorzenia_seria_7);
                    // inicjuj_dzien(dzien,powtorzenia_seria_7);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                } else {
                    seria = new int[5];
                    seria = losuj_serie_przysiady(powtorzenia_seria_5);
                    // inicjuj_dzien(dzien,powtorzenia_seria_5);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                }
                return seria;

            case 3:
                if(wczesniejsze_powtorzenia<powtorzenia){
                    set_powtorzenia((int) (Math.sqrt(iloscPowtorzen)
                            * Math.log1p(iloscPowtorzen) * 4));
                }
                else{
                    set_powtorzenia((int) (Math.sqrt(iloscPowtorzen)
                            * Math.log1p(iloscPowtorzen) * 4)+rand.nextInt(20) );
                }
                if (powtorzenia >= 430) {
                    seria = new int[10];
                    seria = koniec_brzuszki(seria);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                } else if (powtorzenia > 300) { // brzuszki
                    seria = new int[10];
                    seria = losuj_serie_brzuszki(powtorzenia_seria_10);
                    // inicjuj_dzien(dzien,powtorzenia_seria_10);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                } else if (powtorzenia > 230) {
                    seria = new int[8];
                    seria = losuj_serie_brzuszki(powtorzenia_seria_8);
                    // inicjuj_dzien(dzien,powtorzenia_seria_8);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                } else {
                    seria = new int[6];
                    seria = losuj_serie_brzuszki(powtorzenia_seria_6);
                    // inicjuj_dzien(dzien,powtorzenia_seria_6);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                }
                return seria;

            case 4:
                if(wczesniejsze_powtorzenia<powtorzenia){
                    set_powtorzenia((int) (Math.sqrt(iloscPowtorzen)
                            * Math.log1p(iloscPowtorzen) * 1.6));
                }
                else{
                    set_powtorzenia((int) (Math.sqrt(iloscPowtorzen)
                            * Math.log1p(iloscPowtorzen) * 1.6)+rand.nextInt(20) );
                }
                // drazek
                if (powtorzenia > 160) {
                    seria = new int[5];
                    seria = koniec_drazek(seria);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                } else {
                    seria = new int[5];
                    seria = losuj_serie_drazek(powtorzenia_seria_5);
                    powtorzenia_do_wykonania = zliczPowtorzenia(seria);
                    rozmiar_tablicy = seria.length;
                }
                // inicjuj_dzien(dzien,powtorzenia_seria_5);

                return seria;
        }
        return null;
    }

    private int[] koniec_przysiady(int[] seria) {
        seria[0] = 70;
        seria[1] = 70;
        seria[2] = 70;
        seria[3] = 70;
        seria[4] = 75;
        seria[5] = 70;
        seria[6] = 80;
        return seria;
    }

    private int[] koniec_drazek(int[] seria) {
        seria[0] = 33;
        seria[1] = 38;
        seria[2] = 30;
        seria[3] = 28;
        seria[4] = 40;
        return seria;
    }

    private int[] koniec_brzuszki(int[] seria) {
        seria[0] = 38;
        seria[1] = 44;
        seria[2] = 44;
        seria[3] = 44;
        seria[4] = 44;
        seria[5] = 42;
        seria[6] = 42;
        seria[7] = 40;
        seria[8] = 40;
        seria[9] = 46;
        return seria;
    }



    public void run(int rodzaj_cwiczenia,int ilePowtorzen, int wczesniejsze_powtorzenia) {
        //int powtorzenia = wpisz();
        // set_powtorzenia(ilePowtorzen);
        this.wczesniejsze_powtorzenia = wczesniejsze_powtorzenia;
       oblicz(rodzaj_cwiczenia, ilePowtorzen, wczesniejsze_powtorzenia);
    }

    public int przerobPowtorzenia(int ilePowtorzen) {
        // ilePowtorzen = (int) Math.sqrt(ilePowtorzen);
        ilePowtorzen = (int) Math.exp(ilePowtorzen);

        return ilePowtorzen;
    }

    private void drukuj(int[] seria) {
        int razem = 0;
        System.out.print(" Seria: ");
        for (int i = 0; i < seria.length; i++) {
            System.out.print(" " + seria[i]);
            razem += seria[i];
        }
        System.out.print(" -> razem/ " + razem + " ! ");
        System.out.print(razem / 5);
        System.out.println();
        razem = 0;
    }

    private int zliczPowtorzenia(int[] seria) {
        int wynik = 0;
        for (int element : seria) {
            wynik += element;
        }
        return wynik;

    }

}
