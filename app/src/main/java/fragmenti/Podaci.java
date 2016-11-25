package fragmenti;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zivko.glumci.MainActivity;
import com.example.zivko.glumci.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import db.DataBaseHelper;
import objects.Film;
import objects.Glumac;

/**
 * Created by Živko on 2016-10-25.
 */

public class Podaci extends Fragment {

    DataBaseHelper dataBaseHelper;
    Glumac glumac;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("ZIL", "PODACI onCREATEVIEW");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.podaci, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("ZIL", "PODACI onACTIVITYCREATED");
        super.onActivityCreated(savedInstanceState);

       // if (getActivity().findViewById(R.id.drugi) == null && savedInstanceState == null) {
        if ( savedInstanceState == null) {

            int position = this.getArguments().getInt("POSITION", 0);
            Log.d("ZIL", "PODACI XXXXXXXXXX");

          izmena(position);
        }

    }

    public void izmena(int position) {
        try {
            glumac = getDatabaseHelper().getGlumacDao().queryForId(position);

            Log.d("ZIL", "YYYYYYYYYYYYY");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (glumac != null) {
            TextView imePrezime = (TextView) getActivity().findViewById(R.id.imePrezime);
            imePrezime.setText(glumac.getImePrezime());

            ImageView fotografija = (ImageView) getActivity().findViewById(R.id.fotografija);
            fotografija.setImageResource(glumac.getFotografija());

            TextView rodjen = (TextView) getActivity().findViewById(R.id.rodjen);
            rodjen.setText(glumac.getRodjen());

            TextView umro = (TextView) getActivity().findViewById(R.id.umro);
            rodjen.setText(String.valueOf(glumac.getUmro()));

            TextView biografija = (TextView) getActivity().findViewById(R.id.biografija);
            biografija.setText(glumac.getBigrafija());

            ListView listaFilmovi = (ListView) getActivity().findViewById(R.id.filmovi);

            ForeignCollection<Film> filmoviCollection = glumac.getFilmovi();
            List<Film> filmovi = new ArrayList<Film>();
            if (!filmoviCollection.isEmpty()) {
                for (Film f : filmoviCollection) {
                    filmovi.add(f);
                }
            }
            if (filmovi != null) {
                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, filmovi);
                listaFilmovi.setAdapter(adapter);
            }


            RatingBar ocena = (RatingBar) getActivity().findViewById(R.id.ocena);
            ocena.setRating((float) glumac.getOcena());
        }

    }





    public DataBaseHelper getDatabaseHelper() {
        if (dataBaseHelper == null) {
            dataBaseHelper = OpenHelperManager.getHelper(getActivity(), DataBaseHelper.class);
        }
        return dataBaseHelper;
    }

    @Override
    public void onDestroy() {
        Log.d("ZIL", "PODACI onDESTROY");
        if (dataBaseHelper != null) {
            OpenHelperManager.releaseHelper();
            dataBaseHelper = null;
        }
        super.onDestroy();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (getActivity().findViewById(R.id.drugi) == null) {
            inflater.inflate(R.menu.detail_fragment_menu, menu);
        } else {inflater.inflate(R.menu.menu_land, menu);}
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                try {
                    if (glumac!=null) {
                        ForeignCollection<Film> filmoviCollection = glumac.getFilmovi();
                        final List<Film> filmovi = new ArrayList<Film>();

                        if (!filmoviCollection.isEmpty()) {
                            CloseableIterator<Film> iterator = filmoviCollection.closeableIterator();

                            try {

                                while (iterator.hasNext()) {
                                    Film f = iterator.next();
                                    filmovi.add(f);
                                }
                            } finally {
                                iterator.close();
                            }

                            TransactionManager.callInTransaction(getDatabaseHelper().getConnectionSource(),
                                    new Callable<Void>() {
                                        public Void call() throws Exception {

                                            getDatabaseHelper().getFilmDAO().delete(filmovi);

                                            getDatabaseHelper().getGlumacDao().delete(glumac);

                                            showMessage("Izbrisan je glumac", glumac.getImePrezime());

                                            refreshGlumac();

                                            if (getActivity().findViewById(R.id.drugi) == null) {
                                                getActivity().onBackPressed();}
                                            else {


                                                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                                                Podaci podaci = (Podaci) getFragmentManager().findFragmentById(R.id.drugi);
                                                transaction.remove(podaci);
                                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                transaction.commit();

                                            }

                                            return null;
                                        }
                                    });

                        } else { TransactionManager.callInTransaction(getDatabaseHelper().getConnectionSource(),
                                new Callable<Void>() {
                                    public Void call() throws Exception {

                                        getDatabaseHelper().getGlumacDao().delete(glumac);

                                        showMessage("Izbrisan je glumac", glumac.getImePrezime());

                                        refreshGlumac();

                                        if (getActivity().findViewById(R.id.drugi) == null) {
                                            getActivity().onBackPressed();}
                                        else {


                                            FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                                            Podaci podaci = (Podaci) getFragmentManager().findFragmentById(R.id.drugi);
                                            transaction.remove(podaci);
                                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                            transaction.commit();

                                        }

                                        return null;
                                    }
                                });
                        }

                        /*if (!filmoviCollection.isEmpty()) {
                            for (Film f : filmoviCollection) {
                                filmovi.add(f);
                            }
                            for (Film f : filmovi) {
                                getDatabaseHelper().getFilmDAO().delete(f);
                            }
                        }
                        getDatabaseHelper().getGlumacDao().delete(glumac);*/




                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.edit:
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_add_glumac);

                    final EditText imePrezime = (EditText) dialog.findViewById(R.id.imePrezime);
                    final EditText rodjen = (EditText) dialog.findViewById(R.id.rodjen);
                    final EditText ocena = (EditText) dialog.findViewById(R.id.ocena);
                    final EditText biografija = (EditText) dialog.findViewById(R.id.biografija);

                    imePrezime.setText(glumac.getImePrezime());
                    rodjen.setText(glumac.getRodjen());
                    ocena.setText(Integer.toString(glumac.getOcena()));
                    biografija.setText(glumac.getBigrafija());


                    Button ok = (Button) dialog.findViewById(R.id.save);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {



                            glumac.setImePrezime(imePrezime.getText().toString());
                            glumac.setRodjen(rodjen.getText().toString());
                            glumac.setOcena(Integer.parseInt(ocena.getText().toString()));
                            glumac.setBigrafija(biografija.getText().toString());

                            try {
                                getDatabaseHelper().getGlumacDao().update(glumac);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }


                            showMessage("Izmenjen je glumac", glumac.getImePrezime());
                            refreshGlumac();
                            izmena(glumac.getId());
                            dialog.dismiss();
                            dialog.cancel();

                        }
                    });

                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            dialog.cancel();
                        }

                    });
                    dialog.show();

                break;

            case R.id.add_film:
                final Dialog filmDialog = new Dialog(getActivity());
                filmDialog.setContentView(R.layout.dialog_add_film);

                final EditText imeFilm = (EditText) filmDialog.findViewById(R.id.ime);
                //final EditText zanr = (EditText) filmDialog.findViewById(R.id.zanr);
                final EditText godina = (EditText) filmDialog.findViewById(R.id.godina);




                Button okFilm = (Button) filmDialog.findViewById(R.id.save);
                okFilm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Film newFilm = new Film();

                        newFilm.setName(imeFilm.getText().toString());
                        newFilm.setGodina(Integer.parseInt(godina.getText().toString()));
                        newFilm.setGlumac(glumac);


                        try {
                            getDatabaseHelper().getFilmDAO().create(newFilm);


                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        showMessage("Dodat je novi film", newFilm.getName() );
                        refreshFilm();
                        filmDialog.dismiss();
                    }
                });

                Button cancelFilm = (Button) filmDialog.findViewById(R.id.cancel);
                cancelFilm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filmDialog.dismiss();
                    }

                });
                filmDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMessage (String text, String newGlumac) {
        SharedPreferences st = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name = st.getString("message", "Toast");
        if (name.equals("Toast")) {
            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
            builder.setSmallIcon(R.drawable.ic_action_add);
            builder.setContentTitle(text);
            builder.setContentText(newGlumac);


            // Shows notification with the notification manager (notification ID is used to update the notification later on)
            NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, builder.build());
        }
    }

    private void refreshFilm() {

        ListView listview = (ListView) getActivity().findViewById(R.id.filmovi);

        if (listview != null){
            ArrayAdapter<Film> adapter = (ArrayAdapter<Film>) listview.getAdapter();

            if(adapter!= null)
            {

                    adapter.clear();

                List<Film> filmovi = null;
                try {
                    filmovi = getDatabaseHelper().getFilmDAO().queryBuilder().where().eq(Film.GLUMAC_ID, glumac).query();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                    adapter.addAll(filmovi);

                    adapter.notifyDataSetChanged();


            }
        }
    }

    private void refreshGlumac() {

        ListView listview = (ListView) getActivity().findViewById(R.id.listaGlumaca);

        if (listview != null){
            ArrayAdapter<Glumac> adapter = (ArrayAdapter<Glumac>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Glumac> list = getDatabaseHelper().getGlumacDao().queryForAll();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onPause() {
        Log.d("ZIL", "PODACI onPAUSE");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("ZIL", "PODACI onSTOP");
        super.onStop();
    }


}
