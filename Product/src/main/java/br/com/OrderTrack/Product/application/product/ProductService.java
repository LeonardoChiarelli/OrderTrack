package br.com.OrderTrack.Product.application.product;

import br.com.OrderTrack.Product.application.product.dto.CreateProductDTO;
import br.com.OrderTrack.Product.application.product.dto.ListOfProductsDTO;
import br.com.OrderTrack.Order.infrastructure.inventory.InventoryEntity;
import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;
import br.com.OrderTrack.Order.infrastructure.inventory.JpaInventoryRepository;
import br.com.OrderTrack.Order.infrastructure.product.JpaProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private JpaProductRepository repository;

    @Autowired
    private JpaInventoryRepository inventoryRepository;

    public ProductEntity createProduct(@Valid CreateProductDTO dto) {
        var existingProduct = repository.existsByName(dto.name());

        // Strategy
        if (existingProduct) { throw new ValidationException("ProductEntity already existing."); }

        var product = new ProductEntity(dto);
        repository.save(product);

        var inventory = new InventoryEntity(dto.initialInventory(), product);
        inventoryRepository.save(inventory);

        return product;
    }

    public ProductEntity changeProductStauts(Long id, Boolean status) {
        var product = repository.findById(id).orElseThrow(() -> new ValidationException("ProductEntity with id: '%d' wasn't find.".formatted(id)));

        // Strategy
        if (product.isActive() == status){
            if (!status) { throw new ValidationException("ProductEntity is already deactivated"); }
            throw new ValidationException("ProductEntity is already activated");
        }

        product.changeStatus(status);
        return product;
    }

    public Page<ListOfProductsDTO> listActivatedProducts(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(ListOfProductsDTO::new);
    }

    public Page<ListOfProductsDTO> listAllProducts(Pageable pageable) {
        return repository.findAll(pageable).map(ListOfProductsDTO::new);
    }

    public void deleteProduct(Long id) {
        repository.findById(id).orElseThrow(() -> new ValidationException("ProductEntity with id: '%d' wasn't find.".formatted(id)));
        repository.deleteById(id);
    }
}
