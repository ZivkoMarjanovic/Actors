package objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static objects.Film.FILM_TABLE_NAME;

/**
 * Created by Živko on 2016-10-25.
 */
@DatabaseTable(tableName = FILM_TABLE_NAME )
public class Film {

    public static final String FILM_TABLE_NAME = "film";
    public static final String ID_FILM = "id";
    public static final String NAME_FILM = "name";
    public static final String GODINA_FILM = "godina";
    public static final String GLUMAC_ID = "glumac";

    @DatabaseField(generatedId = true, columnName = ID_FILM)
    int id;

    @DatabaseField(columnName = NAME_FILM)
    String name;

    @DatabaseField(columnName = GODINA_FILM)
    int godina;

    @DatabaseField(columnName = GLUMAC_ID, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Glumac glumac;

    public Film() {
    }

    public Film(int id, String name, int godina) {
        this.id = id;
        this.name = name;
        this.godina = godina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGodina() {
        return godina;
    }

    public void setGodina(int godina) {
        this.godina = godina;
    }

    public Glumac getGlumac() {
        return glumac;
    }

    public void setGlumac(Glumac glumac) {
        this.glumac = glumac;
    }

    @Override
    public String toString() {
        return name;
    }
}
