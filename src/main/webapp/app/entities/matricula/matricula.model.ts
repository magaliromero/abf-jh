import dayjs from 'dayjs/esm';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { EstadosPagos } from 'app/entities/enumerations/estados-pagos.model';

export interface IMatricula {
  id: number;
  concepto?: string | null;
  monto?: number | null;
  fechaInscripcion?: dayjs.Dayjs | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaPago?: dayjs.Dayjs | null;
  estado?: EstadosPagos | null;
  alumno?: Pick<IAlumnos, 'id' | 'nombreCompleto'> | null;
}

export type NewMatricula = Omit<IMatricula, 'id'> & { id: null };
