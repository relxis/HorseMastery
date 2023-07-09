package me.classes;

import org.bukkit.entity.Horse;

import java.io.Serializable;

import static me.relxis.HorseMastery.plugin;


public class HorseInformation implements Serializable {

    private static double divisor = 0.0;
    private int tier;
    private int level;
    private int levelCap;
    private double Exp;
    private Horse.Color color;
    private Horse.Style style;

    public HorseInformation(int tiers, int levels, int levelCaps, double xps, Horse.Color colors, Horse.Style styles) {
        this.setTier(tiers);
        this.setLevel(levels);
        this.setLevelCap(levelCaps);
        this.setExp(xps);
        this.setColor(colors);
        this.setStyle(styles);
    }
    public Horse.Style getStyle() {
        return style;
    }

    public void setStyle(Horse.Style style) {
        this.style = style;
    }

    public Horse.Color getColor() {
        return color;
    }

    public void setColor(Horse.Color color) {
        this.color = color;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.setExp(calculateLevelInverse(level-1));
    }

    public int getLevelCap() {
        return levelCap;
    }

    public void setLevelCap(int levelCap) {
        this.levelCap = levelCap;
    }

    public double getExp() {
        return Exp;
    }

    public void setExp(double exp) {
        this.Exp = exp;
        this.level = calculateLevel(exp);
    }

    private int calculateLevel(double exp) {
        double target = exp / divisor;
        int n = 1;
        double sum = 0;
        while (sum <= target) {
            sum += n;
            n++;
        }
        return n - 1;
    }

    private double calculateLevelInverse(int level) {
        int n = 1;
        double sum = 0;
        while (n <= level) {
            sum += n;
            n++;
        }
        return sum * divisor;
    }
    public double getExpPercentage() {
        double requiredExp = calculateLevelInverse(level) - calculateLevelInverse(level-1);
        double currentExp = this.getExp() - calculateLevelInverse(level-1);
        return Math.floor(currentExp/requiredExp * 1000) /10;
    }

    public void updateDivisor() {
        divisor = (double) plugin.getConfig().getInt("Experience required to reach maximum level") / 780;
    }

}
