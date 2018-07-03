package com.example.sandwitch;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    SandwichRepository sandwichRepository;

    @Autowired
    CloudinaryConfig cloudinaryConfig;

    @RequestMapping("/")
    public String listSandwich(Model model)
    {
        model.addAttribute("sandwiches", sandwichRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String addSandwich(Model model)
    {
        model.addAttribute("sandwich", new Sandwich());
        return "form";
    }

    @PostMapping("/process")
    public String processSandwich(@Valid @ModelAttribute Sandwich sandwich, BindingResult result,
                                   @PathVariable("file") MultipartFile file)
    {
        if(result.hasErrors())
        {
            return "redirect:/";
        }
        if(file.isEmpty())
        {
            return "redirect:/";
        }
        try {
            Map upload = cloudinaryConfig.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            sandwich.setImage(upload.get("url").toString());
            sandwichRepository.save(sandwich);
            return "redirect:/";
        }catch (IOException e)
        {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @RequestMapping("/update/{id}")
    public String updateSancwich(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("sandwich", sandwichRepository.findById(id));
        return "form";
    }

    @RequestMapping("/detail/{id}")
    public String detailSandwich(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("sandwich", sandwichRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/delete/{id}")
    public String deleteSandwich(@PathVariable("id") long id)
    {
        sandwichRepository.deleteById(id);
        return "redirect:/";
    }

}
