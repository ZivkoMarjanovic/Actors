package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import objects.Film;
import objects.Glumac;

/**
 * Created by Živko on 2016-11-17.
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String BASENAME = "glumacFilm.db";
    public static final int VERSION = 2;

    Dao<Glumac, Integer> glumacDao = null;
    Dao<Film, Integer> filmDao = null;

    public DataBaseHelper(Context context) {
        super(context, BASENAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Glumac.class);
            TableUtils.createTable(connectionSource, Film.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Glumac.class, true);
            TableUtils.dropTable(connectionSource, Film.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Glumac, Integer> getGlumacDao() throws SQLException {
        if (glumacDao == null) {
            glumacDao = getDao(Glumac.class);
        }

        return glumacDao;
    }

    public Dao<Film, Integer> getFilmDAO() throws SQLException {
        if (filmDao == null) {
            filmDao = getDao(Film.class);
        }

        return filmDao;
    }

    @Override
    public void close() {
        glumacDao = null;
        filmDao = null;

        super.close();
    }
}
