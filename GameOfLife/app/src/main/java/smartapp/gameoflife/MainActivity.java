package smartapp.gameoflife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.IntegerRes;
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
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ArrayList<Integer> alive = new ArrayList<Integer>();

    final int LIFE_SIZE = 12;
    final int HORIZONTAL_MARGIN = 2;
    final int VERTICAL_MARGIN = 2;
    Boolean playFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button resetButton = (Button) findViewById(R.id.reset);
        Button nextButton = (Button) findViewById(R.id.next);
        Button playButton = (Button) findViewById(R.id.play);

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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> tempAlive = newAlive();
                alive.clear();
                alive = new ArrayList<Integer>(tempAlive);
            }
        });


        //Call handler to handle play event
        playButton.setOnClickListener(playOnClickListener);

    }

    //Set timer to run continuously and call main thread to change UI
    private  View.OnClickListener playOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            Timer time = new Timer();
            time.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Integer> tempAlive = newAlive();
                            alive.clear();
                            alive = new ArrayList<Integer>(tempAlive);
                        }
                    });
                }
            }, 0, 1000);
        }
    };


    //OnClick Swap alive and dead cell
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


    //Find alive cell for next generation
    public ArrayList<Integer> newAlive(){
        ArrayList<Integer> newAlive = new ArrayList<Integer>();

        for (int i = 1; i <= (LIFE_SIZE * LIFE_SIZE); i++) {
            int currentAliveNeighbour = 0;
            currentAliveNeighbour = countNeighbourAlive(i);

            // 1 rule: Any live cell with fewer than two live neighbours dies, as if caused by underpopulation
            if(currentAliveNeighbour < 2){
                Button tempButton = (Button) findViewById(i+1-1);
                tempButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.background_light));
            }

            // 2 rule: Any live cell with two or three live neighbours lives on to the next generation
            else if(checkCellAlive(i) && (currentAliveNeighbour == 2 || currentAliveNeighbour == 3)){
                Button tempButton = (Button) findViewById(i+1-1);
                tempButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
                newAlive.add(i);
            }

            // 3 rule: Any live cell with more than three live neighbours dies, as if by overpopulation
            else if(currentAliveNeighbour > 3){
                Button tempButton = (Button) findViewById(i+1-1);
                tempButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.background_light));
            }

            else if(!checkCellAlive(i) && currentAliveNeighbour == 3){
                Button tempButton = (Button) findViewById(i+1-1);
                tempButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
                newAlive.add(i);
            }

        }

        return newAlive;
    }


    //Find number of alive neighbours
    public int countNeighbourAlive(int cell){

        //Define all the neighbours
        int left = cell - 1;
        int right = cell + 1;
        int top = cell - LIFE_SIZE;
        int bottom = cell + LIFE_SIZE;
        int topLeft = top - 1;
        int topRight = top + 1;
        int bottomLeft = bottom - 1;
        int bottomRight = bottom + 1;
        int neighbourAlive = 0;

        if (left > 0 && (cell % LIFE_SIZE) != 1 && checkCellAlive(left)) {
            neighbourAlive+= 1;
        }

        if(right <= (LIFE_SIZE * LIFE_SIZE) && (cell % LIFE_SIZE) != 0 && checkCellAlive(right)){
            neighbourAlive+= 1;
        }

        if(top > 0 && checkCellAlive(top)){
            neighbourAlive+= 1;
        }

        if(bottom <= (LIFE_SIZE * LIFE_SIZE) && checkCellAlive(bottom)){
            neighbourAlive+= 1;
        }

        if(topLeft > 0 && (cell % LIFE_SIZE) != 1 && checkCellAlive(topLeft)){
            neighbourAlive+= 1;
        }

        if(topRight > 0 && (cell % LIFE_SIZE) != 0 && checkCellAlive(topRight)){
            neighbourAlive+= 1;
        }

        if(bottomLeft <= (LIFE_SIZE * LIFE_SIZE) && (cell % LIFE_SIZE) != 1 && checkCellAlive(bottomLeft)){
            neighbourAlive+= 1;
        }

        if(bottomRight <= (LIFE_SIZE * LIFE_SIZE) && (cell % LIFE_SIZE) != 0 && checkCellAlive(bottomRight)){
            neighbourAlive+= 1;
        }

        return neighbourAlive;
    }

    //Check whether cell is alive or dead
    public boolean checkCellAlive(int cell){

        for(int i = 0; i < alive.size(); i++){
            if(cell == alive.get(i)){
                return true;
            }
        }
        return false;
    }
}
