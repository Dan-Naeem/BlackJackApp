package com.example.dan.blackjackltd;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by DAN on 5/16/18.
 */

public class LocalMatch extends Activity {

    DatabaseHelper myDb;

    TextView dealerHandTxt, p1HandTxt, p2HandTxt, info;
    Button hit, stay, next;

    //keep track of button clicks
    int pressNext, pressHit, pressStay;

    //deck of cards
    int deck[];

    //p1 values
    int p1Total, p1Ace;
    String p1Hand, p1Hidden;

    //p2 values
    int p2Total, p2Ace;
    String p2Hand, p2Hidden;

    //dealer values
    int dealerTotal, dealerAce;
    String dealerHand, dealerHidden;

    //hidden
    String faceDown = "??";

    //state: control state of the program and status of game
    String prgrmState, gameStatus;

    //states and game status
    //*** STATE >>> prgrmState ***
    //active
    final static String stateNext = "press next";
    final static String stateP1 = "P1: hit or stay";
    final static String stateP2 = "P2: hit or stay";
    //end
    final static String stateEnded = "Game Has Ended";
    //*** GAME >>> gameStatus ***
    //active
    final static String inPrg = "In Progress";
    //end
    final static String p1Win = "P1 Won";
    final static String p2Win = "P2 Won";
    final static String dealerWin = "dealer Won";
    final static String Draw = "Draw";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_match);
        myDb = new DatabaseHelper(this);

        //set up text views
        dealerHandTxt = (TextView) findViewById(R.id.localDealerHand);
        p1HandTxt = (TextView) findViewById(R.id.P1Hand);
        p2HandTxt = (TextView) findViewById(R.id.P2Hand);
        info = (TextView) findViewById(R.id.localInfo);

        //set up buttons
        next = (Button) findViewById(R.id.localNext);
        hit = (Button) findViewById(R.id.localHit);
        stay = (Button) findViewById(R.id.localStay);

        //set up button clicks
        pressNext = 0;

        //setup player values
        //p1
        p1Total = 0;
        p1Hand = "";
        p1Ace = 0;
        p1Hidden = "";
        //p2
        p2Total = 0;
        p2Hand = "";
        p2Ace = 0;
        p2Hidden = "";

        //set up dealer values
        dealerTotal = 0;
        dealerHand = "";
        dealerAce = 0;
        dealerHidden = "";

        //set up state of the program and game status
        prgrmState = stateNext;
        gameStatus = inPrg;

    }

    //in prg
    public void onPressNext() {
        //if game has not ended
        if (prgrmState != stateEnded){
            //
        }
    }

    //in done
    public void onPressHit() {
        //if game has not ended
        if (prgrmState != stateEnded){

            //if state is in next
            if (prgrmState == stateNext) {

                info.setText(stateNext);
            }

            //else if state is P1
            else if (prgrmState == stateP1){

                //Hit, deal a card to the player
                String cardFace = "";
                int cardValue = 0;

                //draw a card
                Pair<String, Integer> aCard = drawCard();
                cardFace = aCard.first;
                cardValue = aCard.second;

                //if an ace was drawn
                if ( cardFace.substring(0, 1).equals("A") ){
                    //add to ace count
                    p1Ace += 1;
                }

                //add to player hand
                p1Hand += cardFace + " ";
                //add to player total
                p1Total += cardValue;

                //display player hand
                p1HandTxt.setText(p1Hand);

                //if player over 21
                if (p1Total > 21){
                    //check for an ace
                    if (p1Ace > 0){
                        //lower total
                        p1Total -= 10;
                        //lower ace count
                        p1Ace -= 1;
                    }
                    //else if >21 and no ace
                    else {
                        //Game Over, you lose
                    }
                }//end if >21

                //check if player == 21
                else if (p1Total == 21){
                    //Game Over, you won
                }
            }


            //else if state is P2
            else if (prgrmState == stateP2){

                //Hit, deal a card to the player
                String cardFace = "";
                int cardValue = 0;

                //draw a card
                Pair<String, Integer> aCard = drawCard();
                cardFace = aCard.first;
                cardValue = aCard.second;

                //if an ace was drawn
                if ( cardFace.substring(0, 1).equals("A") ){
                    //add to ace count
                    p2Ace += 1;
                }

                //add to player hand
                p2Hand += cardFace + " ";
                //add to player total
                p2Total += cardValue;

                //display player hand
                p2HandTxt.setText(p2Hand);

                //if player over 21
                if (p2Total > 21){
                    //check for an ace
                    if (p2Ace > 0){
                        //lower total
                        p2Total -= 10;
                        //lower ace count
                        p2Ace -= 1;
                    }
                    //else if >21 and no ace
                    else {
                        //Game Over, you lose
                    }
                }//end if >21

                //check if player == 21
                else if (p2Total == 21){
                    //Game Over, you won
                }
            }
        }

        //else if game has ended
        else {
            info.setText(stateEnded);
        }
    }

    //done (maybe)
    public void onPressStay() {
        // if game has not ended
        if (prgrmState != stateEnded) {

            if (prgrmState == stateNext){

                info.setText(stateNext);
            }
            //else if p1
            else if (prgrmState == stateP1){

                info.setText("P1 chose stay");

                //change text
                next.setText("Next");

                //change button colors
                next.setBackgroundResource(R.color.active);
                hit.setBackgroundResource(R.color.item);
                stay.setBackgroundResource(R.color.item);

                //change state to next
                prgrmState = stateNext;
            }

            else if (prgrmState == stateP2){

                info.setText("P1 chose stay");

                //change text
                next.setText("Next");

                //change button colors
                next.setBackgroundResource(R.color.active);
                hit.setBackgroundResource(R.color.item);
                stay.setBackgroundResource(R.color.item);

                //change state to next
                prgrmState = stateNext;
            }
        }

        //else if game has ended
        else {
            info.setText(stateEnded);
        }
    }

    //done
    public Pair<String, Integer> drawCard() {

        //create a var to store cardFace and cardValue
        String cardFace = "";
        int cardValue = 0;

        //draw a card
        Random rand  = new Random();
        int randomCard  = rand.nextInt(52);

        //enter and stay in while loop if card is used
        while (deck[randomCard] == 1){

            //more up to another card
            randomCard += 7;

            //if > 52, subtract
            if ( randomCard >= 52){
                randomCard -= 52;
            }
        }

        //once an unused card is found, remove from deck
        deck[randomCard] = 1;

        //find suit and num
        int suit = randomCard / 13;
        int num = randomCard - ( suit * 13);

        //update card face and value
        // adjust cardFace
        if (num == 0) {
            cardFace += "K";
            cardValue += 10;
        }
        else if (num == 1) {
            cardFace += "A";
            cardValue += 11;
        }
        else if (num == 11) {
            cardFace += "J";
            cardValue += 10;
        }
        else if (num == 12) {
            cardFace += "Q";
            cardValue += 10;
        }
        else {
            cardFace += Integer.toString(num);
            cardValue += num;
        }

        //adjust suit
        if (suit == 0) {
            cardFace += 'D';
        }
        else if (suit == 1) {
            cardFace += 'C';
        }
        else if (suit == 2) {
            cardFace += 'H';
        }
        else if (suit == 3) {
            cardFace += 'S';
        }

        //store face and value in a pair
        Pair<String, Integer> card_Face_Val = new Pair<String, Integer>(cardFace, cardValue);

        return card_Face_Val;
    }
}






























