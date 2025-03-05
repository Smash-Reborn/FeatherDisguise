package org.reborn.FeatherDisguise.util;

import com.github.retrooper.packetevents.util.ColorUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Constants {

    public static final String PLUGIN_NAME = "FeatherDisguise";

    public static final String EVERYONE_SYN = "@e";

    @NotNull public static String formattedNeutralText(@NotNull String text) {
        return log(
                ColorUtil.toString(NamedTextColor.WHITE) + "[" +
                ColorUtil.toString(NamedTextColor.AQUA) + "/" +
                ColorUtil.toString(NamedTextColor.WHITE) + "] " +
                text);
    }

    @NotNull public static String formattedPositiveText(@NotNull String text) {
        return log(
                ColorUtil.toString(NamedTextColor.WHITE) + "[" +
                ColorUtil.toString(NamedTextColor.GREEN) + "o" +
                ColorUtil.toString(NamedTextColor.WHITE) + "] " +
                text);
    }

    @NotNull public static String formattedNegativeText(@NotNull String text) {
        return log(
                ColorUtil.toString(NamedTextColor.WHITE) + "[" +
                ColorUtil.toString(NamedTextColor.RED) + "x" +
                ColorUtil.toString(NamedTextColor.WHITE) + "] " +
                text);
    }

    // todo AHHHH WHY ISN'T THIS WORKING FUUUUUUUUUUUCk
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '\u00A7' + "[0-9A-FK-ORX]");

    @NotNull private static String log(@NotNull String text) {
        text = STRIP_COLOR_PATTERN.matcher(text).replaceAll(""); // strip away the color codes that are in the message
        return text;
    }

    @NotNull public static String removeUndrNCapFrstLtr(@NotNull String string) {
        string = string.replaceAll("_", " ");
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
