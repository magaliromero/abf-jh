import dayjs from 'dayjs/esm';
import { IClientes } from 'app/entities/clientes/clientes.model';
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
  clientes?: Pick<IClientes, 'id' | 'ruc'> | null;
}

export type NewFacturas = Omit<IFacturas, 'id'> & { id: null };
