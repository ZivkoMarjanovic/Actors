package objects;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Živko on 2016-10-25.
 */
@DatabaseTable
public class Glumac {

    public static final String GLUMAC_TABLE_NAME = "glumac";
    public static final String ID_GLUMAC = "id";
    public static final String IMEPREZIME = "imePrezime";
    public static final String BIOGRAFIJA = "biografija";
    public static final String FOTOGRAFIJA = "fotografija";
    public static final String OCENA = "ocena";
    public static final String RODJEN = "rodjen";
    public static final String UMRO = "umro";
    public static final String LISTAFILMOVA = "glumac";

    @DatabaseField(generatedId = true, columnName = ID_GLUMAC)
    int id;

    @DatabaseField(columnName = IMEPREZIME)
    String imePrezime;

    @DatabaseField(columnName = BIOGRAFIJA)
    String bigrafija;

    @DatabaseField(columnName = FOTOGRAFIJA)
    int fotografija;

    @DatabaseField(columnName = OCENA)
    int ocena;

    @DatabaseField(columnName = RODJEN)
    String rodjen;

    @DatabaseField(columnName = UMRO)
    Date umro;

    @ForeignCollectionField(columnName = LISTAFILMOVA, eager = true)
    ForeignCollection<Film> filmovi;

    public Glumac() {

    }

    public Glumac(int id,String imePrezime, String bigrafija, int fotografija, int ocena, String rodjen, ForeignCollection<Film> filmovi) {
        this.id=id;
        this.imePrezime = imePrezime;
        this.bigrafija = bigrafija;
        this.fotografija = fotografija;
        this.ocena = ocena;
        this.rodjen = rodjen;
        this.filmovi = filmovi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime = imePrezime;
    }

    public String getBigrafija() {
        return bigrafija;
    }

    public void setBigrafija(String bigrafija) {
        this.bigrafija = bigrafija;
    }

    public int getFotografija() {
        return fotografija;
    }

    public void setFotografija(int fotografija) {
        this.fotografija = fotografija;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public String getRodjen() {
        return rodjen;
    }

    public void setRodjen(String rodjen) {
        this.rodjen = rodjen;
    }

    public Date getUmro() {
        return umro;
    }

    public void setUmro(Date umro) {
        this.umro = umro;
    }

    public ForeignCollection<Film> getFilmovi() {
        return filmovi;
    }

    public void setFilmovi(ForeignCollection<Film> filmovi) {
        this.filmovi = filmovi;
    }

    @Override
    public String toString() {
        return  imePrezime;
    }
}
