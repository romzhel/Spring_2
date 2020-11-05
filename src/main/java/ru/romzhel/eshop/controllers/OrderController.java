package ru.romzhel.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.romzhel.eshop.entities.Order;
import ru.romzhel.eshop.services.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/info/{id}")
    public String orderInfo(Model model, @PathVariable("id") Long id) throws Exception {
        Order confirmedOrder = orderService.findById(id);
        model.addAttribute("order", confirmedOrder);
        return "order-result";
        //        response.sendRedirect(request.getHeader("referer"));
    }
}
