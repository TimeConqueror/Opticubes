package io.socol.opticubes.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class ChatComponentExt {
    public static <T extends ITextComponent> T withColor(T component, TextFormatting color) {
        component.setStyle(component.getStyle().setColor(color));
        return component;
    }
}