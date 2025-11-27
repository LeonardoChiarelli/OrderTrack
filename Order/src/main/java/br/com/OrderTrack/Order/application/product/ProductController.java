package br.com.OrderTrack.Order.application.product;

import br.com.OrderTrack.Order.application.product.dto.CreateProductDTO;
import br.com.OrderTrack.Order.application.product.dto.ListOfProductsDTO;
import br.com.OrderTrack.Order.application.product.dto.ProductDetailsDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("orderTrack/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/admin/create")
    @Transactional
    public ResponseEntity<ProductDetailsDTO> createProduct(@RequestBody @Valid CreateProductDTO dto, UriComponentsBuilder uriBuilder){

        var product = service.createProduct(dto);
        var status = "Active";
        if (!product.isActive()) { status = "Inactive"; }

        var uri = uriBuilder.path("{id}").buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(uri).body(new ProductDetailsDTO(product, status));
    }

    @GetMapping("/admin/active")
    public ResponseEntity<Page<ListOfProductsDTO>> listActivatedProducts(@PageableDefault(sort = {"name"})Pageable pageable){
        return ResponseEntity.ok(service.listActivatedProducts(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ListOfProductsDTO>> listAllProducts(@PageableDefault(sort = {"name"})Pageable pageable){
        return ResponseEntity.ok(service.listAllProducts(pageable));
    }

    @PatchMapping("/admin/{id}/deactivate")
    @Transactional
    public ResponseEntity<String> deactivateProduct(@PathVariable Long id){
        var product = service.changeProductStauts(id, false);
        return ResponseEntity.ok("ProductEntity '%s' was deactivate successfully!".formatted(product.getName()));
    }

    @PatchMapping("/admin/{id}/activate")
    @Transactional
    public ResponseEntity<String> activateProduct(@PathVariable Long id){
        var product = service.changeProductStauts(id, true);
        return ResponseEntity.ok("ProductEntity '%s' was activate successfully.".formatted(product.getName()));
    }

    @DeleteMapping("/admin/{id}/delete")
    @Transactional
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
