package ru.romzhel.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romzhel.eshop.entities.Product;
import ru.romzhel.eshop.entities.ProductImage;
import ru.romzhel.eshop.services.CategoryService;
import ru.romzhel.eshop.services.ImageSaverService;
import ru.romzhel.eshop.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    private CategoryService categoryService;
    private ImageSaverService imageSaverService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setImageSaverService(ImageSaverService imageSaverService) {
        this.imageSaverService = imageSaverService;
    }

    @GetMapping("/show/{id}")
    public String show(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.getProductById(id));
        return "product-page";
    }

    @GetMapping("/edit/delete/{page}/{id}")
    public String delete(Model model, @PathVariable Integer page, @PathVariable(name = "id") Long id) {
        productService.deleteProductById(id);
        model.addAttribute("page", page);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/shop?page=" + page;
    }

    @GetMapping("/edit/{page}/{id}")
    public String edit(Model model, @PathVariable Integer page, @PathVariable(name = "id") Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            product = new Product();
            product.setId(0L);
        }
        model.addAttribute("page", page);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-product";
    }

    @PostMapping("/edit")
    public String processProductAddForm(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam("page") Integer page, @Valid @ModelAttribute("product") Product product,
                                        BindingResult theBindingResult, Model model, @RequestParam("file") MultipartFile file) {
        if (product.getId() == 0 && productService.isProductWithTitleExists(product.getTitle())) {
            theBindingResult.addError(new ObjectError("product.title", "Товар с таким названием уже существует")); // todo не отображает сообщение
        }

        if (theBindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("page", page);
            return "edit-product";
        }

        if (!file.isEmpty()) {
            String pathToSavedImage = imageSaverService.saveFile(file);
            ProductImage productImage = new ProductImage();
            productImage.setPath(pathToSavedImage);
            productImage.setProduct(product);
            product.addImage(productImage);
        }

        productService.saveProduct(product);
        return "redirect:/shop?page=" + page;
    }
}
