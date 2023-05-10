import dayjs from 'dayjs/esm';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { TiposEvaluaciones } from 'app/entities/enumerations/tipos-evaluaciones.model';

export interface IEvaluaciones {
  id: number;
  tipoEvaluacion?: TiposEvaluaciones | null;
  idExamen?: number | null;
  idActa?: number | null;
  fecha?: dayjs.Dayjs | null;
  puntosLogrados?: number | null;
  porcentaje?: number | null;
  comentarios?: string | null;
  alumnos?: Pick<IAlumnos, 'id' | 'nombreCompleto'> | null;
}

export type NewEvaluaciones = Omit<IEvaluaciones, 'id'> & { id: null };
