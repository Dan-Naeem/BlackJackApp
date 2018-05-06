package com.example.dan.blackjackltd;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created by DAN on 3/29/18.
 */

public class SoloCampaign extends Activity {

    TextView dealerHandTxt, playerHandTxt, info;
    Button hit, stay, next;

    //keep track of button clicks
    int pressNext, pressHit, pressStay;

    //deck of cards
    int deck[];

    //player value
    int playerTotal, playerAce;
    String playerHand;

    //dealer values
    int dealerTotal, dealerAce;
    String dealerHand, dealerHidden, faceDown;

    //state: control state of the program
    String prgrmState;

    //predefined states
    final static String stateNext = "press next";
    final static String stateHitStay = "press hit or stay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solo_campaign);

        //set up text views
        dealerHandTxt = (TextView) findViewById(R.id.txtDealerHand);
        playerHandTxt = (TextView) findViewById(R.id.txtPlayerHand);
        info = (TextView) findViewById(R.id.txtInfo);

        dealerHandTxt.setText("hand info");
        playerHandTxt.setText("hand info");
        info.setText("Welcome to BlackJack Ltd");

        //set up buttons
        next = (Button) findViewById(R.id.btnNext);
        hit = (Button) findViewById(R.id.btnSoloHit);
        stay = (Button) findViewById(R.id.btnSoloStay);

        //set up button clicks
        pressNext = 0;
        pressHit = 0;
        pressStay = 0;

        //set up player values
        playerTotal = 0;
        playerHand = "";
        playerAce = 0;

        //set up dealer values
        dealerTotal = 0;
        dealerHand = "";
        dealerAce = 0;
        dealerHidden = "";
        faceDown = "?? ";

        //set up state of the program
        prgrmState = stateNext;


        Button myButton = new Button(this);
        myButton.setText("Push Me!");

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                me();
            }
        });

        ConstraintLayout ll= (ConstraintLayout) findViewById(R.id.solo_layout);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, R.id.txtInfo);
        ll.addView(myButton);


        next.setText("Click below to Start the Game");

        next.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {

                 //call pressNext method
                 onPressNext();

             }//end onClick
        });//end listener
    }

    public void onPressNext(){
        //if state is in next
        if (prgrmState == stateNext) {

            //increment pressNext
            pressNext += 1;

            if (pressNext == 1) {
                info.setText("Setting Up Deck");
                next.setText("Next");

                dealerHandTxt.setText("---");
                playerHandTxt.setText("---");

                //set up deck
                deck = new int[52];
                for (int i = 0; i < 52; i++) {
                    deck[i] = 0;
                }
            }

            else if (pressNext == 2) {
                info.setText("Dealer has Dealed");

                //deal 2 cards to the player
                for (int i = 0; i < 2; i++) {
                    String cardFace = "";
                    int cardValue = 0;

                    Pair<String, Integer> aCard = drawCard();
                    cardFace = aCard.first;
                    cardValue = aCard.second;

                    //if an ace was drawn
                    if (cardFace.substring(0, 1) == "A") {
                        //add to ace count
                        playerAce += 1;
                    }

                    //add to player hand
                    playerHand += cardFace + " ";
                    //add to player total
                    playerTotal += cardValue;
                }//end player for

                //deal 2 cards to the dealer
                for (int i = 0; i < 2; i++) {
                    String cardFace = "";
                    int cardValue = 0;

                    Pair<String, Integer> aCard = drawCard();
                    cardFace = aCard.first;
                    cardValue = aCard.second;

                    //if an ace was drawn
                    if (cardFace.substring(0, 1) == "A") {
                        //add to ace count
                        dealerAce += 1;
                    }

                    //hide first card
                    if (i == 0) {
                        //add to dealer hidden
                        dealerHidden = cardFace;
                    }
                    //show second
                    else {
                        //add to dealer hand
                        dealerHand += cardFace + " ";
                    }

                    //add to dealer total
                    dealerTotal += cardValue;
                }//end dealer for


                //update values
                dealerHandTxt.setText(faceDown + dealerHand);
                playerHandTxt.setText(playerHand);

                //check for player 21

                //check 2 aces drawn

                //change state to hit_stay
                prgrmState = stateHitStay;

            }
        }

        //if state is in hit stay
        else if (prgrmState == stateHitStay) {
            info.setText(stateHitStay);
        }



    }

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
            cardFace += 'A';
            cardValue += 11;
        }
        else if (num == 11) {
            cardFace += 'J';
            cardValue += 10;
        }
        else if (num == 12) {
            cardFace += 'Q';
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
    };

    public void me() {
        info.setText("pressNext = " + pressNext);
    }



}























