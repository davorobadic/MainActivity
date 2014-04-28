package mk.finki.ukim.jmm.staracarsija;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListaFragment extends ListFragment {
	OnItemLongClickedListener mCallback;
	
	public final static String EXTRA_MESSAGE = "mk.finki.ukim.jmm.staracarsija.MESSAGE";
	
	public interface OnItemLongClickedListener {
		public void onItemLongClicked(String kategorija);
	}
	
	String[] values = new String[] { "Ресторани", "Кафулиња и барови",
	        "Споменици, Музеи, Култура",
	        "Занаетчии и Антикварници",
	        "Останато (Пазар, Бутици)", "Сите"};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnItemLongClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemLongClickedListener");
        }
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		MySimpleAdapter adapter = new MySimpleAdapter(getActivity(), values);
		setListAdapter(adapter);
		//getListView().setOnItemClickListener(listener)
		
		//ListView lv = getListView();
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

             public boolean onItemLongClick(AdapterView<?> av, View v,
                     int position, long id) {
            	 String text = (String) getListView().getItemAtPosition(position);
            	 //Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
            	 mCallback.onItemLongClicked(text);
                 return true;
             }
         });
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		 String item = (String) getListAdapter().getItem(position);
         
 		Intent intent = new Intent(getActivity(), Objekti.class);
 		intent.putExtra(EXTRA_MESSAGE, item);
 		startActivity(intent);
	}
	

}
