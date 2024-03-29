package logic.bonus;

import controller.Game;
import logic.gameelements.bumper.Bumper;
import logic.gameelements.bumper.PopBumper;
import logic.gameelements.target.SpotTarget;
import logic.gameelements.target.Target;
import logic.table.GameTable;
import logic.table.Table;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Fancisco Muñoz Ponce. on date: 27-05-18
 */
public class JackPotBonusTest {
    private static Target target;
    private static JackPotBonus jackPotBonus;
    private static Game game;
    private static int score, balls;

    /**
     * since we test a class created with singleton Pattern,
     * is better use a setUp of before the class.
     */
    @BeforeClass
    public static void setUp() {
        score = 33;
        balls = 9;
        jackPotBonus = JackPotBonus.getInstance();
        game = new Game(balls, score);
        target = new SpotTarget();

        // observers
        jackPotBonus.setObservers(game);
        target.setObservers(game, jackPotBonus);
    }

    /**
     * test that this method actually return the instance of JackPotBonus
     * satisfying the Singleton Pattern
     */
    @Test
    public void getInstance() {
        assertEquals(jackPotBonus, JackPotBonus.getInstance());
        // if we repeat, still return the same
        assertEquals(jackPotBonus, JackPotBonus.getInstance());
    }

    /**
     * action of visit a SpotTarget, this is used in the
     * visitTarget() method
     */
    @Test
    public void visitASpotTarget() {
        // tested in visitTarget()
    }

    /**
     * test that accept a visit from Bumper doesn't do anything
     */
    @Test
    public void acceptFromBumper() {
        Bumper bumper = new PopBumper();
        assertFalse(bumper.isUpgraded());
        assertEquals(3, bumper.remainingHitsToUpgrade());
        assertEquals(100, bumper.getScore());
        assertFalse(jackPotBonus.isBonusOfBalls());
        int count = jackPotBonus.timesTriggered();
        jackPotBonus.acceptObservationFromBumper(bumper);
        assertFalse(bumper.isUpgraded());
        assertEquals(3, bumper.remainingHitsToUpgrade());
        assertEquals(100, bumper.getScore());
        assertEquals(count, jackPotBonus.timesTriggered());
        assertFalse(jackPotBonus.isBonusOfBalls());
    }

    /**
     * visit a bumper doesn't do anything,
     */
    @Test
    public void visitBumper() {
        Bumper bumper = new PopBumper();
        assertFalse(bumper.isUpgraded());
        assertEquals(3, bumper.remainingHitsToUpgrade());
        assertEquals(100, bumper.getScore());
        assertFalse(jackPotBonus.isBonusOfBalls());
        int count = jackPotBonus.timesTriggered();
        jackPotBonus.hitBumper(bumper);
        assertFalse(bumper.isUpgraded());
        assertEquals(3, bumper.remainingHitsToUpgrade());
        assertEquals(100, bumper.getScore());
        assertEquals(count, jackPotBonus.timesTriggered());
        assertFalse(jackPotBonus.isBonusOfBalls());
    }

    /**
     * visit a target trigger this bonus only if using hit()
     */
    @Test
    public void visitTarget() {
        // check for call hitTarget without hit
        assertTrue(target.isActive());
        assertFalse(target.bonusTriggered());
        int count = jackPotBonus.timesTriggered();
        jackPotBonus.hitTarget(target);
        assertTrue(target.isActive());
        assertFalse(target.bonusTriggered());
        assertEquals(count, jackPotBonus.timesTriggered());

        // then with hit
        target.hit();
        assertEquals(count+1, jackPotBonus.timesTriggered());
    }

    /**
     * visit a table do nothing
     */
    @Test
    public void visitTable() {
        Table table = GameTable.getFullTable("table", 5, 0.2,
                5, 8);
        table.setGameElementsObservers(game);
        assertTrue(table.isPlayableTable());
        assertEquals(0, table.getCurrentlyDroppedDropTargets());
        int count = jackPotBonus.timesTriggered();
        jackPotBonus.changedStateOfTable(table);
        assertTrue(table.isPlayableTable());
        assertEquals(0, table.getCurrentlyDroppedDropTargets());
        assertEquals(count, jackPotBonus.timesTriggered());

    }

