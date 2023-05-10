import dayjs from 'dayjs/esm';
import { IMateriales } from 'app/entities/materiales/materiales.model';

export interface IPrestamos {
  id: number;
  fechaPrestamo?: dayjs.Dayjs | null;
  vigenciaPrestamo?: number | null;
  fechaDevolucion?: dayjs.Dayjs | null;
  materiales?: Pick<IMateriales, 'id' | 'descripcion'> | null;
}

export type NewPrestamos = Omit<IPrestamos, 'id'> & { id: null };
