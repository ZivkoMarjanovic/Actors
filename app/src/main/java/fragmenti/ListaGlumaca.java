package fragmenti;

import android.app.Activity;
import android.app.Fragment;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.zivko.glumci.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import db.DataBaseHelper;
import objects.Glumac;

/**
 * Created by Živko on 2016-10-25.
 */

public class ListaGlumaca extends Fragment implements AdapterView.OnItemClickListener{

    DataBaseHelper dataBaseHelper;
    Communicator comm;
    ListView listaGlumaca;
    List<Glumac> glumci;
//jgsxggwgx

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("ZIL", "LISTA onCreateView");
        if (getActivity().findViewById(R.id.drugi) == null){setHasOptionsMenu(true);}
        return inflater.inflate(R.layout.lista_glumaca, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("ZIL", "LISTA onACTIVITYCREATED");
        super.onActivityCreated(savedInstanceState);

        listaGlumaca = (ListView) getActivity().findViewById(R.id.listaGlumaca);

        try {
            glumci = getDatabaseHelper().getGlumacDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (glumci != null) {

            ListAdapter adapter = new ArrayAdapter<Glumac>(getActivity(), android.R.layout.simple_list_item_1, glumci);
            listaGlumaca.setAdapter(adapter);
            listaGlumaca.setOnItemClickListener(this);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Glumac glumac = (Glumac) listaGlumaca.getItemAtPosition(position);


        comm.respond(glumac.getId());
    }

    public interface Communicator{
        public void respond(int position);
    }

    public DataBaseHelper getDatabaseHelper() {
        if (dataBaseHelper == null) {
            dataBaseHelper = OpenHelperManager.getHelper(getActivity(), DataBaseHelper.class);
        }
        return dataBaseHelper;
    }

    @Override
    public void onDestroy() {
        Log.d("ZIL", "LISTA onDESTROY");
        super.onDestroy();

        if (dataBaseHelper != null) {
            OpenHelperManager.releaseHelper();
            dataBaseHelper = null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        comm= (Communicator) activity;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.activity_item_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        Log.d("ZIL", "LISTA onPAUSE");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("ZIL", "LISTA onSTOP");
        super.onStop();
    }


}
