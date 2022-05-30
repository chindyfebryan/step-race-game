package com.sisfo.sprites;

import com.sisfo.GPanel;
import com.sisfo.GameEvent;
import com.sisfo.tiles.Objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player extends Entity {

    public static int moves = 0;
    public static int mapShift = 0;
    public static int forward;

    public static int player1, player2;

    public final int WIN_SCORE = 50;

    private final GameEvent event;
    private final GPanel window;

    public static Boolean idlingP1 = true;
    public static Boolean idlingP2 = true;
    public static Boolean winner = false;

    public Boolean computerPlay = true;
    public Boolean diceRolling = false;

    public int move, maxMove;

    public Player(GPanel window, GameEvent event) { // Kunci dari semua ini

        this.window = window;
        this.event = event;
        this.setPlayerStartingPos(window);
        this.generateItem();
        this.getPlayer(player1, 1);
        this.getPlayer(player2, 2);
        this.diceRoll();
        this.stopPlayer();

    }

    // .. Akhirnya jadi juga frame updaternya wkwkwk

    public void updateP1() {

        if (!idlingP1) {

            if (PLAYER1_X < maxMove) {
                playerID = 1;
                PLAYER1_X++;

                if (spriteCounterP1 > 5 && !idlingP1) { // Delay per animasi Karakter
                    spriteNumP1 += (spriteNumP1 < walkP1.length) ? 1 : 0;
                    spriteNumP1 = (spriteNumP1 == walkP1.length) ? 1 : spriteNumP1;
                    spriteCounterP1 = 0;
                }
                spriteCounterP1++;

            } else {
                spriteNumP1--;
                stopPlayer();
            }
        }

        else {
            if (spriteCounterP1 > 10) { // Delay per animasi Karakter
                spriteNumP1 += (spriteNumP1 < idleP1.length) ? 1 : 0;
                spriteNumP1 = (spriteNumP1 == idleP1.length) ? 1 : spriteNumP1;
                spriteCounterP1 = 0;
            }
            spriteCounterP1++;
        }

        if (mapShift < 0)
            mapShift = 0;
        // System.out.println("\tPlayer1 ::> " + spriteNumP1 + " " + idlingP1);

    }

    public void updateP2() {

        if (!idlingP2) {

            if (PLAYER2_X < maxMove) {
                playerID = 2;
                PLAYER2_X++;

                if (spriteCounterP2 > 5 && !idlingP2) { // Delay per animasi Karakter
                    spriteNumP2 += (spriteNumP2 < walkP2.length) ? 1 : 0;
                    spriteNumP2 = (spriteNumP2 == walkP2.length) ? 1 : spriteNumP2;
                    spriteCounterP2 = 0;
                }
                spriteCounterP2++;

            } else {
                spriteNumP2--;
                stopPlayer();
            }
        }

        else {
            if (spriteCounterP2 > 10) { // Delay per animasi Karakter
                spriteNumP2 += (spriteNumP2 < idleP2.length) ? 1 : 0;
                spriteNumP2 = (spriteNumP2 == idleP2.length) ? 1 : spriteNumP2;
                spriteCounterP2 = 0;
            }
            spriteCounterP2++;
        }

        // System.out.print("\nPlayer2 ::> " + spriteNumP2 + " " + idlingP2);
    }

    public void drawP1(GraphicsContext render) {

        Image player1 = null;

        if (idlingP1 && spriteNumP1 < idleP1.length) {
            spriteNumP1 = (spriteNumP1 < 1) ? 1 : spriteNumP1;
            player1 = idleP1[spriteNumP1 - 1];

        } else if (!idlingP1 && spriteNumP1 < walkP1.length) {
            spriteNumP1 = (spriteNumP1 < 1) ? 1 : spriteNumP1;
            player1 = walkP1[spriteNumP1 - 1];

        }
        render.drawImage(player1, PLAYER1_X, PLAYER1_Y, window.spriteSize, window.spriteSize);
    }

    public void drawP2(GraphicsContext render) {

        Image player2 = null;

        if (idlingP2 && spriteNumP2 < idleP2.length) {
            spriteNumP2 = (spriteNumP2 < 1) ? 1 : spriteNumP2;
            player2 = idleP2[spriteNumP2 - 1];

        } else if (!idlingP2 && spriteNumP2 < walkP2.length) {
            spriteNumP2 = (spriteNumP2 < 1) ? 1 : spriteNumP2;
            player2 = walkP2[spriteNumP2 - 1];

        }

        render.drawImage(player2, PLAYER2_X, PLAYER2_Y, window.spriteSize, window.spriteSize);
    }

    public void stopPlayer() {

        diceRolling = false;
        if (moves > 1) {
            Player.forward = event.moves;
            PLAYER1_SCORE += (playerID == 1) ? event.moves : 0;
            PLAYER2_SCORE += (playerID == 2) ? event.moves : 0;

            if (event.moves == 6) {
                playerID = (playerID == 1) ? 1 : 2;
                gotPower1 = true;
            } else {
                playerID = (playerID == 1) ? 2 : 1;
                computerPlay = (computerPlay) ? false : true;
            }

        } else {
            computerPlay = false;
            maxMove = 0;
        }

        gotPower1 = (playerID == 1) ? true : false;
        gotPower2 = (playerID == 2) ? true : false;

        idlingP1 = true;
        idlingP2 = true;
        playerID = 0;

        if (computerPlay && !winner) {
            playerID = 2;
            idlingP2 = false;
            diceRoll();
        }
    }

    private int count = 0;

    public void diceRoll() { // Pencet spasi
        count++;
        if (count > 1) {
            P1_IS_INVINCIBLE = false;
            count =0;
        }
        diceRolling = true;
        event.diceRoll();
        move = event.moves;

        if (playerID == 1) {
            gotPower1 = true;
            idlingP1 = true;
            maxMove = PLAYER1_X + (this.event.moves * window.spriteSize);

        } else if (playerID == 2) {
            gotPower2 = true;
            idlingP2 = true;
            maxMove = PLAYER2_X + (this.event.moves * window.spriteSize);
        }

    }

    public void checkWinner() {

        if (PLAYER1_SCORE >= WIN_SCORE || PLAYER2_SCORE >= WIN_SCORE) {
            winner = true;
            playerID = 0;
            stopPlayer();
        }
        if (idlingP1 && idlingP2 && powerSlot2 != null && !gotPower2 ) {
            computerUseSkill((int) (Math.random() * 3));
            gotPower2=false;
        }
    }

    public void useQ() {
        if (idlingP1 && idlingP2 && powerSlot1[0] != null) {
            useSkill(0);
        }
    }

    public void useW() {
        if (idlingP1 && idlingP2 && powerSlot1[1] != null) {
            useSkill(1);
        }
    }

    public void useE() {
        if (idlingP1 && idlingP2 && powerSlot1[2] != null) {
            useSkill(2);
        }
    }

    public void useSkill(int i) {
        
        if (powerSlot1[i] == "BLINK") {


            PLAYER1_X += 3 * 48;
            PLAYER1_SCORE += 3;
            Objects.message = "PLAYER 1 USED BLINK!";

        } else if (powerSlot1[i] == "HOOK") {
 
            PLAYER2_X = (PLAYER2_X > PLAYER1_X) ? PLAYER1_X : PLAYER2_X;
            PLAYER2_SCORE = (PLAYER2_SCORE > PLAYER1_X) ? PLAYER1_SCORE : PLAYER2_SCORE;
            Objects.message = "PLAYER 1 USED HOOK ON PLAYER 2!";

        } else if (powerSlot1[i] == "INVINCIBLE") {
   
            P1_IS_INVINCIBLE = true;
            Objects.message = "PLAYER 1 USED INVINCIBLE!";
        }
        powerSlot1[i] = null;
    }

    public void computerUseSkill(int i) {
        
        if (powerSlot2[i] == "BLINK") {
            PLAYER2_X += 3 * 48;
            PLAYER2_SCORE += 3;
            Objects.message = "PLAYER 2 USED BLINK!";

        } else if (powerSlot2[i] == "HOOK") {
     
            PLAYER1_X = (PLAYER1_X > PLAYER2_X) ? PLAYER2_X : PLAYER1_X;
            PLAYER1_SCORE = (PLAYER1_SCORE > PLAYER2_X) ? PLAYER2_SCORE : PLAYER1_SCORE;
            Objects.message = "PLAYER 2 USED HOOK ON PLAYER 1!";

        } else if (powerSlot2[i] == "INVINCIBLE") {

            P1_IS_INVINCIBLE = true;
            Objects.message = "PLAYER 2 USED INVINCIBLE!";
        }
        powerSlot2[i] = null;
    }
}
