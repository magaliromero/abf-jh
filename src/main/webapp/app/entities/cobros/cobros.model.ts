import dayjs from 'dayjs/esm';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { IFacturas } from 'app/entities/facturas/facturas.model';
import { TiposPagos } from 'app/entities/enumerations/tipos-pagos.model';

export interface ICobros {
  id: number;
  montoPago?: number | null;
  montoInicial?: number | null;
  saldo?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  fechaPago?: dayjs.Dayjs | null;
  tipoPago?: TiposPagos | null;
  descripcion?: string | null;
  alumnos?: Pick<IAlumnos, 'id' | 'nombreCompleto'> | null;
  factura?: Pick<IFacturas, 'id' | 'facturaNro'> | null;
}

export type NewCobros = Omit<ICobros, 'id'> & { id: null };
