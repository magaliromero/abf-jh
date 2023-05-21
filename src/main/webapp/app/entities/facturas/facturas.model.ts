import dayjs from 'dayjs/esm';
import { CondicionVenta } from 'app/entities/enumerations/condicion-venta.model';

export interface IFacturas {
  id: number;
  fecha?: dayjs.Dayjs | null;
  facturaNro?: string | null;
  timbrado?: number | null;
  razonSocial?: string | null;
  ruc?: string | null;
  condicionVenta?: CondicionVenta | null;
  total?: number | null;
}

export type NewFacturas = Omit<IFacturas, 'id'> & { id: null };
