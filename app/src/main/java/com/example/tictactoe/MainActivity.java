package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int playersTurn = 1;
    int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    int counter = 0;
    TextView turnDisplay;
    boolean cancelled = false;
    Button resetButton;
    boolean playingComputer = false;
    int flag = 0;
    static int HUMAN = 1;
    static int AI = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { resetGame(view);}
        });

        openStartAlert();
    }

    public void onTap (View view) {
        ImageView image = (ImageView) view;
        int tapped = Integer.parseInt(image.getTag().toString());
        //returns so player cannot push any of the drawable images
        if(cancelled == true){
            return;
        }

        if(playingComputer == false) {
            //checks if spot tapped is open to place a marker
            if (gameState[tapped] == 0) {
                counter++;
                gameState[tapped] = playersTurn;

                if (playersTurn == 1) {
                    image.setImageResource(R.drawable.ximage);
                    playersTurn = 2;
                    turnDisplay = findViewById(R.id.turnDisplay);
                    turnDisplay.setText("O's Turn - Tap to Play");
                } else {
                    image.setImageResource(R.drawable.oimage);
                    playersTurn = 1;
                    turnDisplay = findViewById(R.id.turnDisplay);
                    turnDisplay.setText("X's Turn - Tap to Play");
                }
            }
        } else {
            if (gameState[tapped] == 0) {
                counter++;
                gameState[tapped] = playersTurn;

                if (playersTurn == 1) {
                    image.setImageResource(R.drawable.ximage);
                    playersTurn = 2;
                    turnDisplay = findViewById(R.id.turnDisplay);
                    turnDisplay.setText("Computer's Turn");
                    int[] newBoard = makeCopyOfBoard(gameState);
                    int number = computerMove(newBoard, playersTurn);
                    if (number >= 0 && gameState[number] == 0) {
                        counter++;
                        ImageView imageComputer;
                        gameState[number] = AI;
                        if (number == 0) {
                            imageComputer = findViewById(R.id.imageView0);
                        } else if (number == 1) {
                            imageComputer = findViewById(R.id.imageView1);
                        } else if (number == 2) {
                            imageComputer = findViewById(R.id.imageView2);
                        } else if (number == 3) {
                            imageComputer = findViewById(R.id.imageView3);
                        } else if (number == 4) {
                            imageComputer = findViewById(R.id.imageView4);
                        } else if (number == 5) {
                            imageComputer = findViewById(R.id.imageView5);
                        } else if (number == 6) {
                            imageComputer = findViewById(R.id.imageView6);
                        } else if (number == 7) {
                            imageComputer = findViewById(R.id.imageView7);
                        } else {
                            imageComputer = findViewById(R.id.imageView8);
                        }
                        imageComputer.setImageResource(R.drawable.oimage);
                        playersTurn = 1;
                        turnDisplay = findViewById(R.id.turnDisplay);
                        turnDisplay.setText("Your Turn - Tap to Play");
                    }
                }
            }
        }
            //checks for winners, then goes opens alert box if there is a winner
//            for (int[] winPosition : winPositions) {
//                if (gameState[winPosition[1]] == gameState[winPosition[2]] && gameState[winPosition[2]] == gameState[winPosition[0]] &&
//                        gameState[winPosition[1]] != 0) {
//                    flag = 1;
//                    if (gameState[winPosition[1]] == 1) {
//                        if(playingComputer == true){openAlertBox(view, "You won!");}
//                        else{openAlertBox(view, "X has won!");}
//                    } else {
//                        if(playingComputer == true){
//                            openAlertBox(view, "Computer won!");
//                        }
//                        else {openAlertBox(view, "O has won!");}
//                    }
//                }
//            }
        //checks for winners, then goes opens alert box if there is a winner
                if (winningPositions(HUMAN, gameState)) {
                    if(playingComputer){openAlertBox(view, "You won!");}
                    else{openAlertBox(view, "X has won!");}
                } else if(winningPositions(AI, gameState)){
                    if(playingComputer){
                        openAlertBox(view, "Computer won!");
                    }
                    else {openAlertBox(view, "O has won!");}
                }

        if (counter == 9 && flag == 0) {
            openAlertBox(view, "It's a Tie");
        }
    }

    public void resetGame(View view){
        cancelled = false;
        playersTurn = 1;
        counter = 0;
        for(int i = 0; i < gameState.length; i++){
            gameState[i] = 0;
        }
        //setting all the images to not display anything
        ((ImageView) findViewById(R.id.imageView0)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView8)).setImageResource(0);

        View resetButton = findViewById(R.id.resetButton);
        resetButton.setVisibility(View.GONE);

        //X is always first to play
        TextView status = findViewById(R.id.turnDisplay);
        status.setText("X's Turn - Tap to play");
        openStartAlert();
    }

    //when a player has won, send the view and the string of the winning player
    private void openAlertBox(View view, String winner) {
        AlertDialog.Builder dialogBox = new AlertDialog.Builder(this);
        dialogBox.setMessage("Play again?");
        dialogBox.setTitle(winner);
        dialogBox.setCancelable(false);
        //if they click yes, reset the game
        dialogBox.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetGame(view);
                    }
                });
        //if they click no,
        dialogBox.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        //makes it so the player cannot push place any markers
                        for(int j = 0; j < gameState.length; j++){
                            if(gameState[j] == 0){
                                gameState[j] = 3;
                            }
                        }
                        cancelled = true;
                        TextView status = findViewById(R.id.turnDisplay);
                        status.setText("Game Over");
                        //if player decides they want to restart the game, reset button appears
                        View resetButton = findViewById(R.id.resetButton);
                        resetButton.setVisibility(View.VISIBLE);
                    }
                });
        AlertDialog alert = dialogBox.create();
        alert.show();
    }

    private void openStartAlert() {
        AlertDialog.Builder dialogBox = new AlertDialog.Builder(this);
        dialogBox.setMessage("Play against a ...");
        dialogBox.setTitle("Tic Tac Toe");
        dialogBox.setCancelable(false);
        //if they click yes, start AI
        dialogBox.setPositiveButton(
                "Computer?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        playingComputer = true;
                        turnDisplay = findViewById(R.id.turnDisplay);
                        turnDisplay.setText("Your Turn - Tap to Play");
                    }
                });
        //if they click no,
        dialogBox.setNegativeButton(
                "Friend?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        playingComputer = false;
                        turnDisplay = findViewById(R.id.turnDisplay);
                        turnDisplay.setText("X's Turn - Tap to Play");
                    }
                });
        AlertDialog alert = dialogBox.create();
        alert.show();
    }

    private int computerMove(int[] board, int player){
        int[] newBoard = makeCopyOfBoard(board);
        int[] availSpots = emptyIndexies(newBoard);
        if(winningPositions(HUMAN, newBoard)){
            return -10;
        }
        else if(winningPositions(AI, newBoard)){
            return 10;
        }
        else if(availSpots.length == 0){ return 0;}

        List<Move> moves = new ArrayList<Move>();

        for(int i = 0; i < availSpots.length; i++){
            Move move = new Move();
            move.index = availSpots[i];
            newBoard[availSpots[i]] = player;

            if(player == AI){
               move.score = computerMove(newBoard, HUMAN);
            } else {
                move.score = computerMove(newBoard, AI);
            }

            availSpots[i] = move.index;
            moves.add(move);
        }

        int bestMove = -1;
        if(player == AI){
            int bestScore = Integer.MIN_VALUE;
            for(int i = 0; i < moves.size(); i++){
                if(moves.get(i).score > bestScore){
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        } else {
            int bestScore = Integer.MAX_VALUE;
            for(int i = 0; i < moves.size(); i++){
                if(moves.get(i).score < bestScore){
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        }
        return moves.get(bestMove).index;
    }

    private int[] emptyIndexies(int[] board){
        List<Integer> empty = new ArrayList<Integer>();
        for(int i = 0; i < 9; i++){
            if(board[i] == 0){
                empty.add(i);
            }
        }
        int[] array = new int[empty.size()];
        for(int i = 0; i < empty.size(); i++) array[i] = empty.get(i);
        return array;
    }

    private int[] makeCopyOfBoard(int[] board){
        List<Integer> newBoard = new ArrayList<Integer>();
        for(int i = 0; i < 9; i++){
            newBoard.add(board[i]);
        }
        int[] array = new int[newBoard.size()];
        for(int i = 0; i < newBoard.size(); i++) array[i] = newBoard.get(i);
        return array;
    }

    private boolean winningPositions(int player, int[] board){
        flag = 0;
        //checks for winners, then goes opens alert box if there is a winner
        for (int[] winPosition : winPositions) {
            if (board[winPosition[1]] == board[winPosition[2]] && board[winPosition[2]] == board[winPosition[0]] &&
                    board[winPosition[1]] != 0) {
                flag = 1;
                if (board[winPosition[1]] == player) { return true; }
                else { return false;}
            }
        }
        return false;
    }
}

class Move {
    int[] newBoard;
    int score;
    int index;
}