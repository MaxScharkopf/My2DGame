package config;

import entity.Entity;
import entity.ItemEntity;
import main.GamePanel;
import main.GameState;
import objects.*;

import java.io.*;
import java.util.Properties;

public class SaveData {

    GamePanel gp;
    public int currentSlot = -1;
    private static final int MAX_SLOTS = 4;

    public static class SlotSummary {
        public int level;
        public int coin;
        public String mapName;
    }

    public SaveData(GamePanel gp) {
        this.gp = gp;
    }

    private String filePath(int slot) {
        return "save_" + slot + ".properties";
    }

    public boolean hasSaveFile(int slot) {
        return slot >= 0 && slot < MAX_SLOTS && new File(filePath(slot)).exists();
    }

    public SlotSummary getSlotSummary(int slot) {
        if (!hasSaveFile(slot)) return null;
        Properties props = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath(slot)))) {
            props.load(br);
        } catch (IOException e) {
            return null;
        }
        SlotSummary s = new SlotSummary();
        s.level   = Integer.parseInt(props.getProperty("player.level", "1"));
        s.coin    = Integer.parseInt(props.getProperty("player.coin",  "0"));
        s.mapName = props.getProperty("mapName", "Overworld");
        return s;
    }

    public void clearSlot(int slot) {
        File f = new File(filePath(slot));
        if (f.exists()) f.delete();
    }

    public void save() {
        if (currentSlot < 0 || currentSlot >= MAX_SLOTS) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath(currentSlot)))) {
            var p = gp.player;
            String mapName = gp.currentMap == 0 ? "Overworld" : "Interior";

            bw.write("player.class=" + p.playerClass);                         bw.newLine();
            bw.write("mapName=" + mapName);                                    bw.newLine();
            bw.write("currentMap=" + gp.currentMap);                           bw.newLine();
            bw.write("player.worldX=" + p.worldX);                            bw.newLine();
            bw.write("player.worldY=" + p.worldY);                            bw.newLine();
            bw.write("player.direction=" + p.direction);                       bw.newLine();
            bw.write("player.life=" + p.life);                                bw.newLine();
            bw.write("player.maxLife=" + p.maxLife);                          bw.newLine();
            bw.write("player.mana=" + p.mana);                                bw.newLine();
            bw.write("player.maxMana=" + p.maxMana);                          bw.newLine();
            bw.write("player.coin=" + p.coin);                                bw.newLine();
            bw.write("player.level=" + p.level);                              bw.newLine();
            bw.write("player.exp=" + p.exp);                                  bw.newLine();
            bw.write("player.nextLevelExp=" + p.nextLevelExp);                bw.newLine();
            bw.write("player.strength=" + p.strength);                        bw.newLine();
            bw.write("player.dexterity=" + p.dexterity);                      bw.newLine();
            bw.write("player.attack=" + p.attack);                            bw.newLine();
            bw.write("player.defense=" + p.defense);                          bw.newLine();
            bw.write("player.currentWeapon=" + p.currentWeapon.getClass().getSimpleName()); bw.newLine();
            bw.write("player.currentShield=" + p.currentShield.getClass().getSimpleName()); bw.newLine();
            bw.write("player.inventorySize=" + p.inventory.size());            bw.newLine();
            for (int i = 0; i < p.inventory.size(); i++) {
                bw.write("player.inventory." + i + "=" + p.inventory.get(i).getClass().getSimpleName());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(int slot) {
        currentSlot = slot;

        Properties props = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath(slot)))) {
            props.load(br);
        } catch (IOException e) {
            return;
        }

        var p = gp.player;
        p.playerClass  = props.getProperty("player.class", "Fighter");
        gp.currentMap  = Integer.parseInt(props.getProperty("currentMap",  "0"));
        p.worldX       = Integer.parseInt(props.getProperty("player.worldX"));
        p.worldY       = Integer.parseInt(props.getProperty("player.worldY"));
        p.direction    = props.getProperty("player.direction", "down");
        p.life         = Integer.parseInt(props.getProperty("player.life"));
        p.maxLife      = Integer.parseInt(props.getProperty("player.maxLife"));
        p.mana         = Integer.parseInt(props.getProperty("player.mana"));
        p.maxMana      = Integer.parseInt(props.getProperty("player.maxMana"));
        p.coin         = Integer.parseInt(props.getProperty("player.coin"));
        p.level        = Integer.parseInt(props.getProperty("player.level"));
        p.exp          = Integer.parseInt(props.getProperty("player.exp"));
        p.nextLevelExp = Integer.parseInt(props.getProperty("player.nextLevelExp"));
        p.strength     = Integer.parseInt(props.getProperty("player.strength"));
        p.dexterity    = Integer.parseInt(props.getProperty("player.dexterity"));
        p.attack       = Integer.parseInt(props.getProperty("player.attack"));
        p.defense      = Integer.parseInt(props.getProperty("player.defense"));

        p.currentWeapon = (ItemEntity) createItem(props.getProperty("player.currentWeapon", "OBJ_Sword_Normal"));
        p.currentShield = (ItemEntity) createItem(props.getProperty("player.currentShield", "OBJ_Shield_Wood"));

        int invSize = Integer.parseInt(props.getProperty("player.inventorySize", "2"));
        p.inventory.clear();
        for (int i = 0; i < invSize; i++) {
            Entity item = createItem(props.getProperty("player.inventory." + i, ""));
            if (item != null) p.inventory.add(item);
        }

        gp.aSetter.setObject();
        gp.aSetter.setNPC();
        gp.aSetter.setMonster();
        gp.aSetter.setCritter();
        gp.aSetter.setInteractiveTile();

        gp.gameState = GameState.PLAY;
        gp.playMusic(0);
    }

    private Entity createItem(String name) {
        return switch (name) {
            case "OBJ_Sword_Normal" -> new OBJ_Sword_Normal(gp);
            case "OBJ_Axe"          -> new OBJ_Axe(gp);
            case "OBJ_Shield_Wood"  -> new OBJ_Shield_Wood(gp);
            case "OBJ_Shield_Blue"  -> new OBJ_Shield_Blue(gp);
            case "OBJ_Key"          -> new OBJ_Key(gp);
            case "OBJ_Door"         -> new OBJ_Door(gp);
            case "OBJ_Chest"        -> new OBJ_Chest(gp);
            case "OBJ_Boots"        -> new OBJ_Boots(gp);
            case "OBJ_Heart"        -> new OBJ_Heart(gp);
            case "OBJ_ManaCrystal"  -> new OBJ_ManaCrystal(gp);
            case "OBJ_Coin_Bronze"  -> new OBJ_Coin_Bronze(gp);
            case "OBJ_Potion_Red"   -> new OBJ_Potion_Red(gp);
            case "OBJ_Rock"         -> new OBJ_Rock(gp);
            case "OBJ_Fireball"     -> new OBJ_Fireball(gp);
            default                 -> null;
        };
    }
}
