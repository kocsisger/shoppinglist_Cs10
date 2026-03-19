package hu.unideb.inf.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ItemsActivity extends AppCompatActivity {

    public static final String ITEM = "ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isColorFragmentNotShown = true;

    public void handleAddItem(View view) {
        String value = ((Button) view).getText().toString();

        if (value.equals(getString(R.string.apple))) {
            if (isColorFragmentNotShown) {
                displayColorFragment();
                isColorFragmentNotShown = false;
                return;
            } else {
                value += " " + colorFragment.getColor();
            }
        }

        Intent replyIntent = new Intent();
        replyIntent.putExtra(ITEM, value);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    ColorFragment colorFragment;

    private void displayColorFragment() {
        colorFragment = new ColorFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, colorFragment)
                .addToBackStack(null)
                .commit();
    }
}