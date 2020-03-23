package com.example.smarthome.server.telegram.scenario.levels.administration_users;

import com.example.smarthome.server.telegram.EmojiCallback;
import com.example.smarthome.server.telegram.UserInstance;
import com.example.smarthome.server.telegram.objects.IncomingMessage;
import com.example.smarthome.server.telegram.objects.MessageType;
import com.example.smarthome.server.telegram.objects.inlinemsg.InlineKeyboardMessage;
import com.example.smarthome.server.telegram.scenario.AnswerCreator;

import static com.example.smarthome.server.telegram.MessageExecutor.deleteAsync;
import static com.example.smarthome.server.telegram.MessageExecutor.executeAsync;
import static com.example.smarthome.server.telegram.scenario.levels.administration_users.UserSetupRoleLevel.goToUserSetupRoleLevel;
import static com.example.smarthome.server.telegram.scenario.levels.administration_users.UsersLevel.goToUsersLevel;

public class UserAdditionLevel implements AnswerCreator {

    private static final UserAdditionLevel instance = new UserAdditionLevel();

    // ************************************* MESSAGES *************************************************
    private static final String sendUserContact = "Отправьте контакт пользователя, которого хотите добавить";

    private UserAdditionLevel() {
    }

    public static UserAdditionLevel getInstance() {
        return instance;
    }


    @Override
    public boolean create(UserInstance user, IncomingMessage msg) {
        if (msg.getType() == MessageType.CALLBACK && msg.getText().equals("back")) {
            goToUsersLevel(user, msg);
            EmojiCallback.back(msg.getCallbackId());
            // если сообщение успешно обработано, то возвращаем истину
            return true;
        } else if (msg.getType() == MessageType.CONTACT) {
            try {
                goToUserSetupRoleLevel(user, msg, Long.parseLong(msg.getText()));
            } finally {
                deleteAsync(user.getChatId(), user.getLastMessageId(), () -> user.setLastMessageId(0));
            }
            // если сообщение успешно обработано, то возвращаем истину
            return true;
        }
        // иначе, если содержание сообщения не может быть обработано уровнем, возвращаем ложь
        return false;
    }

    public static void goToUserAdditionLevel(UserInstance user, IncomingMessage msg) {
        executeAsync(new InlineKeyboardMessage(user.getChatId(), sendUserContact, null)
                .hasBackButton(true).setMessageId(msg.getId()), () -> {
            user.setLastMessageId(msg.getId());
            user.setCurrentLvl(instance);
        });
    }
}
