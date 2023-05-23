import { IEvaluaciones } from 'app/entities/evaluaciones/evaluaciones.model';
import { ITemas } from 'app/entities/temas/temas.model';

export interface IEvaluacionesDetalle {
  id: number;
  comentarios?: string | null;
  puntaje?: number | null;
  evaluaciones?: Pick<IEvaluaciones, 'id' | 'nroEvaluacion'> | null;
  temas?: Pick<ITemas, 'id' | 'titulo'> | null;
}

export type NewEvaluacionesDetalle = Omit<IEvaluacionesDetalle, 'id'> & { id: null };
