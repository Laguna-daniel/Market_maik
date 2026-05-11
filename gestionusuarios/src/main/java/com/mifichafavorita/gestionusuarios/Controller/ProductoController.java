import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/micromarket/productos")
@CrossOrigin(origins = "*") 
public class ProductoController {

    @Autowired
    private ProductoService service;

    @GetMapping("/lista")
    public List<Producto> getAll() {
        return service.listarTodo();
    }

    @PostMapping("/guardar")
    public Producto save(@RequestBody Producto p) {
        return service.registrarProducto(p);
    }

    @DeleteMapping("/eliminar/{id}")
    public void delete(@PathVariable Long id) {
        service.borrar(id);
    }
}