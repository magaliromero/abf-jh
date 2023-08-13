package py.com.abf.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.com.abf.service.CodigoVerificadorService;

@RestController
@RequestMapping("/digito")
public class CodigoVerificadorResource {

    private final CodigoVerificadorService service;

    public CodigoVerificadorResource(CodigoVerificadorService service) {
        this.service = service;
    }

    @GetMapping("/codigo/{documento}")
    public ResponseEntity<String> getAllcodigo(@PathVariable String documento) {
        String codigo = service.calcularCodigo(documento);
        return ResponseEntity.ok().body(codigo);
    }
}
