package scripts.cluehuntercollector.tasks.items;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import scripts.api.dax_api.api_lib.DaxWalker;
import scripts.api.interaction.Interactions;

import static scripts.AutoIceGloves.Data.Constants.behind_throne;
import static scripts.cluehuntercollector.data.Constants.*;
import static scripts.api.bank.bank.isAtBank;

public class Garb {

    private boolean continueRunning = true;
    private static int TIMEOUT = General.random(5000,10000);
    private static long START_TIME = System.currentTimeMillis();

    // this is an instanced class you can use throughout the whole script.
    private ABCUtil abcInstance = new ABCUtil();

    private int run_at = abcInstance.generateRunActivation();

    private void runCheck(){
        if (!Game.isRunOn() && Game.getRunEnergy() >= run_at) {
            if (Options.setRunOn(true)){
                Timing.waitCondition(() -> Options.isRunEnabled(), General.random(600, 1100));
                run_at = abcInstance.generateRunActivation();
            }
        }
    }


    public enum State{
        WALK_TO_AREA,DIG
    }



    private State SCRIPT_STATE = getState();

    private boolean hasSupplies(){
        RSItem[] spade = Inventory.find("Spade");
        return spade.length > 0;
    }

    private boolean inArea(){
        return AREA_GARB.equals(Player.getPosition());
    }


    private State getState() {

            if (inArea()) {
                return State.DIG;
            } else {
                return State.WALK_TO_AREA;
            }



    }

    public void run() {
        while (continueRunning) {
            SCRIPT_STATE = getState();
            performTimedActions();
            runCheck();
            switch (SCRIPT_STATE) {

                case DIG:

                            if (hasItem("Clue hunter garb")){
                                continueRunning = false;
                            } else {
                                if (System.currentTimeMillis() - START_TIME >= TIMEOUT) {
                                    General.println("This is taking a little too long... ");
                                    if (DaxWalker.walkTo(AREA_GARB)) {
                                        Timing.waitCondition(() -> !Player.isMoving(), General.random(600, 1100));
                                        START_TIME = System.currentTimeMillis();
                                    }
                                } else {

                                        RSItem[] spade = Inventory.find("Spade");
                                        if (spade.length > 0 && spade[0] != null) {
                                            if (spade[0].click("Dig")) {
                                                Timing.waitCondition(() -> hasItem("Clue hunter garb"), TIMEOUT);
                                            }
                                        }

                                }

                            }

                    break;

                case WALK_TO_AREA:
                    if (AREA_GARB.isOnScreen() && AREA_GARB.isClickable()){
                        if (Walking.clickTileMS(AREA_GARB,"Walk here")){
                            Timing.waitCondition(() -> !Player.isMoving(), General.random(2500, 4100));
                        }
                    } else {
                        if (!AREA_GARB.equals(Player.getPosition())) {
                            if (DaxWalker.walkTo(AREA_GARB)) {
                                Timing.waitCondition(() -> !Player.isMoving(), General.random(600, 1100));
                            }
                        }
                    }


                    break;


            }

        }

    }

    public void performTimedActions() {


        if (abcInstance.shouldCheckXP()) {
            abcInstance.checkXP();
        }

        if (abcInstance.shouldExamineEntity())
            abcInstance.examineEntity();

        if (abcInstance.shouldMoveMouse())
            abcInstance.moveMouse();

        if (abcInstance.shouldPickupMouse())
            abcInstance.pickupMouse();

        if (abcInstance.shouldRightClick())
            abcInstance.rightClick();

        if (abcInstance.shouldRotateCamera())
            abcInstance.rotateCamera();


        if (abcInstance.shouldLeaveGame())

            abcInstance.leaveGame();



    }

    private boolean hasItemFilter (String...ItemName){
        RSItem[] items = Inventory.find(Filters.Items.nameContains(ItemName));
        return items.length > 0 && items[0]!=null;
    }

    private boolean bankHasItemFilter (String...ItemName){
        RSItem[] items = Banking.find(Filters.Items.nameContains(ItemName));
        return items.length > 0 && items[0]!=null;
    }


    private boolean hasItem (String...ItemName){
        RSItem[] items = Inventory.find(ItemName);
        return items.length > 0 && items[0]!=null;
    }

    private boolean bankHasItem (String...ItemName){
        RSItem[] items = Banking.find(ItemName);
        return items.length > 0 && items[0]!=null;
    }


    private int openInventorySpots(){
        return 28 - Inventory.getAll().length;
    }


}
