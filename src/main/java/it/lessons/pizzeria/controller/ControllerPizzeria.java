package it.lessons.pizzeria.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.lessons.pizzeria.model.Pizza;
import it.lessons.pizzeria.model.SpecialOffer;
import it.lessons.pizzeria.repository.IngredientRepository;
import it.lessons.pizzeria.repository.PizzaRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/pizzeria")
public class ControllerPizzeria {

	@Autowired
	private PizzaRepository pizzaRepo;

	@Autowired
	private IngredientRepository ingredientRepo;

	@GetMapping
	public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
		List<Pizza> allPizzas;

		if (keyword != null && !keyword.isBlank()) {
			allPizzas = pizzaRepo.findByNameContaining(keyword);
			model.addAttribute("keyword", keyword);
		} else {
			allPizzas = pizzaRepo.findAll();
		}

		model.addAttribute("pizzas", allPizzas);

		return "/pizza/index";
	}

	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") Integer id, Model model) {

		Optional<Pizza> pizzOptional = pizzaRepo.findById(id);
		if (pizzOptional.isPresent()) {
			model.addAttribute("pizzas", pizzOptional.get());
		}

		return "/pizza/show";
	}

	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("pizza", new Pizza());
		model.addAttribute("allIngredients", ingredientRepo.findAll());
		return "/pizza/create";
	}

	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("pizza") Pizza formPizza,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "/pizza/create";
		}

		pizzaRepo.save(formPizza);

		return "redirect:/pizzeria";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("pizza", pizzaRepo.findById(id).get());
		model.addAttribute("allIngredients", ingredientRepo.findAll());

		return "pizza/edit";
	}

	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("pizza") Pizza formPizza,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "/pizza/edit";
		}

		pizzaRepo.save(formPizza);

		return "redirect:/pizzeria";
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {

		pizzaRepo.deleteById(id);

		return "redirect:/pizzeria";
	}

	@GetMapping("/{id}/specialoffer")
	public String specialoffer(@PathVariable Integer id, Model model) {

		Pizza pizza = pizzaRepo.findById(id).get();

		SpecialOffer specialOffer = new SpecialOffer();
		specialOffer.setPizza(pizza);
		specialOffer.setStartDate(LocalDate.now());

		model.addAttribute("specialoffer", specialOffer);

		return "/specialoffers/edit";

	}

}
