package com.example.zivko.glumci;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import db.DataBaseHelper;
import fragmenti.ListaGlumaca;
import fragmenti.Podaci;
import objects.Glumac;

public class MainActivity extends AppCompatActivity implements ListaGlumaca.Communicator{

    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ZIL", "Main onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        ListaGlumaca listaGlumaca = new ListaGlumaca();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.prvi, listaGlumaca, "LISTA");
        ft.commit();



        /*if (findViewById(R.id.drugi) != null) {

            getFragmentManager().popBackStack();

            Podaci podaci = (Podaci) getFragmentManager().findFragmentById(R.id.drugi);
            if (podaci == null) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                podaci = new Podaci();
                transaction.replace(R.id.drugi, podaci, "PODACI");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        }*/

    }




    @Override
    public void respond(int position) {
        Log.d("ZIL", "Main RESPOND");

        Podaci mPodaci = (Podaci) getFragmentManager()
                .findFragmentById(R.id.drugi);
        if (mPodaci!=null) {
            getFragmentManager().beginTransaction().remove(mPodaci).commit();
        }
            mPodaci= new Podaci();
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", position);
            mPodaci.setArguments(bundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (findViewById(R.id.drugi) != null)
            {
            transaction.replace(R.id.drugi, mPodaci, "PODACI1");
            } else {
                transaction.replace(R.id.prvi, mPodaci, "PODACI2");
                transaction.addToBackStack(null);
            }

            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            transaction.commit();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.activity_item_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                break;

            case R.id.About:
                About about = new About();
                about.show(getSupportFragmentManager(), "ABOUT");
                break;


            case R.id.action_add:

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_add_glumac);

                final EditText imePrezime = (EditText) dialog.findViewById(R.id.imePrezime);
                final EditText rodjen = (EditText) dialog.findViewById(R.id.rodjen);
                final EditText ocena = (EditText) dialog.findViewById(R.id.ocena);
                final EditText biografija = (EditText) dialog.findViewById(R.id.biografija);



                Button ok = (Button) dialog.findViewById(R.id.save);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Glumac newGlumac = new Glumac();

                        newGlumac.setImePrezime(imePrezime.getText().toString());
                        newGlumac.setRodjen(rodjen.getText().toString());
                        newGlumac.setOcena(Integer.parseInt(ocena.getText().toString()));
                        newGlumac.setBigrafija(biografija.getText().toString());

                        try {
                            getDatabaseHelper().getGlumacDao().create(newGlumac);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        refreshGlumac();

                        showMessage("Napravljen je novi glumac", newGlumac.getImePrezime());

                        dialog.dismiss();
                    }
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }

                });
                dialog.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshGlumac() {

        ListView listview = (ListView) findViewById(R.id.listaGlumaca);

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

    public void showMessage (String text, String newGlumac) {
        SharedPreferences st = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String name = st.getString("message", "Toast");
        if (name.equals("Toast")) {
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
            builder.setSmallIcon(R.drawable.ic_action_add);
            builder.setContentTitle(text);
            builder.setContentText(newGlumac);


            // Shows notification with the notification manager (notification ID is used to update the notification later on)
            NotificationManager manager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, builder.build());
        }
    }


    public DataBaseHelper getDatabaseHelper() {
        if (dataBaseHelper == null) {
            dataBaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        }
        return dataBaseHelper;
    }

    @Override
    public void onDestroy() {
        Log.d("ZIL", "Main onDESTROY");
        super.onDestroy();

        if (dataBaseHelper != null) {
            OpenHelperManager.releaseHelper();
            dataBaseHelper = null;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Log.d("ZIL", "Main onSAVEINSTANTESTATE");
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause() {
        Log.d("ZIL", "Main onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("ZIL", "Main onSTOP");
        super.onStop();
    }


    @Override
    protected void onRestart() {
        Log.d("ZIL", "Main onRESTART");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d("ZIL", "Main onRESUME");
        super.onResume();
    }
}
