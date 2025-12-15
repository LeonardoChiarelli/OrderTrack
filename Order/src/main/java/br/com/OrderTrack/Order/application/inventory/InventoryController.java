package br.com.OrderTrack.Order.application.inventory;

import br.com.OrderTrack.Order.application.inventory.dto.InventoryListDTO;
import br.com.OrderTrack.Order.application.inventory.dto.UpdateInventoryDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderTrack/admin/inventory")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @GetMapping
    public ResponseEntity<Page<InventoryListDTO>> listOfProductsInInventory(@PageableDefault(sort = {"quantity"}) Pageable pageable){
        return ResponseEntity.ok().body(service.getList(pageable));
    }

    @PatchMapping("/add")
    @Transactional
    public ResponseEntity<Void> addQuantity(@RequestBody @Valid UpdateInventoryDTO dto){
        service.addStock(dto.productIdOrNumber(), dto.quantity());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/decrease")
    @Transactional
    public ResponseEntity<Void> removeQuantity(@RequestBody @Valid UpdateInventoryDTO dto){
        service.decreaseStock(dto.productIdOrNumber(), dto.quantity());
        return ResponseEntity.ok().build();
    }
}
