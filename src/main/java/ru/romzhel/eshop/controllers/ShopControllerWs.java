package ru.romzhel.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.romzhel.eshop.entities.Notification;
import ru.romzhel.eshop.services.ShoppingCartService;

import javax.servlet.http.HttpSession;

@Controller
public class ShopControllerWs {
    private ShoppingCartService shoppingCartService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/cart-refreshed")
    public void addProductToCart(Notification notification, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        HttpSession httpSession = (HttpSession) headerAccessor.getSessionAttributes().get("httpSession");
        String sessionId = httpSession.getId();
        long cartItemsCount = shoppingCartService.addToCart(httpSession, notification.getNumericValue());
        simpMessagingTemplate.convertAndSend("/feedback/notifications/" + sessionId, new Notification(cartItemsCount));
    }

    @MessageMapping("/cart-status")
    public void getCartStatus(Notification notification, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        HttpSession httpSession = (HttpSession) headerAccessor.getSessionAttributes().get("httpSession");
        String sessionId = httpSession.getId();
        long cartItemsCount = shoppingCartService.getTotalItemsCount(httpSession);
        simpMessagingTemplate.convertAndSend("/feedback/notifications/" + sessionId, new Notification(cartItemsCount));
    }
}
