package it.lessons.pizzeria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import it.lessons.pizzeria.model.Ingredient;
import it.lessons.pizzeria.model.Pizza;
import it.lessons.pizzeria.repository.IngredientRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/ingredient")
public class ControllerIngredient {

    @Autowired
    private IngredientRepository ingredientRepo;

    @GetMapping
    public String index(Model model) {

        List<Ingredient> all = ingredientRepo.findAll();

        model.addAttribute("ingredients", all);
        model.addAttribute("ingre", new Ingredient());

        return "/ingredients/index";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute(name = "ingre") Ingredient ingredient, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            List<Ingredient> all = ingredientRepo.findAll();

            model.addAttribute("ingredients", all);
            model.addAttribute("ingre", new Ingredient());

            return "/ingredients/index";
        }

        ingredientRepo.save(ingredient);

        return "redirect:/ingredient";
    }

    @PostMapping("delete")
    public String delete(@PathVariable Integer id, Model model) {

        Ingredient ingredient = ingredientRepo.findById(id).get();

        for (Pizza pizza : ingredient.getPizzas()) {
            pizza.getIngredients().remove(ingredient);
        }

        ingredientRepo.deleteById(id);

        return "redirect:/ingredients";
    }

}
