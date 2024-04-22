package edu.java.bot.serviceTest.commandTests;

import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.service.commands.Command;
import edu.java.bot.service.commands.HelpCommand;
import edu.java.bot.service.commands.ListCommand;
import edu.java.bot.service.commands.StartCommand;
import edu.java.bot.service.commands.TrackCommand;
import edu.java.bot.service.commands.UntrackCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

public class HelpCommandTest {
    private HelpCommand helpCommand;

    @BeforeEach
    public void setup() {
        List<Command> commands = new ArrayList<>();
        commands.add(new StartCommand());
        commands.add(new TrackCommand());
        commands.add(new UntrackCommand());
        commands.add(new ListCommand());
        helpCommand = new HelpCommand(commands);
    }

    @Test
    public void toApiCommandTest() {
        BotCommand botCommandMock = Mockito.mock(BotCommand.class);

        when(botCommandMock.command()).thenReturn("/help");
        when(botCommandMock.description()).thenReturn("Вывести окно с командами");

        BotCommand result = helpCommand.toApiCommand();
        Assertions.assertEquals(botCommandMock.command(), result.command());
        Assertions.assertEquals(botCommandMock.description(), result.description());
    }
}
