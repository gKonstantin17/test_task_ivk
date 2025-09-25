package org.ivk.resourceserver.utils.beans;

import org.ivk.controller.ConsoleController;
import org.ivk.service.GameService;
import org.ivk.view.MessageView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreBeans {
    // бины для Java-core классов
    @Bean
    public GameService gameService() {
        return new GameService();
    }

    @Bean
    public MessageView messageView() {
        return new MessageView();
    }

    @Bean
    public ConsoleController consoleController(GameService gameService) {
        return new ConsoleController(gameService);
    }
}
