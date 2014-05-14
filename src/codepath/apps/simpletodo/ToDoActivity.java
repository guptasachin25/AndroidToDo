package codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoActivity extends ActionBarActivity {
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	int editablePosition = -1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        readItems();
        lvItems = (ListView) findViewById(R.id.listView1);
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setUpListViewListner();
        setUpEditListner();
    }

    private void setUpListViewListner() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View item, int pos,
					long id) {
					items.remove(pos);
					itemsAdapter.notifyDataSetInvalidated();
					saveItems();
					return true;
			}
    	});
    }
    
   private void setUpEditListner() {
	   lvItems.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,
				long row) {
			String itemToEdit = items.get(pos);
			editablePosition = pos;
			Intent intent = new Intent(ToDoActivity.this, EditItems1.class);
			intent.putExtra("items", itemToEdit);
			startActivityForResult(intent, 1);
		}
	});
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   if(requestCode == 1 && resultCode == RESULT_OK) {
		   items.set(editablePosition, data.getStringExtra("items"));
		   itemsAdapter.notifyDataSetChanged();
		   saveItems();
	   }
   }
    private void readItems(){
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
    		items = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch (IOException e) {
    		items = new ArrayList<String>();
    		e.printStackTrace();
    	}
    }
    
    private void saveItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
    		FileUtils.writeLines(todoFile, items);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void addToDoItem(View v) {
    	EditText etNewItem  = (EditText)findViewById(R.id.editText1);
    	System.out.println(etNewItem.getText().toString());
    	items.add(etNewItem.getText().toString());
    	itemsAdapter.notifyDataSetChanged();
    	saveItems();
    	etNewItem.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
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

}
