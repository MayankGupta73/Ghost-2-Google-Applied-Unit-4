/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    int userScore = 0,compScore = 0;

    Button btnChallenge ,btnReset;
    TextView text,label,tvUserScore,tvCompScore;
    SimpleDictionary simpleDictionary;
    FastDictionary fastDictionary;

    public static final String TAG = "ghost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        btnChallenge = (Button) findViewById(R.id.btnChallenge);
        btnReset = (Button) findViewById(R.id.btnReset);
        tvUserScore = (TextView) findViewById(R.id.tvUserScore);
        tvCompScore = (TextView) findViewById(R.id.tvCompScore);

        AssetManager assetManager = getAssets();
        try {
//             simpleDictionary = new SimpleDictionary(assetManager.open("words.txt"));
            fastDictionary = new FastDictionary(assetManager.open("words.txt"));
            onStart(null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        btnChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userTurn)
                    return;
                String word = text.getText().toString().trim();
                if(word.length()>=4 && fastDictionary.isWord(word)){
                    label.setText("User Wins. This is already a word.");
                    btnChallenge.setEnabled(false);
                    userScore++;
                    updateScore();
                }
                else {
                    String correctWord = fastDictionary.getAnyWordStartingWith(word);
                    if(correctWord == null){
                        label.setText("User Wins. No word can be made.");
                        btnChallenge.setEnabled(false);
                        userScore++;
                        updateScore();
                    }
                    else {
                        text.setText(correctWord);
                        label.setText("Computer Wins. A word can be made");
                        btnChallenge.setEnabled(false);
                        compScore++;
                        updateScore();
                    }
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChallenge.setEnabled(true);
                btnReset.setEnabled(true);
                text.setText("");
                userTurn = random.nextBoolean();
                text.setText("");
                text.setFocusable(true);
                if (userTurn) {
                    label.setText(USER_TURN);
                    userTurn = true;
                } else {
                    label.setText(COMPUTER_TURN);
                    computerTurn();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
            userTurn = true;
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        String word = text.getText().toString().trim();
        btnChallenge.setEnabled(false);
        btnReset.setEnabled(false);
        userTurn = false;
        if(fastDictionary.isWord(word) && (word.length()>=4)){
            label.setText("Computer Wins. This is a word already");
            compScore++;
            updateScore();
            btnReset.setEnabled(true);
            return;
        }
        else{
            String correctWord;
            correctWord = fastDictionary.getAnyWordStartingWith(word);
            Log.d(TAG, "computerTurn: word "+word);
            Log.d(TAG, "computerTurn: correctWord "+correctWord);
            if(correctWord == null){
                label.setText("Computer Wins. No word can be formed!");
                btnChallenge.setEnabled(false);
                compScore++;
                updateScore();
                btnReset.setEnabled(true);
                return;
            }
            else {
                char n = correctWord.charAt(word.length());
                String newWord = word + n;
                text.setText(newWord);
            }
        }

        userTurn = true;
        label.setText(USER_TURN);
        btnChallenge.setEnabled(true);
        btnReset.setEnabled(true);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char key = (char) event.getUnicodeChar();
        if(Character.isLetter(key)){
            String word = text.getText().toString() + String.valueOf(key);
            text.setText(word);
            /*if(simpleDictionary.isWord(word)){
                label.setText("Is a correct word");
            }*/
            computerTurn();
        }

        return super.onKeyUp(keyCode, event);
    }

    void updateScore(){
        tvUserScore.setText("User Score: "+userScore);
        tvCompScore.setText("Computer Score: "+compScore);
    }
}
