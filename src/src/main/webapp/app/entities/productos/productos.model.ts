import { TipoProductos } from 'app/entities/enumerations/tipo-productos.model';

export interface IProductos {
  id: number;
  tipoProducto?: TipoProductos | null;
  precioUnitario?: number | null;
  porcentajeIva?: number | null;
  descripcion?: string | null;
}

export type NewProductos = Omit<IProductos, 'id'> & { id: null };
