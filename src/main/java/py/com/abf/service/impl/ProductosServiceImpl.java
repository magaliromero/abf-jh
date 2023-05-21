package py.com.abf.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.com.abf.domain.Productos;
import py.com.abf.repository.ProductosRepository;
import py.com.abf.service.ProductosService;

/**
 * Service Implementation for managing {@link Productos}.
 */
@Service
@Transactional
public class ProductosServiceImpl implements ProductosService {

    private final Logger log = LoggerFactory.getLogger(ProductosServiceImpl.class);

    private final ProductosRepository productosRepository;

    public ProductosServiceImpl(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    @Override
    public Productos save(Productos productos) {
        log.debug("Request to save Productos : {}", productos);
        return productosRepository.save(productos);
    }

    @Override
    public Productos update(Productos productos) {
        log.debug("Request to update Productos : {}", productos);
        return productosRepository.save(productos);
    }

    @Override
    public Optional<Productos> partialUpdate(Productos productos) {
        log.debug("Request to partially update Productos : {}", productos);

        return productosRepository
            .findById(productos.getId())
            .map(existingProductos -> {
                if (productos.getTipoProducto() != null) {
                    existingProductos.setTipoProducto(productos.getTipoProducto());
                }
                if (productos.getPrecioUnitario() != null) {
                    existingProductos.setPrecioUnitario(productos.getPrecioUnitario());
                }
                if (productos.getPorcentajeIva() != null) {
                    existingProductos.setPorcentajeIva(productos.getPorcentajeIva());
                }
                if (productos.getDescripcion() != null) {
                    existingProductos.setDescripcion(productos.getDescripcion());
                }

                return existingProductos;
            })
            .map(productosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Productos> findAll(Pageable pageable) {
        log.debug("Request to get all Productos");
        return productosRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Productos> findOne(Long id) {
        log.debug("Request to get Productos : {}", id);
        return productosRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Productos : {}", id);
        productosRepository.deleteById(id);
    }
}
