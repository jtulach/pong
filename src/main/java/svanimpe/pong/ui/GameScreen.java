/*
 * Copyright (c) 2014, Steven Van Impe
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *     following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package svanimpe.pong.ui;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import svanimpe.pong.Game;
import svanimpe.pong.objects.Paddle;

import static svanimpe.pong.Constants.*;

public class GameScreen extends Pane
{
    private final Rectangle ball = new Rectangle(BALL_SIZE, BALL_SIZE);
    private final Rectangle player = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT);
    private final Rectangle opponent = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT);
    
    private final Text playerScore = new Text("0");
    private final Text opponentScore = new Text("0");
    
    public GameScreen(final Game game)
    {
        ball.translateXProperty().bind(game.getBall().xProperty());
        ball.translateYProperty().bind(game.getBall().yProperty());
        ball.getStyleClass().add("ball");
        
        player.translateXProperty().bind(game.getPlayer().xProperty());
        player.translateYProperty().bind(game.getPlayer().yProperty());
        player.getStyleClass().add("paddle");
        
        opponent.translateXProperty().bind(game.getOpponent().xProperty());
        opponent.translateYProperty().bind(game.getOpponent().yProperty());
        opponent.getStyleClass().add("paddle");
        
        playerScore.textProperty().bind(game.getPlayer().scoreProperty().asString());
        playerScore.boundsInLocalProperty().addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                /*
                 * When using CSS, the width and height (with CSS applied) aren't available right
                 * away. Therefore, we listen for changes and update the position once the width and
                 * height are available.
                 */
                playerScore.setTranslateX(WIDTH / 2 - SCORE_SPACING / 2 - playerScore.getBoundsInLocal().getWidth());
            }
        });
        playerScore.setTranslateY(TEXT_MARGIN_TOP_BOTTOM);
        playerScore.getStyleClass().add("score");
        
        opponentScore.textProperty().bind(game.getOpponent().scoreProperty().asString());
        opponentScore.setTranslateX(WIDTH / 2 + SCORE_SPACING / 2);
        opponentScore.setTranslateY(TEXT_MARGIN_TOP_BOTTOM);
        opponentScore.getStyleClass().add("score");
        
        setPrefSize(WIDTH, HEIGHT);
        getChildren().addAll(ball, player, opponent, playerScore, opponentScore);
        getStyleClass().add("screen");
        
        setOnTouchPressed(new EventHandler<TouchEvent>()
        {
            @Override
            public void handle(TouchEvent event)
            {
                /*
                 * Ignore multi-touch events.
                 */
                if (event.getTouchCount() != 1) {
                    return;
                }
                
                double touchY = event.getTouchPoint().getSceneY();
                double playerY = player.localToScene(0, PADDLE_HEIGHT / 2).getY();
                if (touchY > playerY) {
                    game.getPlayer().setMovement(Paddle.Movement.DOWN);
                } else {
                    game.getPlayer().setMovement(Paddle.Movement.UP);
                }
            }
        });
        setOnTouchReleased(new EventHandler<TouchEvent>()
        {
            @Override
            public void handle(TouchEvent event)
            {
                game.getPlayer().setMovement(Paddle.Movement.NONE);
            }
        });
    }
}
