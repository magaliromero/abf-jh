import dayjs from 'dayjs/esm';
import { ITemas } from 'app/entities/temas/temas.model';
import { Niveles } from 'app/entities/enumerations/niveles.model';

export interface ICursos {
  id: number;
  nombreCurso?: string | null;
  descripcion?: string | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  cantidadClases?: number | null;
  nivel?: Niveles | null;
  temas?: Pick<ITemas, 'id' | 'titulo'> | null;
}

export type NewCursos = Omit<ICursos, 'id'> & { id: null };
