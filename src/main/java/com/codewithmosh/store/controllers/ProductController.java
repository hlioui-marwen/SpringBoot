package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(required = false, name = "categoryId") Byte categoryId) {

        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }


        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProduct(@PathVariable Byte categoryId) {
        // ResponseEntity<List<ProductDto>>  because you’re returning both data and HTTP status information
        List<Product> products;
        products = productRepository.findByCategoryId(categoryId);

        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ProductDto> productDtos = products.stream().map(productMapper::toDto).toList();

        return ResponseEntity.ok(productDtos);

    }

}
