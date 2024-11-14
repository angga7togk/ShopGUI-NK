package angga7togk.shopgui.provider;

import angga7togk.economyapi.EconomyAPI;
import cn.nukkit.Player;

public class Angga7Togk implements Provider {

    @Override
    public double myMoney(Player player) {
        return (double) EconomyAPI.getInstance().myMoney(player);
    }

    @Override
    public void setMoney(Player player, double money) {
        EconomyAPI.getInstance().setMoney(player, (int) money);
    }

    @Override
    public void addMoney(Player player, double money) {
        EconomyAPI.getInstance().addMoney(player, (int) money);
    }

    @Override
    public void reduceMoney(Player player, double money) {
        EconomyAPI.getInstance().reduceMoney(player, (int) money);
    }
}
