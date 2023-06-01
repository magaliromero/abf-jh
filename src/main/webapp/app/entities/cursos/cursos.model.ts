import dayjs from 'dayjs/esm';
import { Niveles } from 'app/entities/enumerations/niveles.model';

export interface ICursos {
  id: number;
  nombreCurso?: string | null;
  descripcion?: string | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  cantidadClases?: number | null;
  nivel?: Niveles | null;
}

export type NewCursos = Omit<ICursos, 'id'> & { id: null };
