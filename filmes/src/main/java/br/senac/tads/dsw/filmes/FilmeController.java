package br.senac.tads.dsw.filmes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/filmes")
public class FilmeController {

    @Autowired
    private FilmeRepository filmeRepository;

    @GetMapping
    public String listarFilmes(Model model) {
        List<Filme> filmes = filmeRepository.findAll();
        model.addAttribute("filmes", filmes);
        return "lista";
    }

    @GetMapping("/novo")
    public String mostrarFormularioAdicao(Model model) {
        model.addAttribute("filme", new Filme());
        return "form";
    }

    @PostMapping("/novo")
    public String adicionarFilme(@Valid @ModelAttribute("filme") Filme filme, BindingResult result) {
        if (result.hasErrors()) {
            return "form";
        }
        filmeRepository.save(filme);
        return "redirect:/filmes";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable("id") Integer id, Model model) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de filme inválido:" + id));
        model.addAttribute("filme", filme);
        return "form";
    }

    @PostMapping("/editar/{id}")
    public String atualizarFilme(@PathVariable("id") Integer id, @Valid @ModelAttribute("filme") Filme filme,
                              BindingResult result) {
        if (result.hasErrors()) {
            filme.setId(id.longValue());
            return "form";
        }
        filmeRepository.save(filme);
        return "redirect:/filmes";
    }


    @GetMapping("/excluir/{id}")
    public String excluirFilme(@PathVariable("id") Integer id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de filme inválido:" + id));
        filmeRepository.delete(filme);
        return "redirect:/filmes";
    }
}
