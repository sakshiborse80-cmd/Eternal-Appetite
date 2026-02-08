package com.example.The.Eternal.Appetite.Controller;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.The.Eternal.Appetite.Repository.CategoryRepository;
import com.example.The.Eternal.Appetite.Repository.MenuItemRepository;
import com.example.The.Eternal.Appetite.entity.Category;
import com.example.The.Eternal.Appetite.entity.MenuItem;

import jakarta.servlet.http.HttpSession;



@Controller
public class MenuController {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String homePage(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "sort", required = false) String sort,
        Model model
    ) {
        List<MenuItem> menuItems;

        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();

            Optional<Category> matchedCategory = categoryRepository.findAll().stream()
                .filter(cat -> cat.getName().toLowerCase().contains(lowerKeyword))
                .findFirst();

            if (matchedCategory.isPresent()) {
                menuItems = menuItemRepository.findByCategory(matchedCategory.get());
            } else {
                try {
                    double price = Double.parseDouble(keyword);
                    menuItems = menuItemRepository.findByPrice(price);
                } catch (NumberFormatException e) {
                    menuItems = menuItemRepository.findByNameContainingIgnoreCase(keyword);
                }
            }
        } else {
            menuItems = menuItemRepository.findAll();
        }

        // ✅ Sorting logic
        if (sort != null) {
            if (sort.equals("name-asc")) {
                menuItems.sort(Comparator.comparing(MenuItem::getName));
            } else if (sort.equals("name-desc")) {
                menuItems.sort(Comparator.comparing(MenuItem::getName).reversed());
            } else if (sort.equals("price-low")) {
                menuItems.sort(Comparator.comparing(MenuItem::getPrice));
            } else if (sort.equals("price-high")) {
                menuItems.sort(Comparator.comparing(MenuItem::getPrice).reversed());
            }
        }

        model.addAttribute("menuItems", menuItems);
        model.addAttribute("categories", categoryRepository.findAll());
        return "home";
    }


    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<MenuItem> items = menuItemRepository.findByNameContainingIgnoreCase(keyword);
        model.addAttribute("menuItems", items);
        return "home";
    }
    
    @GetMapping("/menu")
    public String showMenuPage(Model model) {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        model.addAttribute("menuItems", menuItems);
        return "menu"; // this should be 'menu.html' in your templates folder
    }

    @GetMapping("/menu/details/{id}")
    public String getMenuItemDetails(@PathVariable("id") Long id, Model model) {
        MenuItem item = menuItemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid menu item ID: " + id));
        model.addAttribute("menuItem", item);
        return "menu-details";
    }

    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/category/{id}")
    public String showItemsByCategory(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id).orElse(null);
        List<MenuItem> items = menuItemRepository.findByCategoryId(id);
        model.addAttribute("category", category);
        model.addAttribute("menuItems", items);
        return "category-items";
    }

    
}



