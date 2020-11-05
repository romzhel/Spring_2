package ru.romzhel.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.romzhel.eshop.entities.DeliveryAddress;
import ru.romzhel.eshop.entities.Order;
import ru.romzhel.eshop.entities.Product;
import ru.romzhel.eshop.entities.User;
import ru.romzhel.eshop.repositories.specifications.ProductSpecs;
import ru.romzhel.eshop.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 5;

    private MailService mailService;
    private UserService userService;
    private OrderService orderService;
    private ProductService productService;
    private ShoppingCartService shoppingCartService;
    private DeliveryAddressService deliverAddressService;
    private CategoryService categoryService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setDeliverAddressService(DeliveryAddressService deliverAddressService) {
        this.deliverAddressService = deliverAddressService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping
    public String shopPage(Model model, HttpServletRequest request,
                           @RequestParam(value = "page") Optional<Integer> page,
                           @RequestParam(value = "categ", required = false) String category,
                           @RequestParam(value = "word", required = false) String word,
                           @RequestParam(value = "min", required = false) Double min,
                           @RequestParam(value = "max", required = false) Double max
    ) {
        request.getSession(); //TODO иначе ошибка при переходе на траницу /shop сразу после запуска проекта

        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Specification<Product> spec = Specification.where(null);
        StringBuilder filters = new StringBuilder();

        if (category != null && !category.equals("Все категории")) {
            spec = spec.and(ProductSpecs.categoryEquals(categoryService.getCategoryByName(category)));
            filters.append("&categ=" + category);
        }
        if (word != null) {
            spec = spec.and(ProductSpecs.titleContains(word));
            filters.append("&word=" + word);
        }
        if (min != null) {
            spec = spec.and(ProductSpecs.priceGreaterThanOrEq(min));
            filters.append("&min=" + min);
        }
        if (max != null) {
            spec = spec.and(ProductSpecs.priceLesserThanOrEq(max));
            filters.append("&max=" + max);
        }

        Page<Product> products = productService.getProductsWithPagingAndFiltering(currentPage, PAGE_SIZE, spec);

        model.addAttribute("products", products.getContent());
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", products.getTotalPages());

        model.addAttribute("filters", filters.toString());

        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("word", word);
        model.addAttribute("category", category);

        model.addAttribute("categories", categoryService.getAllCategories());

        return "shop-page";
    }

    /*@GetMapping("/cart/add/{id}")
    public String addProductToCart(Model model, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        shoppingCartService.addToCart(httpServletRequest.getSession(), id);
        String referrer = httpServletRequest.getHeader("referer");
        return "redirect:" + referrer;
    }*/

    @GetMapping("/order/fill")
    public String orderFill(Model model, HttpServletRequest httpServletRequest, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        Order order = orderService.makeOrder(shoppingCartService.getCurrentCart(httpServletRequest.getSession()), user);
        List<DeliveryAddress> deliveryAddresses = deliverAddressService.getUserAddresses(user.getId());
        DeliveryAddress newAddress = new DeliveryAddress();
        newAddress.setUser(user);
        newAddress.setId(0L);
        model.addAttribute("order", order);
        model.addAttribute("deliveryAddresses", deliveryAddresses);
        model.addAttribute("newAddress", newAddress);
        return "order-filler";
    }

    @PostMapping("/order/addAddress")
    public String addAddress(@ModelAttribute DeliveryAddress newAddress) {
        deliverAddressService.addDeliveryAddress(newAddress);
        return "redirect:/shop/order/fill";
    }

    @GetMapping("/order/removeAddress/{id}")
    public String addAddress(@PathVariable Long id) {
        if (id != null) {
            deliverAddressService.deleteDeliveryAddress(id);
        }
        return "redirect:/shop/order/fill";
    }

    @PostMapping("/order/confirm")
    public String orderConfirm(@Valid @ModelAttribute("order") Order orderFromFrontend, BindingResult theBindingResult,
                               Principal principal, Model model, HttpServletRequest httpServletRequest) {
        if (principal == null) {
            return "redirect:/login";
        }

        if (theBindingResult.hasErrors()) {
            orderFromFrontend.setOrderItems(shoppingCartService.getCurrentCart(httpServletRequest.getSession()).getItems());
            User user = userService.findByUserName(principal.getName());
            List<DeliveryAddress> deliveryAddresses = deliverAddressService.getUserAddresses(user.getId());
            DeliveryAddress newAddress = new DeliveryAddress();
            newAddress.setUser(user);
            newAddress.setId(0L);
            model.addAttribute("deliveryAddresses", deliveryAddresses);
            model.addAttribute("newAddress", newAddress);
            return "order-filler";
        }

        User user = userService.findByUserName(principal.getName());
        Order order = orderService.makeOrder(shoppingCartService.getCurrentCart(httpServletRequest.getSession()), user);
        order.setDeliveryAddress(orderFromFrontend.getDeliveryAddress());
        order.setPhoneNumber(orderFromFrontend.getPhoneNumber());
        order.setDeliveryDate(LocalDateTime.now().plusDays(7));
        order.setDeliveryPrice(0.0);
        order = orderService.saveOrder(order);
        model.addAttribute("order", order);

        shoppingCartService.resetCart(httpServletRequest.getSession());

        return "redirect:/shop/order/result/" + order.getId();
    }

    @GetMapping("/order/result/{id}")
    public String orderConfirm(Model model, @PathVariable(name = "id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        // todo ждем до оплаты, проверка безопасности и проблема с повторной отправкой письма сделать одноразовый вход
        User user = userService.findByUserName(principal.getName());
        Order confirmedOrder = orderService.findById(id);
        if (!user.getId().equals(confirmedOrder.getUser().getId())) {
            return "redirect:/";
        }
//        mailService.sendOrderMail(confirmedOrder);
        model.addAttribute("order", confirmedOrder);
        return "order-result";
    }
}
