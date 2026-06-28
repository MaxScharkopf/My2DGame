package combat;

import entity.LivingEntity;
import entity.Player;
import main.GamePanel;

public class CombatSystem {

    GamePanel gp;

    public CombatSystem(GamePanel gp) {
        this.gp = gp;
    }

    public int calcDamage(int attack, int defense) {
        return Math.max(0, attack - defense);
    }

    public void hitMonster(LivingEntity target, int attack, Player attacker) {
        if (target.invincible) return;

        gp.playSE(5);
        int damage = calcDamage(attack, target.defense);
        target.life -= damage;
        gp.ui.addMessage(damage + " damage!");
        target.invincible = true;
        target.damageReaction();

        if (target.life <= 0) {
            target.dying = true;
            gp.ui.addMessage("Killed the " + target.name + "!");
            gp.ui.addMessage("Exp " + target.exp);
            attacker.exp += target.exp;
            attacker.checkLevelUp();
        }
    }

    public void hitPlayer(int attack) {
        Player player = gp.player;
        if (player.invincible) return;

        gp.playSE(6);
        int damage = calcDamage(attack, player.defense);
        player.life -= damage;
        player.invincible = true;
    }

    public void hitCritter(LivingEntity target) {
        if (target.invincible) return;

        gp.playSE(5);
        target.life -= 1;
        target.invincible = true;
        target.damageReaction();

        if (target.life <= 0) {
            target.dying = true;
        }
    }
}
