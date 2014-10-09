package com.example.jhancarlos.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
//Actividad principal de la aplicacion que contiene la lista de cuestionarios
//Gracias a Marcel por su ayuda puede completar exitosamente la transferencia de datos entre activitys.
public class MainActivity extends Activity {
    //Creamos el listView
	private ListView mQListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        getActionBar().hide();
        //Añadimos los elementos que mostrara la lista
        List<String> values = new ArrayList<String>() {};
        values.add(0,"Cuestionario 1: Geografia");
        values.add(1,"Cuestionario 2: Android");
        values.add(2,"Cuestionario 3: Spring");
        values.add(3,"Cuestionario 4: Programación");
        values.add(4,"Cuestionario 5: Stucom");

		/*String[] values = new String[] { "Questionary 1: Art",
				"Questionary 2: Biology", "Questionary 3: Science",
				"Questionary 4: Programation" };*/

		// Questionary list
		mQListView = (ListView) findViewById(R.id.quiz_list);
        //Custon adapter el cual conecta el stilo de la lista, la actividad, y la lista de cuestionarios
        CustomListAdapter mQListViewAdapter = new CustomListAdapter(MainActivity.this,R.layout.my_listview_style,values);

		/*ArrayAdapter<String> mQListViewAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);*/
        //agregamos el adapter al list view
		mQListView.setAdapter(mQListViewAdapter);
        //añadimos el evento para cada iten de la lista
		mQListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //creamos un intent para hacer el cambio de activity
				Intent myIntent = new Intent(MainActivity.this,
						QuizActivity.class);
                //pasamos la posicion del cuestionario en la lisview por el intent, este lo utilizarems para
                //seleccionar el cuestionario que se mostrara en la otra vista
				myIntent.putExtra("quiz", position);
                //iniciamos la nueva activity pasandole el intent
				startActivity(myIntent);

			}

		});

		// Recover the mCurrentInex if the activity has been destroyed
		if (savedInstanceState != null) {

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Prevent data loss on rotation
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		super.onSaveInstanceState(savedInstanceState);

	}
}
