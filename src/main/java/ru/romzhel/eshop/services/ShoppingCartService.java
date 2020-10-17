package ru.romzhel.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.romzhel.eshop.entities.Product;
import ru.romzhel.eshop.utils.ShoppingCart;

import javax.servlet.http.HttpSession;

@Service
public class ShoppingCartService {
    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public ShoppingCart getCurrentCart(HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public void resetCart(HttpSession session) {
        session.removeAttribute("cart");
    }

    public int addToCart(HttpSession session, Long productId) {
        Product product = productService.getProductById(productId);
        return addToCart(session, product);
    }

    public int addToCart(HttpSession session, Product product) {
        ShoppingCart cart = getCurrentCart(session);
        cart.add(product);
        return cart.getTotalItemsCount();
    }

    public int removeFromCart(HttpSession session, Long productId) {
        Product product = productService.getProductById(productId);
        return removeFromCart(session, product);
    }

    public int removeFromCart(HttpSession session, Product product) {
        ShoppingCart cart = getCurrentCart(session);
        cart.remove(product);
        return cart.getTotalItemsCount();
    }

    public void setProductCount(HttpSession session, Long productId, Long quantity) {
        ShoppingCart cart = getCurrentCart(session);
        Product product = productService.getProductById(productId);
        cart.setQuantity(product, quantity);
    }

    public void setProductCount(HttpSession session, Product product, Long quantity) {
        ShoppingCart cart = getCurrentCart(session);
        cart.setQuantity(product, quantity);
    }

    public double getTotalCost(HttpSession session) {
        return getCurrentCart(session).getTotalCost();
    }

    public int getTotalItemsCount(HttpSession httpSession) {
        return getCurrentCart(httpSession).getTotalItemsCount();
    }
}
