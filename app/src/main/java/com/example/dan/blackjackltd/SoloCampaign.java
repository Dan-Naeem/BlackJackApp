package com.example.dan.blackjackltd;

import android.app.Activity;
import android.graphics.Color;
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

    //state: control state of the program and status of game
    String prgrmState, gameStatus;

    //states and game status
            //*** STATE >>> prgrmState ***
    //active
    final static String stateNext = "press next";
    final static String stateHitStay = "press hit or stay";
    //end
    final static String stateEnded = "Game Has Ended";
            //*** GAME >>> gameStatus ***
    //active
    final static String inPrg = "In Progress";
    //end
    final static String Win = "You Won";
    final static String Lose = "You Lost";
    final static String Draw = "Draw";

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

        //set up state of the program and game status
        prgrmState = stateNext;
        gameStatus = inPrg;



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

        hit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //call pressNext method
                onPressHit();

            }//end onClick
        });//end listener

        stay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //call pressNext method
                onPressStay();

            }//end onClick
        });//end listener
    }//end onCreate

    public void onPressNext(){
        //if game has not ended
        if (prgrmState != stateEnded) {
            //if state is in next
            if (prgrmState == stateNext) {
                //increment pressNext
                pressNext += 1;

                // ** 1: deal cards to dealer and player, change state to to hit/stay
                if (pressNext == 1) {

                    //set up deck
                    deck = new int[52];
                    for (int i = 0; i < 52; i++) {
                        deck[i] = 0;
                    }

                    //deal 2 cards to the player
                    for (int i = 0; i < 2; i++) {

                        //create variables to store values
                        String cardFace = "";
                        int cardValue = 0;

                        //draw a card
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

                    //deal 2 cards to the dealer, one face down and one face up
                    for (int i = 0; i < 2; i++) {

                        //create variables to store values
                        String cardFace = "";
                        int cardValue = 0;

                        //draw a card
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
                            dealerHidden = cardFace + " ";
                        }
                        //show second card
                        else {
                            //add to dealer hand
                            dealerHand += cardFace + " ";
                        }

                        //add to dealer total
                        dealerTotal += cardValue;
                    }//end dealer for

                    //set text
                    info.setText("Dealer has Dealed");
                    next.setText("Your Turn");

                    //update values
                    dealerHandTxt.setText(faceDown + dealerHand);
                    playerHandTxt.setText(playerHand);

                    //check for player == 21
                    if (playerTotal == 21){
                        youWon();
                    }

                    //check 2 aces drawn

                    //change state to hit_stay
                    prgrmState = stateHitStay;

                }

                // ** 2 after player hits stay and is still in the game, allow dealer to play
                else if (pressNext == 2) {
                    //dealer hit while < 17
                    info.setText("Dealer's Turn");
                    while (dealerTotal < 17){
                        String cardFace = "";
                        int cardValue = 0;

                        //draw a card
                        Pair<String, Integer> aCard = drawCard();
                        cardFace = aCard.first;
                        cardValue = aCard.second;

                        //if an ace was drawn
                        if (cardFace.substring(0, 1) == "A"){
                            //add to ace count
                            dealerAce += 1;
                        }

                        //add to dealer hand and value
                        dealerHand += cardFace + "";
                        dealerTotal += cardValue;

                        //if the dealer busts and has an ace
                            //lower ace to 1
                    }

                    //show dealer's hand
                    dealerHandTxt.setText(faceDown + dealerHand);

                    //after the dealer stops, go to end game
                    prgrmState = stateEnded;
                    next.setText("Results");
                }
            }// end if state = next

            //if state is in hit stay
            else if (prgrmState == stateHitStay) {
                info.setText(stateHitStay);
            }//end if state: HitStay
        }//end if game has not ended

        //else if state ended
        else if (prgrmState == stateEnded) {

            //if the results aren't in yet
            if (gameStatus == inPrg){
                //compare values, declare ruling
                if (dealerTotal > 21){
                    youWon();
                }
                else if (dealerTotal > playerTotal){
                    youLost();
                }
                else if (dealerTotal == playerTotal){
                    gameDraw();
                }
                else if (dealerTotal < playerTotal){
                    youWon();
                }

                //show the dealers hand
                dealerHandTxt.setText(dealerHidden + dealerHand);
            }

        }
    }//end onPressNext()

    public void onPressHit() {
        // if game has not ended
        if (prgrmState != stateEnded) {

            //if state is in next
            if (prgrmState == stateNext) {

                info.setText(stateNext);
            }

            //else if state Hit/Stay
            else if (prgrmState == stateHitStay) {

                //Hit, deal a card to the player
                String cardFace = "";
                int cardValue = 0;

                //draw a card
                Pair<String, Integer> aCard = drawCard();
                cardFace = aCard.first;
                cardValue = aCard.second;

                //if an ace was drawn
                if (cardFace.substring(0, 1) == "A"){
                    //add to ace count
                    playerAce += 1;
                }

                //add to player hand
                playerHand += cardFace + " ";
                //add to player total
                playerTotal += cardValue;

                //display player hand
                playerHandTxt.setText(playerHand);

                //if player over 21
                if (playerTotal > 21){

                    //check for an ace
                    if (playerAce > 0){
                        info.setText("PLayer Has an Ace");
                    }
                    //else if >21 and no ace
                    else {
                        //Game Over, you lose
                        youLost();
                    }
                }//end if >21

                //check if player == 21
                else if (playerTotal == 21){
                    //Game Over, you won
                    youWon();
                }
            }
        }

        //else if game had ended
        else {
            info.setText(stateEnded);
        }
    }

    public void onPressStay() {
        // if game has not ended
        if (prgrmState != stateEnded) {

            if (prgrmState == stateNext){

                info.setText(stateNext);
            }

            else if (prgrmState == stateHitStay) {

                info.setText("You chose stay");

                //change state to next
                prgrmState = stateNext;

                //change text
                next.setText("Next");
            }
        }

        //else if game had ended
        else {
            info.setText(stateEnded);
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

    public void youWon() {
        prgrmState = stateEnded;
        gameStatus = Win;
        info.setText(Win);
        next.setText("Game Over");
    }

    public void youLost() {
        prgrmState = stateEnded;
        gameStatus = Lose;
        info.setText(Lose);
        next.setText("Game Over");
    }

    public void gameDraw() {
        prgrmState = stateEnded;
        gameStatus = Draw;
        info.setText(Draw);
        next.setText("Game Over");
    }

    public void me() {
        info.setText("pressNext = " + pressNext);
    }



}
