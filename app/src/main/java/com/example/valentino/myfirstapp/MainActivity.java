package com.example.valentino.myfirstapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener{

    public static final String HOME = "HOME";
    public static final String AWAY = "AWAY";
    public static final String GOALS = "GOALS";
    public static final String SHOTS = "SHOTS";
    public static final String FOULS = "FOULS";
    public static final String ASSISTS = "ASSISTS";
    public static final String SAVES = "SAVES";
    public static final String TACKLES = "TACKLES";
    public static final String PASSES = "PASSES";
    public static final String LOADED = "LOADED";
    public static final String COUNTER = "COUNTER";


    String curr_filename;
    TextView gameText;
    FileOutputStream fos;

    boolean loaded = false;
    private Button sendEmail;
    //private Button newGame;
    private int gameCounter = 1;
    //private Button loadGame;
    //private Button reset;
    private Button menu;
    private int home = 0;
    private Button plusHome;
    private Button minusHome;
    private TextView homeText;
    private TextView homeTextAvg;
    private int away = 0;
    private Button plusAway;
    private Button minusAway;
    private TextView awayText;
    private TextView awayTextAvg;
    private int goals = 0;
    private float goals_avg_ls;
    private float goals_perf_ls;
    private Button plusGoal;
    private Button minusGoal;
    private TextView goalsText;
    private TextView goalsTextAvg;
    private TextView goalsTextPerf;
    private int shots = 0;
    private float shots_avg_ls;
    private float shots_perf_ls;
    private Button plusShot;
    private Button minusShot;
    private TextView shotsText;
    private TextView shotsTextAvg;
    private TextView shotsTextPerf;
    private int fouls= 0;
    private float fouls_avg_ls;
    private float fouls_perf_ls;
    private Button plusFoul;
    private Button minusFoul;
    private TextView foulsText;
    private TextView foulsTextAvg;
    private TextView foulsTextPerf;
    private int assists = 0;
    private float assists_avg_ls;
    private float assists_perf_ls;
    private Button plusAssist;
    private Button minusAssist;
    private TextView assistsText;
    private TextView assistsTextAvg;
    private TextView assistsTextPerf;
    private int saves = 0;
    private float saves_avg_ls;
    private float saves_perf_ls;
    private Button plusSave;
    private Button minusSave;
    private TextView savesText;
    private TextView savesTextAvg;
    private TextView savesTextPerf;
    private int tackles = 0;
    private float tackles_avg_ls;
    private float tackles_perf_ls;
    private Button plusTackle;
    private Button minusTackle;
    private TextView tacklesText;
    private TextView tacklesTextAvg;
    private TextView tacklesTextPerf;
    private int passes = 0;
    private float passes_avg_ls;
    private float passes_perf_ls;
    private Button plusPass;
    private Button minusPass;
    private TextView passesText;
    private TextView passesTextAvg;
    private TextView passesTextPerf;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(HOME, home);
        savedInstanceState.putInt(AWAY, away);
        savedInstanceState.putInt(GOALS, goals);
        savedInstanceState.putInt(SHOTS, shots);
        savedInstanceState.putInt(FOULS, fouls);
        savedInstanceState.putInt(ASSISTS, assists);
        savedInstanceState.putInt(SAVES, saves);
        savedInstanceState.putInt(TACKLES, tackles);
        savedInstanceState.putInt(PASSES, passes);
        savedInstanceState.putBoolean(LOADED, loaded);
        savedInstanceState.putInt(COUNTER, gameCounter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        home = savedInstanceState.getInt(HOME);
        away = savedInstanceState.getInt(AWAY);
        goals = savedInstanceState.getInt(GOALS);
        shots = savedInstanceState.getInt(SHOTS);
        fouls = savedInstanceState.getInt(FOULS);
        assists = savedInstanceState.getInt(ASSISTS);
        saves = savedInstanceState.getInt(SAVES);
        tackles = savedInstanceState.getInt(TACKLES);
        passes = savedInstanceState.getInt(PASSES);
        loaded = savedInstanceState.getBoolean(LOADED);
        gameCounter = savedInstanceState.getInt(COUNTER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create_popup_menu();

        if(savedInstanceState != null) {
            home = savedInstanceState.getInt(HOME);
            away = savedInstanceState.getInt(AWAY);
            goals = savedInstanceState.getInt(GOALS);
            shots = savedInstanceState.getInt(SHOTS);
            fouls = savedInstanceState.getInt(FOULS);
            assists = savedInstanceState.getInt(ASSISTS);
            saves = savedInstanceState.getInt(SAVES);
            tackles = savedInstanceState.getInt(TACKLES);
            passes = savedInstanceState.getInt(PASSES);
            loaded = savedInstanceState.getBoolean(LOADED);
            gameCounter = savedInstanceState.getInt(COUNTER);
        }

        SharedPreferences avg_prefs = getSharedPreferences("average", Context.MODE_PRIVATE);
        SharedPreferences.Editor avg_editor = avg_prefs.edit();

        if(avg_prefs.contains("gameCounter")) {
            gameCounter = avg_prefs.getInt("gameCounter", -1);
        }
        else {
            avg_editor.putInt("gameCounter", gameCounter);
            avg_editor.putFloat("home_avg", home);
            avg_editor.putFloat("away_avg", away);
            avg_editor.putFloat("goals_avg", goals);
            avg_editor.putFloat("shots_avg", shots);
            avg_editor.putFloat("assists_avg", assists);
            avg_editor.putFloat("saves_avg", saves);
            avg_editor.putFloat("fouls_avg", fouls);
            avg_editor.putFloat("tackles_avg", tackles);
            avg_editor.putFloat("passes_avg", passes);
            avg_editor.commit();

            try {
                fos = openFileOutput("average", Context.MODE_PRIVATE);
                PrintWriter pw = new PrintWriter(fos);
                Map<String, ?> prefsMap = avg_prefs.getAll();
                for(Map.Entry<String, ?> entry : prefsMap.entrySet()){
                    pw.println(entry.getKey() + ": " + entry.getValue().toString());
                }

                pw.close();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        curr_filename = "Game" + Integer.toString(gameCounter);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            sendEmail = (Button) findViewById(R.id.send_email);
            sendEmail.setOnClickListener(this);
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gameText = (TextView)findViewById(R.id.curr_game);
            gameText.setText("Game " + gameCounter);/*
            reset = (Button) findViewById(R.id.reset);
            reset.setOnClickListener(this);
            newGame = (Button) findViewById(R.id.new_game);
            newGame.setOnClickListener(this);
            loadGame = (Button) findViewById(R.id.load_game);
            loadGame.setOnClickListener(this);*/
            menu = (Button) findViewById(R.id.menu);
            menu.setOnClickListener(this);
            plusHome = (Button) findViewById(R.id.plus_home);
            plusHome.setOnClickListener(this);
            minusHome = (Button) findViewById(R.id.minus_home);
            minusHome.setOnClickListener(this);
            plusAway = (Button) findViewById(R.id.plus_away);
            plusAway.setOnClickListener(this);
            minusAway = (Button) findViewById(R.id.minus_away);
            minusAway.setOnClickListener(this);
            plusGoal = (Button) findViewById(R.id.plus_goal);
            plusGoal.setOnClickListener(this);
            minusGoal = (Button) findViewById(R.id.minus_goal);
            minusGoal.setOnClickListener(this);
            plusShot = (Button) findViewById(R.id.plus_shot);
            plusShot.setOnClickListener(this);
            minusShot = (Button) findViewById(R.id.minus_shot);
            minusShot.setOnClickListener(this);
            plusAssist = (Button) findViewById(R.id.plus_assist);
            plusAssist.setOnClickListener(this);
            minusAssist = (Button) findViewById(R.id.minus_assist);
            minusAssist.setOnClickListener(this);
            plusSave = (Button) findViewById(R.id.plus_save);
            plusSave.setOnClickListener(this);
            minusSave = (Button) findViewById(R.id.minus_save);
            minusSave.setOnClickListener(this);
            plusFoul = (Button) findViewById(R.id.plus_foul);
            plusFoul.setOnClickListener(this);
            minusFoul = (Button) findViewById(R.id.minus_foul);
            minusFoul.setOnClickListener(this);
            plusTackle = (Button) findViewById(R.id.plus_tackle);
            plusTackle.setOnClickListener(this);
            minusTackle = (Button) findViewById(R.id.minus_tackle);
            minusTackle.setOnClickListener(this);
            plusPass= (Button) findViewById(R.id.plus_pass);
            plusPass.setOnClickListener(this);
            minusPass = (Button) findViewById(R.id.minus_pass);
            minusPass.setOnClickListener(this);

            if(loaded) {
                plusHome.setClickable(false);
                minusHome.setClickable(false);
                plusAway.setClickable(false);
                minusAway.setClickable(false);
                plusGoal.setClickable(false);
                minusGoal.setClickable(false);
                plusShot.setClickable(false);
                minusShot.setClickable(false);
                plusAssist.setClickable(false);
                minusAssist.setClickable(false);
                plusSave.setClickable(false);
                minusSave.setClickable(false);
                plusFoul.setClickable(false);
                minusFoul.setClickable(false);
                plusTackle.setClickable(false);
                minusTackle.setClickable(false);
                plusPass.setClickable(false);
                minusPass.setClickable(false);
            }
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            goals_avg_ls = avg_prefs.getFloat("goals_avg", -1);
            goalsTextAvg = (TextView)findViewById(R.id.avg_goal);
            goalsTextAvg.setText(Float.toString(goals_avg_ls));
            goals_perf_ls = goals / goals_avg_ls * 100;
            goalsTextPerf = (TextView)findViewById(R.id.perf_goal);
            goalsTextPerf.setText(Float.toString(goals_perf_ls) + "%");

            shots_avg_ls = avg_prefs.getFloat("shots_avg", -1);
            shotsTextAvg = (TextView)findViewById(R.id.avg_shot);
            shotsTextAvg.setText(Float.toString(shots_avg_ls));
            shots_perf_ls = shots / shots_avg_ls * 100;
            shotsTextPerf = (TextView)findViewById(R.id.perf_shot);
            shotsTextPerf.setText(Float.toString(shots_perf_ls) + "%");

            assists_avg_ls = avg_prefs.getFloat("assists_avg", -1);
            assistsTextAvg = (TextView)findViewById(R.id.avg_assist);
            assistsTextAvg.setText(Float.toString(assists_avg_ls));
            assists_perf_ls = assists / assists_avg_ls * 100;
            assistsTextPerf = (TextView)findViewById(R.id.perf_assist);
            assistsTextPerf.setText(Float.toString(assists_perf_ls) + "%");

            saves_avg_ls = avg_prefs.getFloat("saves_avg", -1);
            savesTextAvg = (TextView)findViewById(R.id.avg_save);
            savesTextAvg.setText(Float.toString(saves_avg_ls));
            saves_perf_ls = saves / saves_avg_ls * 100;
            savesTextPerf = (TextView)findViewById(R.id.perf_save);
            savesTextPerf.setText(Float.toString(saves_perf_ls) + "%");

            tackles_avg_ls = avg_prefs.getFloat("tackles_avg", -1);
            tacklesTextAvg = (TextView)findViewById(R.id.avg_tackle);
            tacklesTextAvg.setText(Float.toString(tackles_avg_ls));
            tackles_perf_ls = tackles / tackles_avg_ls * 100;
            tacklesTextPerf = (TextView)findViewById(R.id.perf_tackle);
            tacklesTextPerf.setText(Float.toString(tackles_perf_ls) + "%");

            fouls_avg_ls = avg_prefs.getFloat("fouls_avg", -1);
            foulsTextAvg = (TextView)findViewById(R.id.avg_foul);
            foulsTextAvg.setText(Float.toString(fouls_avg_ls));
            fouls_perf_ls = fouls / fouls_avg_ls * 100;
            foulsTextPerf = (TextView)findViewById(R.id.perf_foul);
            foulsTextPerf.setText(Float.toString(fouls_perf_ls) + "%");

            passes_avg_ls = avg_prefs.getFloat("passes_avg", -1);
            passesTextAvg = (TextView)findViewById(R.id.avg_pass);
            passesTextAvg.setText(Float.toString(passes_avg_ls));
            passes_perf_ls = passes / passes_avg_ls * 100;
            passesTextPerf = (TextView)findViewById(R.id.perf_pass);
            passesTextPerf.setText(Float.toString(passes_perf_ls) + "%");
        }

        homeText = (TextView)findViewById(R.id.text_home);
        homeText.setText(Integer.toString(home));
        awayText = (TextView)findViewById(R.id.text_away);
        awayText.setText(Integer.toString(away));
        goalsText = (TextView)findViewById(R.id.text_goal);
        goalsText.setText(Integer.toString(goals));
        shotsText = (TextView)findViewById(R.id.text_shot);
        shotsText.setText(Integer.toString(shots));
        assistsText = (TextView)findViewById(R.id.text_assist);
        assistsText.setText(Integer.toString(assists));
        savesText = (TextView)findViewById(R.id.text_save);
        savesText.setText(Integer.toString(saves));
        tacklesText = (TextView)findViewById(R.id.text_tackle);
        tacklesText.setText(Integer.toString(tackles));
        foulsText = (TextView)findViewById(R.id.text_foul);
        foulsText.setText(Integer.toString(fouls));
        passesText = (TextView)findViewById(R.id.text_pass);
        passesText.setText(Integer.toString(passes));


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.send_email) {
            send_email();
        }

/*        if(v.getId() == R.id.reset) {

        }
        if(v.getId() == R.id.new_game) {

        }

        if(v.getId() == R.id.load_game) {
            load_a_game();
        }
*/
        if(v.getId() == R.id.menu) {
            show_options();
        }

        if(v.getId() == R.id.plus_home) {
            home++;
            homeText.setText(Integer.toString(home));
            update();
        }
        if(v.getId() == R.id.minus_home) {
            if(home > 0) {
                home--;
            }
            homeText.setText(Integer.toString(home));
            update();
        }

        if(v.getId() == R.id.plus_away) {
            away++;
            awayText.setText(Integer.toString(away));
            update();
        }
        if(v.getId() == R.id.minus_away) {
            if(away > 0) {
                away--;
            }
            awayText.setText(Integer.toString(away));
            update();
        }

        if(v.getId() == R.id.plus_goal) {
            goals++;
            goalsText.setText(Integer.toString(goals));
            update();
        }
        if(v.getId() == R.id.minus_goal) {
            if(goals > 0){
                goals--;
            }
            goalsText.setText(Integer.toString(goals));
            update();
        }

        if(v.getId() == R.id.plus_shot) {
            shots++;
            shotsText.setText(Integer.toString(shots));
            update();
        }
        if(v.getId() == R.id.minus_shot) {
            if(shots > 0) {
                shots--;
            }
            shotsText.setText(Integer.toString(shots));
            update();
        }

        if(v.getId() == R.id.plus_assist) {
            assists++;
            assistsText.setText(Integer.toString(assists));
            update();
        }
        if(v.getId() == R.id.minus_assist) {
            if(assists > 0) {
                assists--;
            }
            assistsText.setText(Integer.toString(assists));
            update();
        }

        if(v.getId() == R.id.plus_save) {
            saves++;
            savesText.setText(Integer.toString(saves));
            update();
        }
        if(v.getId() == R.id.minus_save) {
            if(saves > 0) {
                saves--;
            }
            savesText.setText(Integer.toString(saves));
            update();
        }

        if(v.getId() == R.id.plus_foul) {
            fouls++;
            foulsText.setText(Integer.toString(fouls));
            update();
        }
        if(v.getId() == R.id.minus_foul) {
            if(fouls > 0) {
                fouls--;
            }
            foulsText.setText(Integer.toString(fouls));
            update();
        }

        if(v.getId() == R.id.plus_tackle) {
            tackles++;
            tacklesText.setText(Integer.toString(tackles));
            update();
        }
        if(v.getId() == R.id.minus_tackle) {
            if(tackles > 0) {
                tackles--;
            }
            tacklesText.setText(Integer.toString(tackles));
            update();
        }

        if(v.getId() == R.id.plus_pass) {
            passes++;
            passesText.setText(Integer.toString(passes));
            update();
        }
        if(v.getId() == R.id.minus_pass) {
            if(passes > 0) {
                passes--;
            }
            passesText.setText(Integer.toString(passes));
            update();
        }
        /*
        else if(v.getId() == R.id.peek_button) {
            //Intent intent = new Intent(this, PeekActivity.class);
            //startActivity(intent);
            //startActivityForResult(intent, PEEK_REQUEST);
            Uri webpage = Uri.parse("https://en.wikipedia.org/wiki/Springfield,_Illinois");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(intent);
        }
        */
    }

    protected void update() {
        if(!loaded) {
            SharedPreferences pause_prefs = getSharedPreferences(curr_filename, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pause_prefs.edit();
            editor.putInt(HOME, home);
            editor.putInt(AWAY, away);
            editor.putInt(GOALS, goals);
            editor.putInt(SHOTS, shots);
            editor.putInt(ASSISTS, assists);
            editor.putInt(SAVES, saves);
            editor.putInt(FOULS, fouls);
            editor.putInt(TACKLES, tackles);
            editor.putInt(PASSES, passes);
            editor.commit();

            try {
                fos = openFileOutput(curr_filename, Context.MODE_PRIVATE);
                PrintWriter pw = new PrintWriter(fos);
                Map<String, ?> prefsMap = pause_prefs.getAll();
                for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
                    pw.println(entry.getKey() + ": " + entry.getValue().toString());
                }

                pw.close();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            SharedPreferences avg_prefs3 = getSharedPreferences("average", Context.MODE_PRIVATE);
            SharedPreferences.Editor avg_editor3 = avg_prefs3.edit();

            avg_editor3.putInt("gameCounter", gameCounter);
            avg_editor3.commit();

            try {
                fos = openFileOutput("average", Context.MODE_PRIVATE);
                PrintWriter pw = new PrintWriter(fos);
                Map<String, ?> prefsMap = avg_prefs3.getAll();
                for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
                    pw.println(entry.getKey() + ": " + entry.getValue().toString());
                }

                pw.close();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        update();
    }

/*
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == PEEK_REQUEST) {
            if(resultCode == RESULT_OK) {
                String s = intent.getStringExtra("KEY-RESULT");
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }
    */


/*
    home = savedInstanceState.getInt(HOME);
    away = savedInstanceState.getInt(AWAY);
    goals = savedInstanceState.getInt(GOALS);
    shots = savedInstanceState.getInt(SHOTS);
    fouls = savedInstanceState.getInt(FOULS);
    assists = savedInstanceState.getInt(ASSISTS);
    saves = savedInstanceState.getInt(SAVES);
    tackles = savedInstanceState.getInt(TACKLES);
    passes = savedInstanceState.getInt(PASSES);
*/

    public void send_email() {

        String mail_body = "";
        mail_body += "HOME: "+home+"  AWAY: "+away+"\n";
        mail_body += "GOALS: "+goals+"  AVG GOALS: "+goals_avg_ls+"  PERFORMANCE: "+goals_perf_ls+"%\n";
        mail_body += "SHOTS: "+shots+"  AVG SHOTS: "+shots_avg_ls+"  PERFORMANCE: "+shots_perf_ls+"%\n";
        mail_body += "FOULS: "+fouls+"  AVG FOULS: "+fouls_avg_ls+"  PERFORMANCE: "+fouls_perf_ls+"%\n";
        mail_body += "ASSISTS: "+assists+"  AVG ASSISTS: "+assists_avg_ls+"  PERFORMANCE: "+assists_perf_ls+"%\n";
        mail_body += "SAVES: "+saves+"  AVG SAVES: "+saves_avg_ls+"  PERFORMANCE: "+saves_perf_ls+"%\n";
        mail_body += "TACKLES: "+tackles+"  AVG TACKLES: "+tackles_avg_ls+"  PERFORMANCE: "+tackles_perf_ls+"%\n";
        mail_body += "PASSES: "+passes+"  AVG PASSES: "+passes_avg_ls+"  PERFORMANCE: "+passes_perf_ls+"%\n";

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Soccer Tracker");
        i.putExtra(Intent.EXTRA_TEXT, mail_body);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void load_a_game(){
        //Creating the instance of PopupMenu
/*        PopupMenu popup = new PopupMenu(MainActivity.this, loadGame);
        String[] files = fileList();
        for(int i = 1; i < files.length; i++) {
            popup.getMenu().add("Game " + i);
        }*/
        /* Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());
        */
        //registering popup with OnMenuItemClickListener
/*        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String name = item.getTitle().toString();
                int i = Integer.parseInt((name.substring(5)));
                load_game(i);
                gameText = (TextView)findViewById(R.id.curr_game);
                gameText.setText("Game "+ i +" Loaded");
                return true;
            }
        });

        popup.show(); //showing popup menu
*/

        String[] files = fileList();
        CharSequence[] items=new String[files.length-1];
        final int[] sele = {1};
        for(int i = 1; i < files.length; i++) {
            items[i-1]="Game "+i;
        }
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select a Game").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sele[0] = which+1;
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                load_game(sele[0]);
                gameText = (TextView) findViewById(R.id.curr_game);
                gameText.setText("Game "+ sele[0] +" Loaded");
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.show();
    }

    public void load_game(int i){
        if(!loaded) {
            SharedPreferences prefs = getSharedPreferences(curr_filename, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if(prefs.contains(HOME)) {
                editor.putInt(HOME, home);
                editor.putInt(AWAY, away);
                editor.putInt(GOALS, goals);
                editor.putInt(SHOTS, shots);
                editor.putInt(ASSISTS, assists);
                editor.putInt(SAVES, saves);
                editor.putInt(FOULS, fouls);
                editor.putInt(TACKLES, tackles);
                editor.putInt(PASSES, passes);
                editor.commit();

                try {
                    fos = openFileOutput(curr_filename, Context.MODE_PRIVATE);
                    PrintWriter pw = new PrintWriter(fos);
                    Map<String, ?> prefsMap = prefs.getAll();
                    for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
                        pw.println(entry.getKey() + ": " + entry.getValue().toString());
                    }
                    pw.close();
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                SharedPreferences new_avg_prefs = getSharedPreferences("average", Context.MODE_PRIVATE);
                SharedPreferences.Editor new_avg_editor = new_avg_prefs.edit();
                float home_avg = (new_avg_prefs.getFloat("home_avg", -1) * (gameCounter - 1) + home) / gameCounter;
                float away_avg = (new_avg_prefs.getFloat("away_avg", -1) * (gameCounter - 1) + away) / gameCounter;
                float goals_avg = (new_avg_prefs.getFloat("goals_avg", -1) * (gameCounter - 1) + goals) / gameCounter;
                float shots_avg = (new_avg_prefs.getFloat("shots_avg", -1) * (gameCounter - 1) + shots) / gameCounter;
                float assists_avg = (new_avg_prefs.getFloat("assists_avg", -1) * (gameCounter - 1) + assists) / gameCounter;
                float saves_avg = (new_avg_prefs.getFloat("saves_avg", -1) * (gameCounter - 1) + saves) / gameCounter;
                float fouls_avg = (new_avg_prefs.getFloat("fouls_avg", -1) * (gameCounter - 1) + fouls) / gameCounter;
                float tackles_avg = (new_avg_prefs.getFloat("tackles_avg", -1) * (gameCounter - 1) + tackles) / gameCounter;
                float passes_avg = (new_avg_prefs.getFloat("passes_avg", -1) * (gameCounter - 1) + passes) / gameCounter;
                new_avg_editor.putFloat("home_avg", home_avg);
                new_avg_editor.putFloat("away_avg", away_avg);
                new_avg_editor.putFloat("goals_avg", goals_avg);
                new_avg_editor.putFloat("shots_avg", shots_avg);
                new_avg_editor.putFloat("assists_avg", assists_avg);
                new_avg_editor.putFloat("saves_avg", saves_avg);
                new_avg_editor.putFloat("fouls_avg", fouls_avg);
                new_avg_editor.putFloat("tackles_avg", tackles_avg);
                new_avg_editor.putFloat("passes_avg", passes_avg);
                new_avg_editor.putInt("gameCounter", gameCounter);
                new_avg_editor.commit();

                try {
                    fos = openFileOutput("average", Context.MODE_PRIVATE);
                    PrintWriter pw = new PrintWriter(fos);
                    Map<String, ?> prefsMap = new_avg_prefs.getAll();
                    for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
                        pw.println(entry.getKey() + ": " + entry.getValue().toString());
                    }

                    pw.close();
                    fos.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, curr_filename + " Saved", Toast.LENGTH_LONG).show();

                gameCounter++;
                curr_filename = "Game" + Integer.toString(gameCounter);
            }
        }

        String fn = "Game"+i;
        SharedPreferences sp = getSharedPreferences(fn, 0);
        if(sp.contains(HOME)) {
            home= sp.getInt(HOME,0);
            away = sp.getInt(AWAY,0);
            goals = sp.getInt(GOALS,0);
            shots = sp.getInt(SHOTS,0);
            assists = sp.getInt(ASSISTS,0);
            saves = sp.getInt(SAVES,0);
            fouls = sp.getInt(FOULS,0);
            tackles = sp.getInt(TACKLES,0);
            passes = sp.getInt(PASSES,0);
            homeText = (TextView)findViewById(R.id.text_home);
            homeText.setText(Integer.toString(home));
            awayText = (TextView)findViewById(R.id.text_away);
            awayText.setText(Integer.toString(away));
            goalsText = (TextView)findViewById(R.id.text_goal);
            goalsText.setText(Integer.toString(goals));
            shotsText = (TextView)findViewById(R.id.text_shot);
            shotsText.setText(Integer.toString(shots));
            assistsText = (TextView)findViewById(R.id.text_assist);
            assistsText.setText(Integer.toString(assists));
            savesText = (TextView)findViewById(R.id.text_save);
            savesText.setText(Integer.toString(saves));
            tacklesText = (TextView)findViewById(R.id.text_tackle);
            tacklesText.setText(Integer.toString(tackles));
            foulsText = (TextView)findViewById(R.id.text_foul);
            foulsText.setText(Integer.toString(fouls));
            passesText = (TextView)findViewById(R.id.text_pass);
            passesText.setText(Integer.toString(passes));
            loaded = true;
            plusHome.setClickable(false);
            minusHome.setClickable(false);
            plusAway.setClickable(false);
            minusAway.setClickable(false);
            plusGoal.setClickable(false);
            minusGoal.setClickable(false);
            plusShot.setClickable(false);
            minusShot.setClickable(false);
            plusAssist.setClickable(false);
            minusAssist.setClickable(false);
            plusSave.setClickable(false);
            minusSave.setClickable(false);
            plusFoul.setClickable(false);
            minusFoul.setClickable(false);
            plusTackle.setClickable(false);
            minusTackle.setClickable(false);
            plusPass.setClickable(false);
            minusPass.setClickable(false);
            Toast.makeText(MainActivity.this, fn + " Loaded", Toast.LENGTH_SHORT).show();
        }

    }

    public void show_options(){
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(MainActivity.this, menu);
        popup.getMenu().add("New Game");
        popup.getMenu().add("Load Game");
        popup.getMenu().add("Reset Data");

        final Context self=this;
        /* Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());
        */
    //registering popup with OnMenuItemClickListener
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getTitle().toString()) {
                case "New Game":
                    if(!loaded) {
                        Toast.makeText(MainActivity.this, curr_filename + " Saved", Toast.LENGTH_SHORT).show();
                        SharedPreferences prefs = getSharedPreferences(curr_filename, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(HOME, home);
                        editor.putInt(AWAY, away);
                        editor.putInt(GOALS, goals);
                        editor.putInt(SHOTS, shots);
                        editor.putInt(ASSISTS, assists);
                        editor.putInt(SAVES, saves);
                        editor.putInt(FOULS, fouls);
                        editor.putInt(TACKLES, tackles);
                        editor.putInt(PASSES, passes);
                        editor.commit();

                        try {
                            fos = openFileOutput(curr_filename, Context.MODE_PRIVATE);
                            PrintWriter pw = new PrintWriter(fos);
                            Map<String, ?> prefsMap = prefs.getAll();
                            for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
                                pw.println(entry.getKey() + ": " + entry.getValue().toString());
                            }
                            pw.close();
                            fos.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        SharedPreferences new_avg_prefs = getSharedPreferences("average", Context.MODE_PRIVATE);
                        SharedPreferences.Editor new_avg_editor = new_avg_prefs.edit();
                        float home_avg = (new_avg_prefs.getFloat("home_avg", -1) * (gameCounter - 1) + home) / gameCounter;
                        float away_avg = (new_avg_prefs.getFloat("away_avg", -1) * (gameCounter - 1) + away) / gameCounter;
                        float goals_avg = (new_avg_prefs.getFloat("goals_avg", -1) * (gameCounter - 1) + goals) / gameCounter;
                        float shots_avg = (new_avg_prefs.getFloat("shots_avg", -1) * (gameCounter - 1) + shots) / gameCounter;
                        float assists_avg = (new_avg_prefs.getFloat("assists_avg", -1) * (gameCounter - 1) + assists) / gameCounter;
                        float saves_avg = (new_avg_prefs.getFloat("saves_avg", -1) * (gameCounter - 1) + saves) / gameCounter;
                        float fouls_avg = (new_avg_prefs.getFloat("fouls_avg", -1) * (gameCounter - 1) + fouls) / gameCounter;
                        float tackles_avg = (new_avg_prefs.getFloat("tackles_avg", -1) * (gameCounter - 1) + tackles) / gameCounter;
                        float passes_avg = (new_avg_prefs.getFloat("passes_avg", -1) * (gameCounter - 1) + passes) / gameCounter;
                        new_avg_editor.putFloat("home_avg", home_avg);
                        new_avg_editor.putFloat("away_avg", away_avg);
                        new_avg_editor.putFloat("goals_avg", goals_avg);
                        new_avg_editor.putFloat("shots_avg", shots_avg);
                        new_avg_editor.putFloat("assists_avg", assists_avg);
                        new_avg_editor.putFloat("saves_avg", saves_avg);
                        new_avg_editor.putFloat("fouls_avg", fouls_avg);
                        new_avg_editor.putFloat("tackles_avg", tackles_avg);
                        new_avg_editor.putFloat("passes_avg", passes_avg);
                        new_avg_editor.putInt("gameCounter", gameCounter);
                        new_avg_editor.commit();

                        try {
                            fos = openFileOutput("average", Context.MODE_PRIVATE);
                            PrintWriter pw = new PrintWriter(fos);
                            Map<String, ?> prefsMap = new_avg_prefs.getAll();
                            for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
                                pw.println(entry.getKey() + ": " + entry.getValue().toString());
                            }

                            pw.close();
                            fos.close();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        gameCounter++;
                        curr_filename = "Game" + Integer.toString(gameCounter);
                    }
                    gameText = (TextView)findViewById(R.id.curr_game);
                    gameText.setText("Game " + gameCounter);
                    loaded = false;
                    plusHome.setClickable(true);
                    minusHome.setClickable(true);
                    plusAway.setClickable(true);
                    minusAway.setClickable(true);
                    plusGoal.setClickable(true);
                    minusGoal.setClickable(true);
                    plusShot.setClickable(true);
                    minusShot.setClickable(true);
                    plusAssist.setClickable(true);
                    minusAssist.setClickable(true);
                    plusSave.setClickable(true);
                    minusSave.setClickable(true);
                    plusFoul.setClickable(true);
                    minusFoul.setClickable(true);
                    plusTackle.setClickable(true);
                    minusTackle.setClickable(true);
                    plusPass.setClickable(true);
                    minusPass.setClickable(true);

                    home = 0;
                    homeText.setText(Integer.toString(home));
                    away = 0;
                    awayText.setText(Integer.toString(away));
                    goals = 0;
                    goalsText.setText(Integer.toString(goals));
                    shots = 0;
                    shotsText.setText(Integer.toString(shots));
                    assists = 0;
                    assistsText.setText(Integer.toString(assists));
                    saves = 0;
                    savesText.setText(Integer.toString(saves));
                    fouls = 0;
                    foulsText.setText(Integer.toString(fouls));
                    tackles = 0;
                    tacklesText.setText(Integer.toString(tackles));
                    passes = 0;
                    passesText.setText(Integer.toString(passes));
                    break;
                case "Load Game":
                    load_a_game();
                    break;
                case "Reset Data":
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(self);
                    //builderSingle.setIcon(R.drawable.ic_launcher);
                    builderSingle.setTitle("This will clear all previous data. Are you sure you want to continue?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String[] files = fileList();
                            for (int i = 0; i < files.length; i++) {
                                deleteFile(files[i]);
                                SharedPreferences reset = getSharedPreferences(files[i], Context.MODE_PRIVATE);
                                SharedPreferences.Editor reset_sp = reset.edit();
                                reset_sp.clear();
                                reset_sp.commit();
                            }

                            curr_filename = "Game" + 1;
                            gameCounter = 1;
                            gameText = (TextView) findViewById(R.id.curr_game);
                            gameText.setText("Game " + gameCounter);
                            loaded = false;
                            plusHome.setClickable(true);
                            minusHome.setClickable(true);
                            plusAway.setClickable(true);
                            minusAway.setClickable(true);
                            plusGoal.setClickable(true);
                            minusGoal.setClickable(true);
                            plusShot.setClickable(true);
                            minusShot.setClickable(true);
                            plusAssist.setClickable(true);
                            minusAssist.setClickable(true);
                            plusSave.setClickable(true);
                            minusSave.setClickable(true);
                            plusFoul.setClickable(true);
                            minusFoul.setClickable(true);
                            plusTackle.setClickable(true);
                            minusTackle.setClickable(true);
                            plusPass.setClickable(true);
                            minusPass.setClickable(true);

                            home = 0;
                            homeText.setText(Integer.toString(home));
                            away = 0;
                            awayText.setText(Integer.toString(away));
                            goals = 0;
                            goalsText.setText(Integer.toString(goals));
                            shots = 0;
                            shotsText.setText(Integer.toString(shots));
                            assists = 0;
                            assistsText.setText(Integer.toString(assists));
                            saves = 0;
                            savesText.setText(Integer.toString(saves));
                            fouls = 0;
                            foulsText.setText(Integer.toString(fouls));
                            tackles = 0;
                            tacklesText.setText(Integer.toString(tackles));
                            passes = 0;
                            passesText.setText(Integer.toString(passes));

                            SharedPreferences avg_prefs2 = getSharedPreferences("average", Context.MODE_PRIVATE);
                            SharedPreferences.Editor avg_editor2 = avg_prefs2.edit();

                            avg_editor2.putInt("gameCounter", 1);
                            avg_editor2.putFloat("home_avg", 0);
                            avg_editor2.putFloat("away_avg", 0);
                            avg_editor2.putFloat("goals_avg", 0);
                            avg_editor2.putFloat("shots_avg", 0);
                            avg_editor2.putFloat("assists_avg", 0);
                            avg_editor2.putFloat("saves_avg", 0);
                            avg_editor2.putFloat("fouls_avg", 0);
                            avg_editor2.putFloat("tackles_avg", 0);
                            avg_editor2.putFloat("passes_avg", 0);
                            avg_editor2.commit();

                            try {
                                fos = openFileOutput("average", Context.MODE_PRIVATE);
                                PrintWriter pw = new PrintWriter(fos);
                                Map<String, ?> prefsMap = avg_prefs2.getAll();
                                for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
                                    pw.println(entry.getKey() + ": " + entry.getValue().toString());
                                }

                                pw.close();
                                fos.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.show();
                    break;
            }
            return true;
        }
    });

    popup.show(); //showing popup menu
}
}
