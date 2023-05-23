import dayjs from 'dayjs/esm';
import { IProductos } from 'app/entities/productos/productos.model';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';

export interface IPagos {
  id: number;
  fecha?: dayjs.Dayjs | null;
  total?: number | null;
  cantidadHoras?: number | null;
  producto?: Pick<IProductos, 'id' | 'descripcion'> | null;
  funcionario?: Pick<IFuncionarios, 'id' | 'nombreCompleto'> | null;
}

export type NewPagos = Omit<IPagos, 'id'> & { id: null };
