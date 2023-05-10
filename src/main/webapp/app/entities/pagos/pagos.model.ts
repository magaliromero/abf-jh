import dayjs from 'dayjs/esm';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';
import { TiposPagos } from 'app/entities/enumerations/tipos-pagos.model';

export interface IPagos {
  id: number;
  montoPago?: number | null;
  montoInicial?: number | null;
  saldo?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  fechaPago?: dayjs.Dayjs | null;
  tipoPago?: TiposPagos | null;
  descripcion?: string | null;
  funcionarios?: Pick<IFuncionarios, 'id' | 'nombreCompleto'> | null;
}

export type NewPagos = Omit<IPagos, 'id'> & { id: null };
