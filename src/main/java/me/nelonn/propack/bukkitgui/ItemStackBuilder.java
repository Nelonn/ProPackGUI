package me.nelonn.propack.bukkitgui;

import com.google.common.collect.Multimap;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemStackBuilder {
    protected static final String TAG_DISPLAY = "display";
    protected static final String TAG_NAME = "Name";
    protected static final String TAG_LORE = "Lore";
    protected final ItemStack itemStack;
    protected final NBTItem nbt;

    public ItemStackBuilder(@NotNull ItemStack item, boolean directApply) {
        //MareLibPlugin.checkNBTAPI();
        if (item.getType().isAir()) {
            throw new NullPointerException("ItemStack can't be null/Air!");
        }
        this.itemStack = directApply ? item : item.clone();
        this.nbt = new NBTItem(itemStack, true);
    }

    public ItemStackBuilder(@NotNull ItemStack item) {
        this(item, false);
    }

    public ItemStackBuilder(@NotNull Material material) {
        this(new ItemStack(material), true);
    }

    public ItemStackBuilder setType(@NotNull Material type) {
        if (type == Material.AIR) {
            throw new NullPointerException("Material can't be Air!");
        }
        itemStack.setType(type);
        return this;
    }

    public ItemStackBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder setCustomModelData(Integer cmd) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(cmd);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @NotNull
    protected BaseComponent getNotItalicComponent(@NotNull BaseComponent component) {
        BaseComponent c = component.duplicate();
        if (c.isItalicRaw() == null) {
            c.setItalic(false);
        }
        return c;
    }

    protected BaseComponent[] getNotItalicComponents(BaseComponent... components) {
        BaseComponent[] lore = new BaseComponent[components.length];
        for (int i = 0; i < components.length; i++) {
            lore[i] = getNotItalicComponent(components[i]);
        }
        return lore;
    }

    public ItemStackBuilder setDisplayName(@Nullable String displayName) {
        return displayName == null ? setDisplayName() : setDisplayName(TextUtils.colorComponents(displayName));
    }

    public ItemStackBuilder setDisplayName(BaseComponent... displayName) {
        NBTCompound display = nbt.getOrCreateCompound(TAG_DISPLAY);
        if (displayName.length == 0) {
            display.setString(TAG_NAME, null);
        } else {
            display.setString(TAG_NAME, ComponentSerializer.toString(getNotItalicComponents(displayName)));
        }
        return this;
    }

    public ItemStackBuilder setLore(String... lines) {
        NBTCompound display = nbt.getOrCreateCompound(TAG_DISPLAY);
        NBTList<String> loreList = display.getStringList(TAG_LORE);
        loreList.clear();
        for (String line : lines) {
            loreList.add(ComponentSerializer.toString(getNotItalicComponents(TextUtils.colorComponents(line))));
        }
        return this;
    }

    public ItemStackBuilder setLore(BaseComponent... lines) {
        NBTCompound display = nbt.getOrCreateCompound(TAG_DISPLAY);
        NBTList<String> loreList = display.getStringList(TAG_LORE);
        loreList.clear();
        for (BaseComponent component : getNotItalicComponents(lines)) {
            loreList.add(ComponentSerializer.toString(component));
        }
        return this;
    }

    public ItemStackBuilder addLore(int index, @NotNull String line) {
        return addLore(index, TextUtils.colorComponents(line));
    }

    public ItemStackBuilder addLore(@NotNull String line) {
        return addLore(TextUtils.colorComponents(line));
    }

    public ItemStackBuilder addLore(int index, BaseComponent... line) {
        NBTCompound display = nbt.getOrCreateCompound(TAG_DISPLAY);
        display.getStringList(TAG_LORE).add(index, ComponentSerializer.toString(getNotItalicComponents(line)));
        return this;
    }

    public ItemStackBuilder addLore(BaseComponent... line) {
        NBTCompound display = nbt.getOrCreateCompound(TAG_DISPLAY);
        display.getStringList(TAG_LORE).add(ComponentSerializer.toString(getNotItalicComponents(line)));
        return this;
    }

    public ItemStackBuilder addEnchant(@NotNull Enchantment enchantment, int i, boolean b) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.addEnchant(enchantment, i, b);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder removeEnchant(@NotNull Enchantment enchantment) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.removeEnchant(enchantment);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder addFlags(@NotNull ItemFlag... flags) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.addItemFlags(flags);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder removeFlags(@NotNull ItemFlag... flags) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.removeItemFlags(flags);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.addAttributeModifier(attribute, modifier);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> multimap) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setAttributeModifiers(multimap);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder removeAttributeModifier(@NotNull Attribute attribute) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.removeAttributeModifier(attribute);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder removeAttributeModifier(@NotNull EquipmentSlot equipmentSlot) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.removeAttributeModifier(equipmentSlot);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder removeAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.removeAttributeModifier(attribute, modifier);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack getItem() {
        return itemStack;
    }

    public NBTItem getNBT() {
        return nbt;
    }
}