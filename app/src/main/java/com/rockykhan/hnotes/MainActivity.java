package com.rockykhan.hnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rockykhan.hnotes.RecyclerListener.RecyclerItemClickListener;
import com.rockykhan.hnotes.adapters.MyRecyclerAdapter;
import com.rockykhan.hnotes.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DatabaseHelper databaseHelper;
    ArrayList<Note> notesArray;
    MyRecyclerAdapter adapter;
    boolean isSelectMode = false;
    ArrayList<Note> selectedItems;
    ArrayList<View> selectedViews;

    public static final String NOTES_ID = "com.rockykhan.hnotes.editNotes.ID";
    public static final String NOTES_TITLE = "com.rockykhan.hnotes.editNotes.TITLE";
    public static final String NOTES_TEXT = "com.rockykhan.hnotes.editNotes.TEXT";

    @Override
    protected void onStart() {
        super.onStart();
        showNotes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        selectedItems = new ArrayList<>();
        selectedViews = new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = DatabaseHelper.getInstance(this);

        showNotes();

        binding.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, binding.recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (isSelectMode){
                    binding.deleteLayout.setVisibility(View.VISIBLE);

                    if (selectedItems.contains(notesArray.get(position))){
                        deselectItem(view, position);
                    }else{
                        selectItem(view, position);
                    }

                    if (selectedItems.size() == 0){
                        isSelectMode = false;
                        binding.deleteLayout.setVisibility(View.GONE);
                    }

                }else {

                    Intent intent = new Intent(getApplicationContext(), EditNotes.class);
                    int id = notesArray.get(position).getId();
                    String title = notesArray.get(position).getTitle();
                    String text = notesArray.get(position).getText();
                    intent.putExtra(NOTES_ID, id);
                    intent.putExtra(NOTES_TITLE, title);
                    intent.putExtra(NOTES_TEXT, text);
                    startActivity(intent);

                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

                isSelectMode = true;
                binding.deleteLayout.setVisibility(View.VISIBLE);

                if (selectedItems.contains(notesArray.get(position))){
                    deselectItem(view, position);
                }else{
                    selectItem(view, position);
                }

                if (selectedItems.size() == 0){
                    isSelectMode = false;
                    binding.deleteLayout.setVisibility(View.GONE);
                }

            }
        }));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int adapterPosition = viewHolder.getAdapterPosition();
                int noteId = notesArray.get(adapterPosition).getId();
                String noteTitle = notesArray.get(adapterPosition).getTitle();
                String noteText = notesArray.get(adapterPosition).getText();
                databaseHelper.notesDAO().deleteNote(new Note(noteId, noteTitle, noteText));
                showNotes();
            }
        }).attachToRecyclerView(binding.recyclerView);

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddNotes.class));
            }
        });

        binding.btnAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.fabAdd.performClick();
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDeleteDialog();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.opt_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){

            case R.id.opt_new:
                binding.fabAdd.performClick();
                break;

            case R.id.opt_select:
                    isSelectMode = true;
                Toast.makeText(this, "Tap to select items", Toast.LENGTH_LONG).show();
                break;

            case R.id.opt_delete:
                if (isSelectMode){
                    showDeleteDialog();
                }else {
                    binding.deleteLayout.setVisibility(View.VISIBLE);
                    isSelectMode = true;
                    Toast.makeText(this, "Select items to delete", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.opt_exit:
                onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void selectItem(View item, int position){
        item.setBackgroundResource(R.color.selectedColor);
        selectedItems.add(notesArray.get(position));
        selectedViews.add(item);
    }

    public void deselectItem(View item, int position){
        item.setBackgroundResource(R.color.normalColor);
        selectedItems.remove(notesArray.get(position));
        selectedViews.remove(item);
    }

    public void showDeleteDialog(){
        if (selectedItems.size() == 0){
            Toast.makeText(this, "please select items to delete", Toast.LENGTH_SHORT).show();
        }else{

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        databaseHelper.notesDAO().deleteNote(new Note(notesArray.get(position).getId(), notesArray.get(position).getTitle(), notesArray.get(position).getText()));
                        for (Note note: selectedItems){
                            databaseHelper.notesDAO().deleteNote(note);
                        }
                        showNotes();
                        binding.deleteLayout.setVisibility(View.GONE);
                        isSelectMode = false;
                    }
                })
                .setNegativeButton(R.string.negative, null)
                .setIcon(R.drawable.delete)
                .show();
        }

    }

    public void showNotes() {

        notesArray = (ArrayList<Note>) databaseHelper.notesDAO().getNotes();

        if (notesArray.size() > 0){
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyNotesLinearLayout.setVisibility(View.GONE);
            adapter = new MyRecyclerAdapter(getApplicationContext(), notesArray);
            binding.recyclerView.setAdapter(adapter);
        }else{
            binding.emptyNotesLinearLayout.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {

        if (!isSelectMode){

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to Exit?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.drawable.warning)
                    .show();

        }else {

            isSelectMode = false;
            selectedItems.clear();
            binding.deleteLayout.setVisibility(View.GONE);
            for (View view: selectedViews) {
                view.setBackgroundResource(R.color.normalColor);
            }
            selectedViews.clear();

        }

    }

}
