package com.istanbul.eminonurehber.Controller;

import com.istanbul.eminonurehber.Config.JwtUtil;
import com.istanbul.eminonurehber.DTO.CompanyMobileDTO;
import com.istanbul.eminonurehber.DTO.ProductDTO;
import com.istanbul.eminonurehber.DTO.ProductMobileDTO;
import com.istanbul.eminonurehber.Entity.Company;
import com.istanbul.eminonurehber.Entity.Product;
import com.istanbul.eminonurehber.Repository.CompanyRepository;
import com.istanbul.eminonurehber.Service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;
    private final CompanyRepository companyRepository;



    // Uygulamanın çalıştığı URL'yi almak için
    @Value("${server.port}")
    private String serverPort;

    public ProductController(ProductService productService, JwtUtil jwtUtil, CompanyRepository companyRepository) {
        this.productService = productService;
        this.jwtUtil = jwtUtil;
        this.companyRepository = companyRepository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestHeader("Authorization") String token) {
        String role = jwtUtil.extractRoles(token).toString();
        String email = jwtUtil.extractEmail(token);

        if (role.contains("ADMIN")) {
            return ResponseEntity.ok(productService.getAllProductsWithCompanyNames());
        } else if (role.contains("FIRMA")) {
            Company company = companyRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Firma bulunamadı"));
            List<Product> productList=productService.getProductsByCompany(company.getId());
            return ResponseEntity.ok(productList);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/mobile")
    public List<ProductMobileDTO> getAllProductsForMobile(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isBlank()) {

            return productService.getAllMobileProductsWithCompanyNames();
        }
        return Collections.emptyList();
    }


    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestHeader("Authorization") String token,
                                                 @RequestBody ProductDTO productDTO) {
        String email = jwtUtil.extractEmail(token);

        try {
            Product product=new Product();
            Company company = companyRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Firma bulunamadı"));

            if (productDTO.getImageBase64() != null && !productDTO.getImageBase64().isEmpty()) {
                byte[] imageBytes = Base64.getDecoder().decode(productDTO.getImageBase64());
                product.setProductImage(imageBytes);
                product.setProductDescription(productDTO.getProductDescription());
                product.setProductTitle(productDTO.getProductTitle());
                product.setProductPrice(productDTO.getProductPrice());
            }

            product.setCompany(company);
            return ResponseEntity.ok(productService.addProduct(product, company.getId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @GetMapping("/{id}")
    ResponseEntity<Product> getProduct (@RequestHeader("Authorization") String token,
                                        @PathVariable("id") long id) {
      //  String role = jwtUtil.extractRoles(token).toString();
     //   String email = jwtUtil.extractEmail(token);

        return productService.getProductDetails(id);

    }
}
