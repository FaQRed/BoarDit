package pl.sankouski.boarditfront.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sankouski.boarditfront.dto.CategoryDTO;
import pl.sankouski.boarditfront.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(@CookieValue(value = "jwt", required = false) String token, Model model) {
        categoryService.setToken(token);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories";
    }

    @PostMapping("/add")
    public String addCategory(@CookieValue(value = "jwt", required = false) String token, @RequestParam String name, RedirectAttributes redirectAttributes) {
        try {
            categoryService.setToken(token);
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setName(name);
            categoryService.createCategory(categoryDTO);
            redirectAttributes.addFlashAttribute("success", "Category added successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@CookieValue(value = "jwt", required = false) String token, @PathVariable Long id, @RequestParam String name, RedirectAttributes redirectAttributes) {
        try {
            categoryService.setToken(token);
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(id);
            categoryDTO.setName(name);
            categoryService.updateCategory(categoryDTO, id);
            redirectAttributes.addFlashAttribute("success", "Category updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@CookieValue(value = "jwt", required = false) String token, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.setToken(token);
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}