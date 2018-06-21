package pl.sanszo.pcis;

import com.badlogic.gdx.graphics.OrthographicCamera;



public class FollowingCamera extends OrthographicCamera {
    private Polandball player;

    @Override
    public void update () {
        if(player != null) {
            float newY;
            if (player.getY() > Game.HEIGHT * 3 / 4f) {
                newY = player.getY() - 1 / 4f * Game.HEIGHT;

            } else {
                newY = Game.HEIGHT * 1 / 2f;
            }
            //Gdx.app.log("CAM", "player: " + player.getY() + " cam: " + position.y);
            if (newY > position.y) {
                position.y = newY;
            }
        }
        super.update();
    }

    public void restart() {
        position.y = Game.HEIGHT*1/2f;
    }

    public void setPlayer(Polandball player) {
        this.player = player;
    }

}
