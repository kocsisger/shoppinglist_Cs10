package hu.unideb.inf.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final String TEXTVIEW_CONTENT = "TEXTVIEW_CONTENT";
    TextView itemsTextView;
    private boolean listIsEmpty = true;

    ActivityResultLauncher activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            activityResult -> {
                Log.d("ITEMS_TEST", "I have returned");
                String item = activityResult.getData().getStringExtra(ItemsActivity.ITEM);
                Log.d("ITEMS_TEST", item);
                //if (listIsEmpty) {
                if (itemsTextView.getText().toString().equals(getString(R.string.empty_list))) {
                    itemsTextView.setText("");
                    listIsEmpty = false;
                }
                itemsTextView.append(item + "\n");
            }
    );

    SharedPreferences sharedPreferences;
    String sharedPrefFilename = "shrdprf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences(sharedPrefFilename, MODE_PRIVATE);

        itemsTextView = findViewById(R.id.itemsTextView);
        if (savedInstanceState != null)
            itemsTextView.setText(savedInstanceState.getString(TEXTVIEW_CONTENT));
        else
            itemsTextView.setText(
                    sharedPreferences.getString(TEXTVIEW_CONTENT, getString(R.string.empty_list))
            );
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedPreferences.edit()
                .putString(TEXTVIEW_CONTENT, itemsTextView.getText().toString())
                .apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEXTVIEW_CONTENT, itemsTextView.getText().toString());
    }

    public void handleAddButtonPressed(View view) {
        Intent intent = new Intent(this, ItemsActivity.class);
        //startActivity(intent);
        activityResultLauncher.launch(intent);
    }
}