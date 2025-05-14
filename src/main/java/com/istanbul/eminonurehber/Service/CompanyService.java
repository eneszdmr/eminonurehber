package com.istanbul.eminonurehber.Service;

import com.istanbul.eminonurehber.DTO.CompanyDTO;
import com.istanbul.eminonurehber.DTO.CompanyImageDTO;
import com.istanbul.eminonurehber.DTO.CompanyListForMobileIdName;
import com.istanbul.eminonurehber.DTO.CompanyMobileDTO;
import com.istanbul.eminonurehber.Entity.*;
import com.istanbul.eminonurehber.Repository.CategoryRepository;
import com.istanbul.eminonurehber.Repository.CompanyRepository;
import com.istanbul.eminonurehber.Repository.RoleRepository;
import com.istanbul.eminonurehber.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class CompanyService {


    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, CategoryRepository categoryRepository, RoleRepository roleRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<CompanyDTO> getOneCompany(Long id){
        return companyRepository.findById(id).map(this::convertCompanyToDTO);
    }

    public List<CompanyDTO> getCompanyByEmail(String email){
        List<CompanyDTO> dtos=new ArrayList<>();

        companyRepository.findByEmail(email).ifPresent(company -> {
            CompanyDTO dto=modelMapper.map(company,CompanyDTO.class);
            dtos.add(dto);
        });
        return dtos;
    }


    // bütün firmaları ve detaylarıyla getirir
    public List<CompanyMobileDTO> getAllCompaniesMobile() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(company -> new CompanyMobileDTO(
                        company.getId(),
                        company.getName(),
                        company.getAddress(),
                        company.getPhone(),
                        company.getWebsite(),
                        company.getCategory().getId(),
                        company.getEmail(),
                        company.getCategory().getName(),
                        company.getLatitude().toString(),
                        company.getLongitude().toString()
                ))
                .collect(Collectors.toList());
    }

    public List<CompanyListForMobileIdName> getAllCompaniesIDName() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(company -> new CompanyListForMobileIdName(
                        company.getId(),
                        company.getName(),
                        company.getCategory().getName()
                ))
                .collect(Collectors.toList());
    }



    public List<CompanyDTO> getAllCompanies() {
        List<CompanyDTO> allcomps=new ArrayList<>();
        allcomps= companyRepository.findAll().stream().map(company ->{

            CompanyDTO dto=modelMapper.map(company,CompanyDTO.class);
            dto.setCategoryId(company.getCategory().getId());
            dto.setCategoryName(company.getCategory().getName());
            return dto;
        }).collect(Collectors.toList());

        return allcomps;
    }

    public List<CompanyDTO> getCompaniesByCategory(String categoryName) {
        return companyRepository.findByCategoryName(categoryName).stream().map( company ->
       modelMapper.map(company,CompanyDTO.class)).collect(Collectors.toList());

    }

    @Transactional
    public Company createCompany(CompanyDTO companyDTO) {
        try {
            // Kullanıcı oluştur
            User user = new User();
            user.setEmail(companyDTO.getEmail());
            user.setPassword(passwordEncoder.encode(companyDTO.getPassword()));

            Role firmaRole = roleRepository.findByName("FIRMA")
                    .orElseThrow(() -> new RuntimeException("FIRMA rolü bulunamadı"));
            user.setRoles(Set.of(firmaRole));
            user = userRepository.save(user);

            // Kategori kontrolü
            Category category = categoryRepository.findById(companyDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Kategori bulunamadı: " + companyDTO.getCategoryId()));

            // Şirket nesnesini oluştur
            Company company = new Company();
            company.setName(companyDTO.getName());
            company.setEmail(companyDTO.getEmail());
            company.setPassword(passwordEncoder.encode(companyDTO.getPassword()));
            company.setAddress(companyDTO.getAddress());
            company.setPhone(companyDTO.getPhone());
            company.setWebsite(companyDTO.getWebsite());
            company.setUser(user);
            company.setCategory(category);
            company.setLatitude(Double.valueOf(companyDTO.getLatitude()));
            company.setLongitude(Double.valueOf(companyDTO.getLongitude()));

            company.setWhatsapp(companyDTO.getWhatsapp());
            company.setFacebook(companyDTO.getFacebook());
            company.setTwitter(companyDTO.getTwitter());
            company.setInstagram(companyDTO.getInstagram());
            company.setTiktok(companyDTO.getTiktok());
            company.setYoutube(companyDTO.getYoutube());

            Company savedCompany = companyRepository.save(company);

            // Resimleri ekle
            if (companyDTO.getImages() != null && !companyDTO.getImages().isEmpty()) {
                List<CompanyImage> companyImages = new ArrayList<>();

                for (CompanyImageDTO imageDTO : companyDTO.getImages()) {
                    if (imageDTO.getImageBase64() != null && !imageDTO.getImageBase64().isEmpty()) {
                        try {
                            byte[] imageBytes = Base64.getDecoder().decode(imageDTO.getImageBase64());
                            CompanyImage companyImage = new CompanyImage();
                            companyImage.setImageData(imageBytes);
                            companyImage.setCompany(savedCompany);
                            companyImages.add(companyImage);
                        } catch (IllegalArgumentException e) {
                            throw new RuntimeException("Geçersiz Base64 formatı: " + e.getMessage());
                        }
                    }
                }

                savedCompany.setImages(companyImages);
                System.out.println("saved company: " + savedCompany);
                //companyRepository.save(savedCompany); // resimleri dahil ederek tekrar kaydet
            }

            return savedCompany;
        } catch (Exception ex) {
            // Gerekirse loglama yapılabilir
            throw new RuntimeException("Firma oluşturulurken hata oluştu: " + ex.getMessage(), ex);
        }
    }




    public void deleteACompany(Long id) {
        companyRepository.deleteById(id);
    }
    @Transactional
    public CompanyDTO updateCompany(Long id, CompanyDTO dto) {
        try {
            // Mevcut şirketi getir
            Company existCompany = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Firma bulunamadı: " + id));

            // Kategori kontrolü
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Kategori bulunamadı: " + dto.getCategoryId()));

            // Şirket bilgilerini güncelle
            existCompany.setName(dto.getName());
            existCompany.setEmail(dto.getEmail());
            existCompany.setPassword(passwordEncoder.encode(dto.getPassword()));
            existCompany.setAddress(dto.getAddress());
            existCompany.setPhone(dto.getPhone());
            existCompany.setWebsite(dto.getWebsite());
            existCompany.setLongitude(Double.valueOf(dto.getLongitude()));
            existCompany.setLatitude(Double.valueOf(dto.getLatitude()));
            existCompany.setCategory(category);
            // social media update
            existCompany.setFacebook(dto.getFacebook());
            existCompany.setTwitter(dto.getTwitter());
            existCompany.setInstagram(dto.getInstagram());
            existCompany.setYoutube(dto.getYoutube());
            existCompany.setTiktok(dto.getTiktok());
            existCompany.setWhatsapp(dto.getWhatsapp());

            // Görselleri güncelle (önce mevcutları sil, sonra yenileri ekle)


                List<CompanyImage> imageList = existCompany.getImages();
                imageList.clear();
            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                for (CompanyImageDTO imageDTO : dto.getImages()) {
                    if (imageDTO.getImageBase64() != null && !imageDTO.getImageBase64().isEmpty()) {
                        try {
                            byte[] imageBytes = Base64.getDecoder().decode(imageDTO.getImageBase64());
                            CompanyImage image = new CompanyImage();
                            image.setImageData(imageBytes);
                            image.setCompany(existCompany); // çok önemli!
                            imageList.add(image);
                        } catch (IllegalArgumentException e) {
                            throw new RuntimeException("Geçersiz Base64 formatı: " + e.getMessage());
                        }
                    }
                }
            }


            // Şirketi kaydet
            Company updatedCompany = companyRepository.save(existCompany);

            // Kullanıcı bilgilerini güncelle
            User updateUser = userRepository.findByEmail(updatedCompany.getEmail())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + dto.getEmail()));
            updateUser.setEmail(updatedCompany.getEmail());
            updateUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            userRepository.save(updateUser);

            // DTO'ya dönüştürüp döndür
            return convertCompanyToDTO(updatedCompany);

        } catch (Exception e) {
            throw new RuntimeException("Firma güncellenirken hata oluştu: " + e.getMessage(), e);
        }
    }


    public CompanyDTO convertCompanyToDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();

        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setAddress(company.getAddress());
        dto.setPhone(company.getPhone());
        dto.setWebsite(company.getWebsite());
        dto.setPassword(company.getPassword());
        dto.setCategoryId(company.getCategory() != null ? company.getCategory().getId() : null);
        dto.setCategoryName(company.getCategory() != null ? company.getCategory().getName() : null);
        dto.setEmail(company.getEmail());
        dto.setLatitude(company.getLatitude() != null ? company.getLatitude().toString() : null);
        dto.setLongitude(company.getLongitude() != null ? company.getLongitude().toString() : null);

        dto.setFacebook(company.getFacebook() != null ? company.getFacebook().toString() : null);
        dto.setTwitter(company.getTwitter() != null ? company.getTwitter().toString() : null);
        dto.setInstagram(company.getInstagram() != null ? company.getInstagram().toString() : null);
        dto.setYoutube(company.getYoutube() != null ? company.getYoutube().toString() : null);
        dto.setTiktok(company.getTiktok() != null ? company.getTiktok().toString() : null);
        dto.setWhatsapp(company.getWhatsapp() != null ? company.getWhatsapp().toString() : null);

        if (company.getImages() != null) {
            List<CompanyImageDTO> imageDTOs = company.getImages().stream()
                    .filter(img -> img.getImageData() != null)
                    .map(img -> {
                        CompanyImageDTO imageDTO = new CompanyImageDTO();
                        imageDTO.setImageBase64(Base64.getEncoder().encodeToString(img.getImageData()));
                        return imageDTO;
                    })
                    .collect(Collectors.toList());

            dto.setImages(imageDTOs);
        }

        return dto;
    }

    public void incrementCompanyClick(Long id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            company.setClickCount(company.getClickCount() + 1);
            companyRepository.save(company);
            ResponseEntity.ok().build();
        } else {
            ResponseEntity.notFound().build();
        }
    }


}
