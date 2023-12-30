package angga7togk.shopgui.menu;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import me.iwareq.fakeinventories.FakeInventory;
import me.onebone.economyapi.EconomyAPI;
import ru.nukkitx.forms.elements.CustomForm;
import ru.nukkitx.forms.elements.SimpleForm;

import java.util.*;

import angga7togk.shopgui.ShopGUI;

public class SGMenu {

    private final ShopGUI plugin;
    public SGMenu(ShopGUI plugin){
        this.plugin = plugin;
    }

    public void mainShop(Player player){
        FakeInventory inv = new FakeInventory(InventoryType.DOUBLE_CHEST);
        inv.setTitle(TextFormat.BOLD + "SHOP");
        Map<String, String> itemEvent = new HashMap<>();
        for (String category : plugin.shop.getSection("category").getKeys(false)){
            String[] items = plugin.shop.getString("category." + category).split(":");
            inv.addItem( Item.get(Integer.parseInt(items[0]), Integer.parseInt(items[1]), 1).setCustomName(TextFormat.AQUA + category));
            itemEvent.put(items[0] + ":" + items[1], category);
        }

        inv.setDefaultItemHandler((item, event) -> {
            event.setCancelled();
            Player target = event.getTransaction().getSource();
            String itemIds = item.getId() + ":" + item.getDamage();
            categoryShop(target, itemEvent.get(itemIds), 1);
        });
        player.addWindow(inv);
    }

    public void categoryShop(Player player, String category, int page){
        FakeInventory inv = new FakeInventory(InventoryType.DOUBLE_CHEST);
        inv.setTitle(TextFormat.BOLD + "SHOP | " + category);
        double myMoney = ShopGUI.getProvider().myMoney(player);
        Map<Integer, Map<String, Double>> pageMap = new HashMap<>();

        // Membuat Page Map :V
        int pageI = 1;
        int i = 0;
        pageMap.put(pageI, new HashMap<>());
        for (String itemString : plugin.shop.getSection("shop." + category).getKeys(false)){
            pageMap.get(pageI).put(itemString, plugin.shop.getDouble("shop." + category + "." + itemString));
            if(i == 35){
                pageI++;
                i = 0;
                pageMap.put(pageI, new HashMap<>());
            }
            i++;
        }

        // Add item
        Set<String> keySet = pageMap.get(page).keySet();
        for(String itemKeys : keySet){
            String[] items = itemKeys.split(":");
            double price = plugin.shop.getDouble("shop." + category + "." + itemKeys);
            inv.addItem(Item.get(Integer.parseInt(items[0]), Integer.parseInt(items[1])).setLore(TextFormat.GOLD + "My Money, " + TextFormat.GREEN + myMoney, TextFormat.GOLD + "Price, " + TextFormat.GREEN + price));
        }

        inv.setItem(47, Item.get(402, 14, 1).setCustomName(TextFormat.AQUA + "Previous").setLore(TextFormat.YELLOW + "(" + page + "/" + pageMap.size()+")"));
        inv.setItem(49, Item.get(331, 0, 1).setCustomName(TextFormat.YELLOW + "Back To Main Shop"));
        inv.setItem(51, Item.get(402, 1, 1).setCustomName(TextFormat.AQUA + "Next").setLore(TextFormat.YELLOW + "(" + page + "/" + pageMap.size()+")"));

        inv.setDefaultItemHandler((item, event) -> {
            event.setCancelled();
            Player target = event.getTransaction().getSource();
            String itemIds = item.getId() + ":" + item.getDamage();
            double price = plugin.shop.getLong("shop." + category + "." + itemIds);
            if (item.getCustomName().equals(TextFormat.AQUA + "Previous")) {
                if (page > 1) {
                    categoryShop(target, category, page - 1);
                }
            } else if (item.getCustomName().equals(TextFormat.AQUA + "Next")) {
                if (page < pageMap.size()) {
                    categoryShop(target, category, page + 1);
                }
            } else if (item.getCustomName().equals(TextFormat.YELLOW + "Back To Main Shop")) {
                mainShop(target);
            } else {
                player.removeWindow(inv);
                onCheckout(target, Item.get(item.getId(), item.getDamage()), price);
            }

        });

        player.addWindow(inv);
    }

    protected void onCheckout(Player player, Item item, double price){
        CustomForm form = new CustomForm(TextFormat.BOLD + "Checkout");
        form.addLabel("§ehow much do you want to buy?");
        form.addInput("Amount", "64");

        form.send(player, (targetP, targetF, data) -> {
            if (data == null) return;
            if (data.get(1) == null) {
                targetP.sendMessage(ShopGUI.prefix + "§cthe form cannot be empty!");
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(data.get(1).toString());
            } catch (NumberFormatException e) {
                targetP.sendMessage(ShopGUI.prefix + "§cplease enter numbers!");
                return;
            }

            Inventory inv = targetP.getInventory();
            double myMoney = ShopGUI.getProvider().myMoney(targetP);
            if (inv.isFull()) {
                targetP.sendMessage(ShopGUI.prefix + "§cInventory is full!");
                return;
            }

            if(myMoney < (price * amount)){
                targetP.sendMessage(ShopGUI.prefix + "§cYou dont have enough money!");
                return;
            }

            item.setCount(amount);
            onBuy(targetP, item, (price * amount));
        });
    }

    protected void onBuy(Player player, Item item, double totalPrice){
        SimpleForm form = new SimpleForm(TextFormat.BOLD + "Confirmation");
        form.setContent("§l§ePurchase details\n§rItems, §a" + item.getName() + "\n§rAmount, §a" + item.getCount() + "\n§rTotalPrice, §a" + totalPrice + "\n\n§e@7togksmp");
        form.addButton("§l§aBuy Now\n§rTap To Buy");
        form.addButton("§l§cCancel\n§rTap To Cancel");
        form.send(player, (targetP, targetF, data) -> {
            if(data == -1 || data == 1){
                targetP.sendMessage(ShopGUI.prefix + "§corder cancelled");
                return;
            }
            Inventory inv = targetP.getInventory();
            inv.addItem(item);
            ShopGUI.getProvider().reduceMoney(targetP, totalPrice);
            player.sendMessage(ShopGUI.prefix + "§aSuccessfully to buy " + item.getCount() + " " + item.getName());
        });
    }
}
