package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.backends.lwjgl.LwjglBackend;
import sk.tuke.kpi.oop.game.scenarios.MyOwnScenario;

public class Main {

    public static void main(String[] args) {
        WindowSetup windowSetup = new WindowSetup("Project Ellen", 800, 600);
        Game game = new GameApplication(windowSetup, new LwjglBackend());
        //Scene scene = new World("world", "maps/escape-room.tmx", new EscapeRoom.Factory());
        Scene scene = new World("world", "maps/map.tmx", new MyOwnScenario.Factory());

        //FirstSteps firstSteps = new FirstSteps();
        //scene.addListener(firstSteps);

        //MissionImpossible missionImpossible = new MissionImpossible();
        //scene.addListener(missionImpossible);

        //EscapeRoom escapeRoom = new EscapeRoom();
        //scene.addListener(escapeRoom);

        MyOwnScenario myOwnScenario = new MyOwnScenario();
        scene.addListener(myOwnScenario);

        game.addScene(scene);
        game.getInput().onKeyPressed(Input.Key.ESCAPE, game::stop);

        game.start();
    }
}
