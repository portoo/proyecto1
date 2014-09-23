package com.mobile.average;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class AgregarMateria extends Activity {
	
	private EditText input_name[];
	private EditText input_decimal[];
	private LinearLayout name_cont;
	private LinearLayout name_dec;
	private SQLiteDataBase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregarmateria);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		Initialize();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.ingreso, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
	
	public boolean AddEditTexts(boolean next)
	{
		try {
			if (input_name!=null){
				name_cont.removeAllViews();
				name_dec.removeAllViews();
			}
			EditText edit = (EditText)findViewById(R.id.Nnotas);
			int N = Integer.parseInt(edit.getText().toString());
			input_name = new EditText[N];
			input_decimal = new EditText[N];
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
		            LayoutParams.WRAP_CONTENT, 1);
			for (int i = 0; i < N; i++) {
				input_name[i] = new EditText(name_cont.getContext());
				input_decimal[i] = new EditText(name_cont.getContext());
				input_name[i].setLayoutParams(params);
				input_decimal[i].setLayoutParams(params);
				input_name[i].setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
				input_decimal[i].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
				name_cont.addView(input_name[i]);
				name_dec.addView(input_decimal[i]);
			}
			EditText tx = (EditText)findViewById(R.id.ETCreditos);
			if (next) {
				edit.clearFocus();
				tx.requestFocus();
				tx.setCursorVisible(true);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void Initialize()
	{
		db = new SQLiteDataBase(this);
		final EditText edit = (EditText) findViewById(R.id.Nnotas);
		name_cont = (LinearLayout) findViewById(R.id.inputcontainer1);
		name_dec = (LinearLayout) findViewById(R.id.inputcontainer2);
		edit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
					return AddEditTexts(true);	
				} else {
					return false;
				}
			}
		});
		edit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				AddEditTexts(false);
			}
		});
		Button Aceptar = (Button)findViewById(R.id.BTaceptar);
		Aceptar.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText txname = (EditText) findViewById(R.id.materia_name);
				String name= txname.getText().toString();
				String nota_name[] = new String[input_name.length];
				float nota[] = new float[input_name.length];
				EditText cred = (EditText)findViewById(R.id.ETCreditos);
				int creditos = Integer.parseInt(cred.getText().toString());
				try {
					float result=0.0f;
					for (int i = 0; i < nota_name.length; i++) {
						nota_name[i] = input_name[i].getText().toString();
						nota[i] = Float.parseFloat(input_decimal[i].getText().toString());
						result+=nota[i];
					}
					if (result==100.0f && creditos>0){
						Bundle bn = getIntent().getExtras();
						String dataname = bn.getString("dataname");
						db.AddMateria(dataname, name, creditos);
						db.AddNotas(dataname, name, nota_name, nota);
						finish();
					} else {
						Toast.makeText(v.getContext(), "La Suma de los porcentajes no es 100.", Toast.LENGTH_LONG).show();
					}
				} catch (Exception ex) {
					Toast.makeText(v.getContext(), "Error en los numeros.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
