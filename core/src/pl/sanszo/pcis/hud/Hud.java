package pl.sanszo.pcis.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import pl.sanszo.pcis.GameStage;

public class Hud extends Stage {
    private Array<HudActor> visibleAlways = new Array<>();
    private Array<HudActor> visibleInTryAgain = new Array<>();
    private Array<HudActor> visibleOnStart = new Array<>();


    public void addVisableAlwaysActor(HudActor actor) {
        addActor(actor);
        visibleAlways.add(actor);
    }

    public void addVisableInTryAgainModeActor(HudActor actor) {
        addActor(actor);
        visibleInTryAgain.add(actor);
    }

    public void addVisableOnStartActor(HudActor actor) {
        addActor(actor);
        visibleOnStart.add(actor);
    }

    public void addVisableOnStartAndTryAgain(HudActor actor) {
        addActor(actor);
        visibleOnStart.add(actor);
        visibleInTryAgain.add(actor);
    }

    public void update(GameStage.Mode mode) {
        switch (mode) {
            case START:
                showAll(visibleAlways);
                showAll(visibleOnStart);
                break;
            case PLAY:
                hideAll(visibleOnStart);
                hideAll(visibleInTryAgain);
                break;
            case TRY_AGAIN:
                showAll(visibleInTryAgain);
                break;
        }
    }


    private static void showAll(Array<HudActor> list) {
        for(HudActor actor : list)
            actor.show();
    }

    private static void hideAll(Array<HudActor> list) {
        for(HudActor actor : list)
            actor.hide();
    }

    public Hud (Viewport viewport, Batch batch) {
        super(viewport, batch);
    }
}