    /**
     * test that reset the counter of times triggers actually set the number of
     * triggers to 0, this is common to all bonuses, to see the test, go to
     * {@link DropTargetBonusTest}
     */
    @Test
    public void resetCounterTriggers() {
        // tested in DropTargetBonusTest
    }

    /**
     * test that set observers actually set the correct number of observers.
     * Common behavior for all bonuses, to see test go to {@link DropTargetBonusTest}
     */
    @Test
    public void setObservers() {
        // tested in DropTargetBonusTest
    }

    /**
     * plenty tested in interactions
     */
    @Test
    public void timesTriggered() {
        // go to test of bumpers, targets, table or game
    }

    /**
     * test that trigger() augment the timesTriggered by 1
     * and augment the score in game.
     */
    @Test
    public void trigger() {
        score = game.getScore();
        int count = jackPotBonus.timesTriggered();
        jackPotBonus.trigger();
        int jackScore = jackPotBonus.getBonusValue();
        assertEquals(count +1, jackPotBonus.timesTriggered());
        assertEquals(score + jackScore, game.getScore());
    }

    /**
     * plenty testes in interactions
     */
    @Test
    public void getBonusValue() {
        // go to test of bumpers, targets, table or game
    }

    /**
     * test that this method return False for JackPotBonus
     */
    @Test
    public void isBonusOfBalls() {
        assertFalse(jackPotBonus.isBonusOfBalls());
    }

    /**
     * interaction of the Observer,
     * plenty tested with interactions
     */
    @Test
    public void update() {
        // go to test of bumpers, targets, table or game
    }

    /**
     * test that accept a visit from Game will augment
     * the game score. This is usually accessed trough a
     * trigger().
     */
    @Test
    public void acceptFromGame() {
        score = game.getScore();
        jackPotBonus.acceptObservationFromGame(game);
        assertEquals(score + jackPotBonus.getBonusValue(), game.getScore());
    }

    /**
     * test that accept a visit from another bonus
     * doesn't do anything.
     */
    @Test
    public void acceptFromBonus() {
        Bonus bonus = ExtraBallBonus.getInstance();
        int countExt = bonus.timesTriggered();
        int countJack = jackPotBonus.timesTriggered();
        jackPotBonus.acceptObservatiobFromBonus(bonus);
        assertEquals(countExt, bonus.timesTriggered());
        assertEquals(countJack, jackPotBonus.timesTriggered());
    }

    /**
     * test that accept a visit from target doesn't do anything,
     * common behavior for al bonuses, to see test go to
     * {@link DropTargetBonusTest}
     */
    @Test
    public void acceptFromTarget() {
        // tested in DropTargetBonusTest
    }

    /**
     * test that accept a visit from Table,
     * desn't do anything
     */
    @Test
    public void acceptFromTable() {
        Table table = GameTable.getTableWithoutTargets("table", 6, 0.3);
        table.setGameElementsObservers(game);
        assertTrue(table.isPlayableTable());
        assertEquals(0, table.getCurrentlyDroppedDropTargets());
        int count = jackPotBonus.timesTriggered();
        jackPotBonus.acceptObservationFromTable(table);
        assertTrue(table.isPlayableTable());
        assertEquals(0, table.getCurrentlyDroppedDropTargets());
        assertEquals(count, jackPotBonus.timesTriggered());
    }

    /**
     * test that visit a Bonus doesn't do anything.
     */
    @Test
    public void visitBonus() {
        Bonus bonus = DropTargetBonus.getInstance();
        assertFalse(bonus.isBonusOfBalls());
        int countExt = bonus.timesTriggered();
        int countDrop = jackPotBonus.timesTriggered();
        jackPotBonus.triggeredBonus(bonus);
        assertFalse(bonus.isBonusOfBalls());
        assertEquals(countDrop, jackPotBonus.timesTriggered());
        assertEquals(countExt, bonus.timesTriggered());
    }
}