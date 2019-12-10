package com.example.smarthome.server;

import com.example.smarthome.server.netty.Server;
import com.example.smarthome.server.repository.TelegramUsersRepository;
import com.example.smarthome.server.repository.TokensRepository;
import com.example.smarthome.server.service.DeviceAccessService;
import com.example.smarthome.server.telegram.Telegram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Main {

    private static final String PROXY_HOST = "151.80.199.89";
    private static final Integer PROXY_PORT = 3128;
    private static final int SERVER_PORT = 3141;
    public static final Logger log;

    static {
        log = LoggerFactory.getLogger(Main.class);
    }

    public static void main(String[] args) throws IOException {
        log.debug("Application started");

        ApplicationContext ctx = SpringApplication.run(Main.class, args);

        // ***** INITIALIZE THE DeviceAccessService CLASS ***********/
        DeviceAccessService service = DeviceAccessService.getInstance();

        service.setTokensRepo(ctx.getBean(TokensRepository.class));
        service.setUsersRepo(ctx.getBean(TelegramUsersRepository.class));
        // *********** NETTY THREAD START **********
        new Server(SERVER_PORT); // starting the netty server

        // *********** TELEGRAM THREAD START ********
        new Telegram(PROXY_HOST, PROXY_PORT);
    }
}
