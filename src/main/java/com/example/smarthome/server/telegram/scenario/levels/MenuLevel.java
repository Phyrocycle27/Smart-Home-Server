package com.example.smarthome.server.telegram.scenario.levels;

import com.example.smarthome.server.service.DeviceAccessService;
import com.example.smarthome.server.telegram.Bot;
import com.example.smarthome.server.telegram.MessageExecutor;
import com.example.smarthome.server.telegram.UserInstance;
import com.example.smarthome.server.telegram.objects.IncomingMessage;
import com.example.smarthome.server.telegram.objects.MessageType;
import com.example.smarthome.server.telegram.objects.callback.AnswerCallback;
import com.example.smarthome.server.telegram.objects.callback.CallbackButton;
import com.example.smarthome.server.telegram.objects.inlinemsg.InlineKeyboardMessage;
import com.example.smarthome.server.telegram.scenario.AnswerCreator;

import java.util.ArrayList;
import java.util.List;

public class MenuLevel implements AnswerCreator {

    private static final MenuLevel instance = new MenuLevel();

    private static final DeviceAccessService service = DeviceAccessService.getInstance();
    private static final Bot bot = Bot.getInstance();

    // ************************************* MESSAGES *************************************************
    private static final String buttonInvalid = "Кнопка недействительна";
    private static final String menuMsg = "Нажмите \"Управление домом\" чтобы перейти к управлению умным домом и " +
            "просмотру информации с датчиков или нажмите \"Информация\", чтобы узнать точное время или погоду";

    // ************************************** BUTTONS *************************************************
    private static final List<CallbackButton> menuButtons = new ArrayList<CallbackButton>() {{
        add(new CallbackButton("Управление домом", "home_control"));
        add(new CallbackButton("Информация", "information"));
    }};

    private MenuLevel() {
    }

    public static MenuLevel getInstance() {
        return instance;
    }

    @Override
    public void create(UserInstance user, IncomingMessage msg) {
        if (msg.getType() == MessageType.CALLBACK)
            switch (msg.getText()) {
                case "home_control":
                    HomeControlLevel.goToHomeControlLevel(user, msg);
                    break;
                case "information":
                    InformationLevel.goToInformationLevel(user, msg);
                    break;
                default:
                    if (!msg.getCallbackId().isEmpty())
                        MessageExecutor.execute(bot, new AnswerCallback(msg.getCallbackId(), buttonInvalid));
            }
    }

    public static void goToMain(UserInstance user, IncomingMessage msg) {
        MessageExecutor.execute(bot, new InlineKeyboardMessage(user.getChatId(), menuMsg, menuButtons)
                .setMessageId(msg.getId())
                .setNumOfColumns(2));

        user.setCurrentLvl(instance);
    }
}