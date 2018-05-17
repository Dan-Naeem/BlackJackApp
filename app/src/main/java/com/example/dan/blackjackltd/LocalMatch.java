package com.example.dan.blackjackltd;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    //facedown
    String faceDown = "?? ";

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
    final static String allLost = "Everyone Lost";
    final static String p1Won = "P1 Won";
    final static String p2Won = "P2 Won";
    final static String dealerWon = "dealer Won";
    final static String twoTie = "2-Way Tie";
    final static String threeTie = "3-Way Tie";

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

        //enable button click functionality
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

        //start game
        next.setText("Click below to Start the Game");
        next.setBackgroundResource(R.color.active);

    }

    //in prg
    public void onPressNext() {
        //if game has not ended
        if (prgrmState != stateEnded){
            //if state is in next
            if (prgrmState == stateNext) {
                //increment pressNext
                pressNext += 1;

                // ** 1: deal cards to dealer and players, change state to p1
                if (pressNext == 1){

                    //set up deck
                    deck = new int[52];
                    for (int i = 0; i < 52; i++) {
                        deck[i] = 0;
                    }

                    //PLAYER 1 - deal 2 cards
                    for (int i = 0; i < 2; i++) {

                        //create variables to store values
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
                    }//end player 1

                    //check for player == 21
                    if (p1Total == 21){
                        // youWon();
                    }

                    //check 2 aces drawn (player1)
                    if (p1Ace == 2) {
                        //lower player total
                        p1Total = 12;
                        //lower player ace
                        p1Ace = 1;
                    }

                    //PLAYER 2 - deal 2 cards, one face down
                    for (int i = 0; i < 2; i++) {

                        //create variables to store values
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

                        //hide first card
                        if (i == 0){
                            //add to p2hidden
                            p2Hidden += cardFace  + " ";
                        }
                        //show second card
                        else {
                            //add to p2hand
                            p2Hand += cardFace + " ";
                        }

                        //add to player total
                        p2Total += cardValue;
                    }//end player 2

                    //check for player == 21
                    if (p2Total == 21){
                        // youWon();
                    }

                    //check 2 aces drawn (player1)
                    if (p2Ace == 2) {
                        //lower player total
                        p2Total = 12;
                        //lower player ace
                        p2Ace = 1;
                    }

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
                        if ( cardFace.substring(0, 1).equals("A") ){
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

                    //check 2 aces drawn (dealer)
                    if (dealerAce == 2) {
                        //lower dealer total
                        dealerTotal = 12;
                        //lower dealer ace
                        dealerAce = 1;
                    }

                    //set text
                    info.setText("Player 1's Turn");
                    next.setText("PLAYER 1 - GO");

                    //update values
                    dealerHandTxt.setText(faceDown + dealerHand);
                    p1HandTxt.setText(p1Hand);
                    p2HandTxt.setText(faceDown + p2Hand);

                    //change button colors
                    next.setBackgroundResource(R.color.item);
                    hit.setBackgroundResource(R.color.active);
                    stay.setBackgroundResource(R.color.active);

                    //change state to hit_stay
                    prgrmState = stateP1;

                }

                // ** 2 reveal p2 hidden, change state to p2
                if (pressNext == 2){
                    //reveal p2 hand
                    p2Hand = p2Hidden + p2Hand;
                    p2HandTxt.setText(p2Hand);

                    //set text
                    info.setText("Player 2's Turn");
                    next.setText("PLAYER 2 - GO");

                    //change button colors
                    next.setBackgroundResource(R.color.item);
                    hit.setBackgroundResource(R.color.active);
                    stay.setBackgroundResource(R.color.active);

                    //change state to hit_stay
                    prgrmState = stateP2;
                }

                // ** 3 if any player is still in the game, dealer plays
                if (pressNext == 3){
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
                        if ( cardFace.substring(0, 1).equals("A") ){
                            //add to ace count
                            dealerAce += 1;
                        }

                        //add to dealer hand and value
                        dealerHand += cardFace + " ";
                        dealerTotal += cardValue;

                        //if the dealer busts and has an ace
                        if (dealerTotal > 21 && dealerAce > 0){
                            //lower dealer total
                            dealerTotal -= 10;
                            //lower dealer ace count
                            dealerAce -= 1;
                        }
                    }

                    //show dealer's hand
                    dealerHandTxt.setText(faceDown + dealerHand);

                    //after the dealer stops, go to end game
                    prgrmState = stateEnded;
                    next.setText("Results");
                }
            }//end if state next

            //else if either of the players are playing
            else{
                //mention hit or stay
            }
        }// end if game has not ended

        //else if state  ended
        else if (prgrmState == stateEnded) {
            //while game is still active, determine winner and post score
            if (gameStatus == inPrg) {

                //if p1 && p2 lost
                if (p1Total > 21 && p2Total > 21) {
                    //if dealer bust
                    if (dealerTotal > 21) {
                        //everyone lost
                        allLost();
                    }
                    //else if dealer is still in
                    else {
                        //dealer won
                        dealerWon();
                    }
                }

                //if p1 lost
                else if (p1Total > 21) {
                    //if dealer bust
                    if (dealerTotal > 21) {
                        //p2won
                        p2Won();
                    }
                    //else if dealer is still in the game
                    else {
                        //p2 beat dealer
                        if (p2Total > dealerTotal) {
                            //p2won
                            p2Won();
                        }
                        //dealer beat p2
                        else if (dealerTotal > p2Total) {
                            //dealer won
                            dealerWon();
                        }
                        //dealer == p2
                        else {
                            //2 way tie
                            twoTie();
                        }
                    }
                }

                //if p2 lost
                else if (p2Total > 21) {
                    //if dealer bust
                    if (dealerTotal > 21) {
                        //p1won
                        p1Won();
                    }
                    //else if dealer is still in the game
                    else {
                        //p1 beat dealer
                        if (p1Total > dealerTotal) {
                            //p1won
                            p1Won();
                        }
                        //dealer beat p1
                        if (dealerTotal > p1Total) {
                            //dealer won
                            dealerWon();
                        }
                        //dealer == p1
                        else {
                            //2 way tie
                            twoTie();
                        }
                    }
                }

                //if both players are still in
                else {
                    //dealer bust
                    if (dealerTotal > 21) {
                        //p1 > p2
                        if (p1Total > p2Total) {
                            //p1won
                            p1Won();
                        }
                        //p2 > p1
                        else if (p2Total > p1Total) {
                            //p2won
                            p2Won();
                        }
                        //p1 == p2
                        else {
                            //2 way tie
                            twoTie();
                        }
                    }
                    //dealer didnt bust, all 3 are still in the game
                    else {
                        //if p1 beat p2 and dealer
                        if (p1Total > p2Total && p1Total > dealerTotal) {
                            //p1won
                            p1Won();
                        }
                        //if p2 beat p1 and dealer
                        if (p2Total > p1Total && p2Total > dealerTotal) {
                            //p2won
                            p2Won();
                        }
                        //if dealer beat p1 and p2
                        if (dealerTotal > p1Total && dealerTotal > p2Total) {
                            //dealer won
                            dealerWon();
                        }
                        //if 3 way tie
                        if (p1Total == p2Total && p1Total == dealerTotal) {
                            //3 way tie
                            threeTie();
                        }
                        //else other 2 way ties
                        else {
                            //2 way tie
                            twoTie();
                        }
                    }
                }

                //show the dealers hand
                dealerHandTxt.setText(dealerHidden + dealerHand);
            }
        }
    }//end on press next

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
                        //now its player 2's turn

                        //change text
                        next.setText("Next");

                        //change button colors
                        next.setBackgroundResource(R.color.active);
                        hit.setBackgroundResource(R.color.item);
                        stay.setBackgroundResource(R.color.item);

                        //change state to next
                        prgrmState = stateNext;
                    }
                }//end if >21

                //check if player == 21
                else if (p1Total == 21){
                    //now its player 2's turn

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
                        //dealer's turn

                        //change text
                        next.setText("Next");

                        //change button colors
                        next.setBackgroundResource(R.color.active);
                        hit.setBackgroundResource(R.color.item);
                        stay.setBackgroundResource(R.color.item);

                        //change state to next
                        prgrmState = stateNext;
                    }
                }//end if >21

                //check if player == 21
                else if (p2Total == 21){
                    //dealers turn

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

                info.setText("P2 chose stay");

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

    public void allLost(){
        prgrmState = stateEnded;
        gameStatus = allLost;
        info.setText(allLost);
        next.setText("Game Over");

        //change button colors
        next.setBackgroundResource(R.color.item);
        hit.setBackgroundResource(R.color.item);
        stay.setBackgroundResource(R.color.item);

        postResults();
    }

    public void p1Won(){
        prgrmState = stateEnded;
        gameStatus = p1Won;
        info.setText(p1Won);
        next.setText("Game Over");

        //change button colors
        next.setBackgroundResource(R.color.item);
        hit.setBackgroundResource(R.color.item);
        stay.setBackgroundResource(R.color.item);

        postResults();
    }

    public void p2Won(){
        prgrmState = stateEnded;
        gameStatus = p2Won;
        info.setText(p2Won);
        next.setText("Game Over");

        //change button colors
        next.setBackgroundResource(R.color.item);
        hit.setBackgroundResource(R.color.item);
        stay.setBackgroundResource(R.color.item);

        postResults();
    }

    public void dealerWon(){
        prgrmState = stateEnded;
        gameStatus = dealerWon;
        info.setText(dealerWon);
        next.setText("Game Over");

        //change button colors
        next.setBackgroundResource(R.color.item);
        hit.setBackgroundResource(R.color.item);
        stay.setBackgroundResource(R.color.item);

        postResults();
    }

    public void twoTie(){
        prgrmState = stateEnded;
        gameStatus = twoTie;
        info.setText(twoTie);
        next.setText("Game Over");

        //change button colors
        next.setBackgroundResource(R.color.item);
        hit.setBackgroundResource(R.color.item);
        stay.setBackgroundResource(R.color.item);

        postResults();
    }

    public void threeTie() {
        prgrmState = stateEnded;
        gameStatus = threeTie;
        info.setText(threeTie);
        next.setText("Game Over");

        //change button colors
        next.setBackgroundResource(R.color.item);
        hit.setBackgroundResource(R.color.item);
        stay.setBackgroundResource(R.color.item);

        postResults();
    }

    public void postResults() {

        //add values to myDb
        boolean didWork = myDb.insertData("Multi", gameStatus, p1Total, p2Total, dealerTotal);

        //display a message
        if (didWork== true)
            Toast.makeText(LocalMatch.this, "Data Inserted", Toast.LENGTH_LONG).show();
        else

            Toast.makeText(LocalMatch.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
    }
}






























