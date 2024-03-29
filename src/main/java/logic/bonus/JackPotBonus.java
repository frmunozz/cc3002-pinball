package logic.bonus;

import controller.Game;
import logic.gameelements.GameElementType;
import logic.gameelements.target.SpotTarget;
import logic.gameelements.target.Target;

/**
 * Class that define jackpot bonus.
 * use Singleton Pattern to guarantee only one initialize per game.
 * Use Observable Pattern to receive the message from spotTarget to trigger,
 * Also use Observable Pattern mixed with Visit Pattern to solve the problem of
 * bonus that give points or balls.
 *
 * @see Bonus
 * @see Game
 * @see logic.gameelements.target.SpotTarget
 * @author Fancisco Muñoz Ponce. on date: 17-05-18
 */
public class JackPotBonus extends AbstractBonus {

    /**
     * the instance of JackpotBonus, this is part of Singleton Pattern
     */
    private static JackPotBonus instance = null;

    /**
     * constructor declared as private for Singleton Pattern
     */
    private JackPotBonus() {
        super(100000, false);
    }

    /**
     * the generator of instance for Singleton Pattern, this will generate
     * a new instance of JackPotBonus only if there is no previous instance.
     *
     * @return an instance of JackPotBonus
     */
    public static JackPotBonus getInstance(){
        if (instance == null){
            instance = new JackPotBonus();
        }
        return instance;
    }

    public void visitASpotTarget(SpotTarget spotTarget){
        if (spotTarget.bonusTriggered()){
            trigger();
        }
    }

    @Override
    public void hitTarget(Target target) {
        if (target.getType().equals(GameElementType.SPOT_TARGET)){
            visitASpotTarget((SpotTarget) target);
        }
    }
}
