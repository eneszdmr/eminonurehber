package com.istanbul.eminonurehber.Service;

import com.istanbul.eminonurehber.DTO.ProductMobileDTO;
import com.istanbul.eminonurehber.Entity.Company;
import com.istanbul.eminonurehber.Entity.Product;
import com.istanbul.eminonurehber.Repository.CompanyRepository;
import com.istanbul.eminonurehber.Repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    public ProductService(ProductRepository productRepository, CompanyRepository companyRepository) {
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
    }
    public List<Product> getAllProductsWithCompanyNames() {
        return productRepository.findAll();
    }

    public List<ProductMobileDTO> getAllMobileProductsWithCompanyNames() {
        List<ProductMobileDTO> mobileProductDTOS = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            ProductMobileDTO productMobileDTO = new ProductMobileDTO();
            productMobileDTO.setId(product.getId());
            productMobileDTO.setCompanyId(product.getCompany().getId());
            productMobileDTO.setCompanyName(product.getCompany().getName());
            productMobileDTO.setProductTitle(product.getProductTitle());
            productMobileDTO.setProductDescription(product.getProductDescription());
            productMobileDTO.setProductPrice(product.getProductPrice());

            // Byte array'i Base64 string'e dönüştürme
            byte[] imageBytes = product.getProductImage();
            if (imageBytes != null) {
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                productMobileDTO.setImageBase64(base64Image);
            } else {
                productMobileDTO.setImageBase64(""); // Eğer resim yoksa boş string
            }

            mobileProductDTOS.add(productMobileDTO);
        }
        return mobileProductDTOS;
    }


    @Transactional
    public Product addProduct(Product product, Long companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            product.setCompany(company.get());
            return productRepository.save(product);
        }
        throw new RuntimeException("Firma bulunamadı!");
    }



    public List<Product> getProductsByCompany(Long companyId) {
        return productRepository.findByCompanyId(companyId);
    }

    public ResponseEntity<Product> getProductDetails(long id) {
        return ResponseEntity.ok(productRepository.findById(id).get());
    }
}