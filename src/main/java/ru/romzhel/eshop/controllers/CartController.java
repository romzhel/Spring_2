package ru.romzhel.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.romzhel.eshop.entities.Product;
import ru.romzhel.eshop.services.ProductService;
import ru.romzhel.eshop.services.ShoppingCartService;
import ru.romzhel.eshop.utils.ShoppingCart;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
    private ShoppingCartService shoppingCartService;
    private ProductService productService;

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String cartPage(Model model, HttpSession httpSession) {
        ShoppingCart cart = shoppingCartService.getCurrentCart(httpSession);
        model.addAttribute("cart", cart);
        return "cart-page";
    }

    @GetMapping("/set/{id}/{quantity}")
    public String setQuantity(Model model, HttpSession httpSession, @PathVariable Long id, @PathVariable Long quantity) {
        ShoppingCart cart = shoppingCartService.getCurrentCart(httpSession);
        Product product = productService.getProductById(id);
        cart.setQuantity(product, quantity);
        model.addAttribute("cart", cart);
        return "cart-page";
    }

    @GetMapping("/remove/{id}")
    public String remove(Model model, HttpSession httpSession, @PathVariable Long id) {
        ShoppingCart cart = shoppingCartService.getCurrentCart(httpSession);
        Product product = productService.getProductById(id);
        cart.remove(product);
        model.addAttribute("cart", cart);
        return "cart-page";
    }
}
