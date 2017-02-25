package smartapp.gameoflife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<RectangleObject> rectangles = new ArrayList<RectangleObject>();
    ArrayList<Integer> alive = new ArrayList<Integer>();

    final int LIFE_SIZE = 12;
    final int HORIZONTAL_MARGIN = 2;
    final int VERTICAL_MARGIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button resetButton = (Button) findViewById(R.id.reset);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        Log.d(MainActivity.class.getName(), "device width:" + width);


        int totalMargin = LIFE_SIZE * (HORIZONTAL_MARGIN * 2);
        final GridLayout gridLayout = (GridLayout) findViewById(R.id.gridFrameOfGame);
        gridLayout.setColumnCount(LIFE_SIZE);
        gridLayout.setRowCount(LIFE_SIZE);
        for (int i = 1; i <= (LIFE_SIZE * LIFE_SIZE); i++) {
            Button button = new Button(this);
            button.setId(i+1-1);
//            button.setFitsSystemWindows(true);
            button.setTag(i);
            GridLayout.LayoutParams layoutParams=new GridLayout.LayoutParams();
            layoutParams.height = (width-totalMargin)/LIFE_SIZE;
            layoutParams.width = (width-totalMargin)/LIFE_SIZE;
            layoutParams.setMargins(HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_MARGIN);
            button.setLayoutParams(layoutParams);
            button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.background_light));
//            button.setBackgroundResource(android.R.drawable.btn_default_small);
            button.setOnClickListener(mOnClickListener);
            gridLayout.addView(button);
        }

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Reset Frame")
                        .setMessage("Are you sure you want to reset this frame?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 1; i <= (LIFE_SIZE * LIFE_SIZE); i++) {
                                    Button tempButton = (Button) findViewById(i+1-1);
                                    tempButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.background_light));
                                }
                                alive.clear();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean deadFlag = false;
            int availablePosition = 0;
            for (int live: alive){
                 if(v.getId() == live){
                     v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.background_light));
                     deadFlag = true;
                     alive.remove(availablePosition);
                     break;
                 }
                availablePosition++;
            }
            if(!deadFlag) {
                v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
                alive.add(v.getId());
            }

//            Toast.makeText(getApplicationContext(), "clicked button :" + v.getTag(), Toast.LENGTH_SHORT).show();
//            Log.d(MainActivity.class.getName()+ "Color", String.valueOf(v.getBackground()));
//            Log.d(MainActivity.class.getName()+ "ID", String.valueOf(v.getId()));
        }
    };


}
