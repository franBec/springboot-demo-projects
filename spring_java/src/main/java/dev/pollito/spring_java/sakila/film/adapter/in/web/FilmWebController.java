package dev.pollito.spring_java.sakila.film.adapter.in.web;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.model.FilmFilter;
import dev.pollito.spring_java.sakila.film.domain.model.FilmLanguage;
import dev.pollito.spring_java.sakila.film.domain.model.FilmRating;
import dev.pollito.spring_java.sakila.film.domain.port.in.FilmUseCases;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class FilmWebController {

  private final FilmUseCases filmUseCases;

  @GetMapping("/films")
  public String listFilms(
      @ModelAttribute FilmFilter filter,
      @RequestParam(defaultValue = "title") String sort,
      @RequestParam(defaultValue = "asc") String order,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "9") int size,
      @NonNull Model model) {

    var sortDirection = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
    var pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

    Page<Film> films = filmUseCases.getFilms(filter, pageable);
    model.addAttribute("films", films);
    model.addAttribute("filter", filter);
    model.addAttribute("sort", sort);
    model.addAttribute("order", order);
    model.addAttribute("ratings", FilmRating.values());
    model.addAttribute("languages", FilmLanguage.values());
    return "films/list";
  }

  @GetMapping("/films/{id}")
  public String filmDetail(@PathVariable Integer id, @NonNull Model model) {
    Film film = filmUseCases.getFilm(id);
    model.addAttribute("film", film);
    return "films/detail";
  }

  @ModelAttribute("allLanguages")
  public List<FilmLanguage> allLanguages() {
    return Arrays.asList(FilmLanguage.values());
  }

  @ModelAttribute("allRatings")
  public List<FilmRating> allRatings() {
    return Arrays.asList(FilmRating.values());
  }

  @GetMapping("/films/new")
  public String newFilmForm(@NonNull Model model) {
    model.addAttribute(
        "film",
        Film.builder()
            .title("")
            .language(FilmLanguage.ENGLISH)
            .rentalDuration(3)
            .rentalRate(BigDecimal.valueOf(4.99))
            .replacementCost(BigDecimal.valueOf(19.99))
            .build());
    model.addAttribute("isNew", true);
    return "films/form";
  }

  @PostMapping("/films")
  public String createFilm(
      @ModelAttribute Film film, @NonNull RedirectAttributes redirectAttributes) {
    Film created = filmUseCases.createFilm(film);
    redirectAttributes.addFlashAttribute("toast", "Film created successfully");
    return "redirect:/films/" + created.getId();
  }

  @GetMapping("/films/{id}/edit")
  public String editFilmForm(@PathVariable Integer id, @NonNull Model model) {
    Film film = filmUseCases.getFilm(id);
    model.addAttribute("film", film);
    model.addAttribute("isNew", false);
    return "films/form";
  }

  @PutMapping("/films/{id}")
  public String updateFilm(
      @PathVariable Integer id,
      @ModelAttribute Film film,
      @NonNull RedirectAttributes redirectAttributes) {
    filmUseCases.updateFilm(id, film);
    redirectAttributes.addFlashAttribute("toast", "Film updated successfully");
    return "redirect:/films/" + id;
  }

  @DeleteMapping("/films/{id}")
  public String deleteFilm(
      @PathVariable Integer id, @NonNull RedirectAttributes redirectAttributes) {
    filmUseCases.deleteFilm(id);
    redirectAttributes.addFlashAttribute("toast", "Film deleted successfully");
    return "redirect:/films";
  }
}
