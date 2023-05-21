import { IProductos } from 'app/entities/productos/productos.model';
import { IFacturas } from 'app/entities/facturas/facturas.model';

export interface IFacturaDetalle {
  id: number;
  cantidad?: number | null;
  precioUnitario?: number | null;
  subtotal?: number | null;
  porcentajeIva?: number | null;
  valorPorcentaje?: number | null;
  producto?: Pick<IProductos, 'id' | 'descripcion'> | null;
  factura?: Pick<IFacturas, 'id' | 'facturaNro'> | null;
}

export type NewFacturaDetalle = Omit<IFacturaDetalle, 'id'> & { id: null };
