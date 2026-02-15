package io.socol.opticubes;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.IClientCommand;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OCClientCommand extends CommandBase implements IClientCommand {
    private static final String PREFIX = "opticubes.command";
    private static final String USAGE = ".usage";

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    @Override
    public String getName() {
        return "opticubes";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return PREFIX + USAGE;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException(PREFIX + USAGE);
        }

        String subCommand = args[0];

        if (subCommand.equals("reloadconfigs")) {
            OCConfigs.load();
            sender.sendMessage(new TextComponentTranslation(SubCommand.RELOAD_CONFIGS.prefix("success")));
        } else if (subCommand.equals("help")) {
            for (String command : SubCommand.COMMANDS) {
                sender.sendMessage(new TextComponentTranslation(PREFIX + "." + command + USAGE, "/" + getName() + " " + command));
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, SubCommand.COMMANDS);
        }

        return Collections.emptyList();
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] args, String... possibilities) {
        //noinspection unchecked
        return CommandBase.getListOfStringsMatchingLastWord(args, possibilities);
    }

    public enum SubCommand {
        RELOAD_CONFIGS("reloadconfigs"),
        HELP("help");

        private final String command;

        private static final String[] COMMANDS = Arrays.stream(values()).map(subCommand -> subCommand.command).toArray(String[]::new);

        SubCommand(String command) {
            this.command = command;
        }

        public String prefix(String key) {
            return PREFIX + "." + command + "." + key;
        }
    }
}

