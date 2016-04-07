package com.example.dell.dice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import  android.os.Handler;
import java.util.Random;
import java.util.concurrent.TimeUnit;



public class MainActivity extends AppCompatActivity {

    TextView textViewStatus;
    Button bRoll,bHold,bReset;
    ImageView ivImage;
    private int USER_OVERALL_SCORE=0;
    private int USER_TURN_SCORE=0;
    private int COMP_OVERALL_SCORE=0;
    private int COMP_TURN_SCORE=0;
    Animation anim,anim2;
    private int DICE_IMAGES []={R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};
    Boolean userTurn=true;


    Handler handler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        anim=new TranslateAnimation(0.0f,200.0f,0.0f,0.0f);
        anim.setDuration(200);
        anim2=new TranslateAnimation(0.0f,-200.0f,0.0f,0.0f);
        anim2.setDuration(200);

        bRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RollButton();
            }
        });

        bHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HoldButton();
            }
        });

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetButton();
            }
        });

    }




    private  void initialize(){
        textViewStatus=(TextView) findViewById(R.id.textView2);
        bRoll=(Button) findViewById(R.id.button);
        bHold=(Button) findViewById(R.id.button2);
        bReset=(Button) findViewById(R.id.button3);
        ivImage=(ImageView) findViewById(R.id.imageView);
    }

    public void ResetButton(){
        USER_OVERALL_SCORE=0;
        USER_TURN_SCORE=0;
        COMP_OVERALL_SCORE=0;
        COMP_TURN_SCORE=0;

        textViewStatus.setText("Your Score: 0  Computer Score:0");
    }

    public void RollButton(){
        Random rand=new Random();
        int i=rand.nextInt(6)+1;
        ivImage.setImageResource(DICE_IMAGES[i - 1]);

        ivImage.startAnimation(anim);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivImage.setAnimation(null);
            }
        },200);

        if(i==1){

            USER_TURN_SCORE=0;
            try {
                ComputerTurn();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            userTurn=false;


        }
        else{
            USER_TURN_SCORE+=i;
           // USER_OVERALL_SCORE=USER_OVERALL_SCORE+USER_TURN_SCORE;

        }

        textViewStatus.setText("Your Score: " + USER_OVERALL_SCORE + " Your current Score: " + USER_TURN_SCORE + " Comp Curr Score: " + COMP_TURN_SCORE + " Computer Score " + COMP_OVERALL_SCORE);

        if((USER_OVERALL_SCORE+USER_TURN_SCORE)>=100){
            Toast.makeText(MainActivity.this,"You Won",Toast.LENGTH_LONG).show();
            ResetButton();
        }


    }

    public void HoldButton(){
        userTurn=false;
        USER_OVERALL_SCORE+= USER_TURN_SCORE;
        textViewStatus.setText("your Score: "+USER_OVERALL_SCORE+" "+"Computer Score: "+COMP_OVERALL_SCORE);
        USER_TURN_SCORE=0;
        COMP_TURN_SCORE=0;
        try {
            ComputerTurn();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void ComputerTurn() throws InterruptedException {

        if(userTurn){
            bRoll.setClickable(true);
            bHold.setClickable(true);
            return;
        }

        bRoll.setEnabled(false);
        bHold.setEnabled(false);


        Random Ra=new Random();
        int n=Ra.nextInt(6)+1;
        Log.d("COMPUTER TURN ","NO Of Turns "+ n);

        for(int i=1;i<=n;i++){
            final int j=Ra.nextInt(6)+1;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ivImage.setAnimation(null);
                    ivImage.setImageResource(DICE_IMAGES[j - 1]);
                    ivImage.setAnimation(anim2);
                }
            }, 1000);

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

            if(j==1){

                COMP_TURN_SCORE=0;
                break;


            }
            else{
                COMP_TURN_SCORE+=j;
                // USER_OVERALL_SCORE=USER_OVERALL_SCORE+USER_TURN_SCORE;

            }

            if((COMP_OVERALL_SCORE+COMP_TURN_SCORE)>=100){
                Toast.makeText(MainActivity.this,"You LOST",Toast.LENGTH_LONG).show();
                ResetButton();
            }
        }

       // textViewStatus.setText("Your Score: "+ USER_OVERALL_SCORE+ " Your current Score: "+USER_TURN_SCORE+" Comp Curr Score: "+COMP_TURN_SCORE+" Computer Score "+ COMP_OVERALL_SCORE);

        if((COMP_OVERALL_SCORE+COMP_TURN_SCORE)>=100){
            Toast.makeText(MainActivity.this,"You LOST",Toast.LENGTH_LONG).show();
            ResetButton();
        }

        COMP_OVERALL_SCORE+=COMP_TURN_SCORE;
        textViewStatus.setText("Comp Score: "+ COMP_OVERALL_SCORE+" "+" User Score:"+USER_OVERALL_SCORE);
        userTurn=true;
        bRoll.setEnabled(true);
        bHold.setEnabled(true);



    }
}



