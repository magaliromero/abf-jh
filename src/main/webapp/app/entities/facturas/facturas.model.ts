import dayjs from 'dayjs/esm';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { CondVenta } from 'app/entities/enumerations/cond-venta.model';

export interface IFacturas {
  id: number;
  fecha?: dayjs.Dayjs | null;
  facturaNro?: string | null;
  timbrado?: number | null;
  razonSocial?: string | null;
  ruc?: number | null;
  condicionVenta?: CondVenta | null;
  cantidad?: number | null;
  descripcion?: string | null;
  precioUnitario?: number | null;
  valor5?: number | null;
  valor10?: number | null;
  total?: number | null;
  total5?: number | null;
  total10?: number | null;
  totalIva?: number | null;
  alumnos?: Pick<IAlumnos, 'id' | 'nombreCompleto'> | null;
}

export type NewFacturas = Omit<IFacturas, 'id'> & { id: null };
