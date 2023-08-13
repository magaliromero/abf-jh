import dayjs from 'dayjs/esm';
import { ISucursales } from 'app/entities/sucursales/sucursales.model';

export interface ITimbrados {
  id: number;
  numeroTimbrado?: string | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  sucursales?: Pick<ISucursales, 'id'> | null;
}

export type NewTimbrados = Omit<ITimbrados, 'id'> & { id: null };
