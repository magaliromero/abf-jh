import dayjs from 'dayjs/esm';
import { IMateriales } from 'app/entities/materiales/materiales.model';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { EstadosPrestamos } from 'app/entities/enumerations/estados-prestamos.model';

export interface IPrestamos {
  id: number;
  fechaPrestamo?: dayjs.Dayjs | null;
  fechaDevolucion?: dayjs.Dayjs | null;
  estado?: EstadosPrestamos | null;
  materiales?: Pick<IMateriales, 'id' | 'descripcion'> | null;
  alumnos?: Pick<IAlumnos, 'id' | 'nombreCompleto'> | null;
}

export type NewPrestamos = Omit<IPrestamos, 'id'> & { id: null };
