package ru.romzhel.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.romzhel.eshop.entities.Category;
import ru.romzhel.eshop.entities.Order;
import ru.romzhel.eshop.entities.Role;
import ru.romzhel.eshop.entities.User;
import ru.romzhel.eshop.services.CategoryService;
import ru.romzhel.eshop.services.OrderService;
import ru.romzhel.eshop.services.RoleService;
import ru.romzhel.eshop.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private OrderService orderService;
    private UserService userService;
    private RoleService roleService;
    private CategoryService categoryService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showAdminDashboard() {
        return "admin-panel";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders-page";
    }

    @GetMapping("/orders/ready/{id}")
    public void orderReady(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) throws Exception {
        Order order = orderService.findById(id);
        order.setDeliveryDate(LocalDateTime.now());
        orderService.changeOrderStatus(order, 2L);
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/orders/info/{id}")//todo реализовать просмотр информации о заказе
    public String orderInfo(Model model, @PathVariable("id") Long id) throws Exception {
        Order confirmedOrder = orderService.findById(id);
        model.addAttribute("order", confirmedOrder);
        return "order-result";
        //        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/users")
    public String usersInfo(Model model) throws Exception {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "admin-users";
    }

    @GetMapping("/users/{login}")
    public String userInfo(Model model, @PathVariable String login) {
        User user = userService.findByUserName(login);
        Iterable<Role> allRoles = roleService.getAllRoles();
        List<Role> newRoles = new ArrayList<>();
        allRoles.forEach(newRoles::add);
        newRoles.removeAll(user.getRoles());
        model.addAttribute("user", user);
        model.addAttribute("newRoles", newRoles);
        model.addAttribute("newRole", new Role());
        return "edit-user";
    }

    @GetMapping("/users/{login}/deleteRole/{roleId}")
    public String userDeleteRole(Model model, @PathVariable String login, @PathVariable Long roleId) {
        User user = userService.findByUserName(login);
        Role role = roleService.getRoleById(roleId);
        user.getRoles().remove(role);
        userService.save(user);
        model.addAttribute("user", user);
        return "redirect:/admin/users/" + login;
    }

    @PostMapping("/users/{login}/addRole")
    public String userAddRole(Model model, @PathVariable String login, @ModelAttribute Role newRole) {
        User user = userService.findByUserName(login);
        Role role = roleService.getRoleById(newRole.getId());
        user.getRoles().add(role);
        userService.save(user);
        model.addAttribute("user", user);
        return "redirect:/admin/users/" + login;
    }

    @GetMapping("/users/{login}/delete")
    public String removeUser(Model model, @PathVariable String login) {
        userService.deleteByUserName(login);
        return "redirect:/admin/users";
    }

    @GetMapping("categories")
    public String categories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin-categories";
    }

    @GetMapping("categories/edit/{id}")
    public String editCategory(Model model, @PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "edit-category";
    }

    @PostMapping("categories/edit")
    public String addCategory(Model model, @ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("categories/remove/{id}")
    public String removeCategory(Model model, @PathVariable Long id) {
        categoryService.removeCategory(id);
        return "redirect:/admin/categories";
    }
}
