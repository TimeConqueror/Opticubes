package io.socol.opticubes;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;

import java.util.Arrays;
import java.util.List;

public class OCClientCommand extends CommandBase {
    private static final String PREFIX = "opticubes.command";
    private static final String USAGE = ".usage";

    @Override
    public String getCommandName() {
        return "opticubes";
    }

    @Override
    public String getCommandUsage(ICommandSender pCommandSender) {
        return PREFIX + USAGE;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            throw new WrongUsageException(PREFIX + USAGE);
        }

        String subCommand = args[0];

        if (subCommand.equals("reloadconfigs")) {
            OCConfigs.load();
            sender.addChatMessage(new ChatComponentTranslation(SubCommand.RELOAD_CONFIGS.prefix("success")));
        } else if (subCommand.equals("help")) {
            for (String command : SubCommand.COMMANDS) {
                sender.addChatMessage(new ChatComponentTranslation(PREFIX + "." + command + USAGE, "/" + getCommandName() + " " + command));
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, SubCommand.COMMANDS);
        }

        return null;
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

