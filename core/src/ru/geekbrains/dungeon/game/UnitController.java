package ru.geekbrains.dungeon.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class UnitController {
    private GameController gc;
    private MonsterController monsterController;
    private Hero hero;
    private Unit currentUnit;
    private int index;
    private List<Unit> allUnits;

    public MonsterController getMonsterController() {
        return monsterController;
    }

    public Hero getHero() {
        return hero;
    }

//    public Unit getCurrentUnit() {
//        return currentUnit;
//    }

    public boolean isItMyTurn(Unit unit) {
        return currentUnit == unit;
    }

    public boolean isCellFree(int cellX, int cellY) {
        for (Unit u : allUnits) {
            if (u.getCellX() == cellX && u.getCellY() == cellY) {
                return false;
            }
        }
        return true;
    }

    public UnitController(GameController gc) {
        this.gc = gc;
        this.hero = new Hero(gc);
        this.monsterController = new MonsterController(gc);
    }

    public void init() {
        this.monsterController.activate(5, 5);
        this.monsterController.activate(9, 5);
        this.index = -1;
        this.allUnits = new ArrayList<>();
        this.allUnits.add(hero);
        this.allUnits.addAll(monsterController.getActiveList());
        this.nextTurn();
    }

    @Override
    public String toString() {
        return "UnitController{" +
                "allUnits=" + allUnits +
                '}';
    }

    public void addMonster() {

        this.monsterController.activate(MathUtils.random(1, 15), MathUtils.random(1,15));
        this.allUnits.add(monsterController.newObject());

    }

    public void nextTurn() {
        index++;
//        if (gc.getUnitController().getCurrentUnit().hp<100) {gc.getUnitController().getHero().addHP();}
        if (index >= allUnits.size()) {
            index = 0;
            gc.getUnitController().getHero().addRound();
            if (gc.getUnitController().getHero().getRound()%3==0){
                addMonster();
            }
        }
        currentUnit = allUnits.get(index);
        currentUnit.startTurn();
    }

    public void render(SpriteBatch batch, BitmapFont font18) {
        hero.render(batch, font18);
        monsterController.render(batch, font18);
    }

    public void update(float dt) {
        hero.update(dt);
        monsterController.update(dt);

        if (!currentUnit.isActive() || currentUnit.getTurns() == 0) {
            nextTurn();
        }

    }

    public void removeUnitAfterDeath(Unit unit) {
        int unitIndex = allUnits.indexOf(unit);
        allUnits.remove(unit);
        if (unitIndex <= index) {
            index--;
        }
        gc.getUnitController().getHero().addMoney();
    }
}
