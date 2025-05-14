package com.istanbul.eminonurehber.Controller;

import com.istanbul.eminonurehber.DTO.CompanyDTO;
import com.istanbul.eminonurehber.Repository.CategoryRepository;
import com.istanbul.eminonurehber.Repository.CommentRepository;
import com.istanbul.eminonurehber.Repository.CompanyRepository;
import com.istanbul.eminonurehber.Repository.ProductRepository;
import com.istanbul.eminonurehber.Service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GeneralController {
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;

    public GeneralController(CompanyRepository companyRepository, CategoryRepository categoryRepository, CommentRepository commentRepository, ProductRepository productRepository) {
        this.companyRepository = companyRepository;

        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/counts")
    public ArrayList<Long> getCounts (){
        Long comp = companyRepository.count();
        Long cat = categoryRepository.count();
        Long comm = commentRepository.count();
        Long prod = productRepository.count();
        ArrayList<Long> counts = new ArrayList<>();
        counts.add(comp);
        counts.add(cat);
        counts.add(comm);
        counts.add(prod);

        return counts;

    }

    @GetMapping("/top-clicked-companies")
    public ResponseEntity<List<CompanyDTO>> getTopClickedCompanies() {
        List<CompanyDTO> topCompanies = companyRepository.findTop5ByOrderByClickCountDesc()
                .stream()
                .map(c -> new CompanyDTO(c.getName(), c.getClickCount()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(topCompanies);
    }
}
