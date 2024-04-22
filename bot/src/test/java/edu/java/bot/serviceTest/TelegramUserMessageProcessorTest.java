package edu.java.bot.serviceTest;

import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.service.TelegramUserMessageProcessor;
import edu.java.bot.service.commands.Command;
import edu.java.bot.service.commands.HelpCommand;
import edu.java.bot.service.commands.ListCommand;
import edu.java.bot.service.commands.StartCommand;
import edu.java.bot.service.commands.TrackCommand;
import edu.java.bot.service.commands.UntrackCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class TelegramUserMessageProcessorTest {
    private TelegramUserMessageProcessor messageProcessor;

    @BeforeEach
    public void setup() {
        List<Command> commands = new ArrayList<>();
        commands.add(new StartCommand());
        commands.add(new ListCommand());
        commands.add(new TrackCommand());
        commands.add(new UntrackCommand());

        HelpCommand helpCommand = new HelpCommand(commands);
        messageProcessor = new TelegramUserMessageProcessor(commands);
    }

    @Test
    @DisplayName("Проверка метода commands")
    public void commandsTest() {
        List<? extends Command> commandList = messageProcessor.commands();
        Assertions.assertEquals(5, commandList.size());
    }

    @Test
    @DisplayName("Проверка метода commandsForMenu")
    public void commandsForMenuTest() {
        BotCommand[] botCommands = messageProcessor.commandsForMenu();
        Assertions.assertEquals(5, botCommands.length);
    }
}
