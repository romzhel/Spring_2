package ru.romzhel.eshop.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(ProductController.class);
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

    @GetMapping("/edit/{page}/{id}")
    public String edit(Model model, @PathVariable Integer page, @PathVariable(name = "id") Long id) {
        logger.trace("requested editing item id = {}", id);
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
        logger.trace("requested new/edited product {}", product);

        if (product.getId() == 0 && productService.isProductWithTitleExists(product.getTitle())) {
            theBindingResult.addError(new ObjectError("product.title", "Товар с таким названием уже существует")); // todo не отображает сообщение
        }

        if (theBindingResult.hasErrors()) {
            logger.warn("product edit form error {}", theBindingResult.getAllErrors());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "edit-product";
        }

        if (!file.isEmpty()) {
            String pathToSavedImage = imageSaverService.saveFile(file);
            ProductImage productImage = new ProductImage();
            productImage.setPath(pathToSavedImage);
            productImage.setProduct(product);
            product.addImage(productImage);
        }

        logger.trace("saving product {}", product);
        productService.saveProduct(product);
        return "redirect:/shop?page=" + page;
    }
}
