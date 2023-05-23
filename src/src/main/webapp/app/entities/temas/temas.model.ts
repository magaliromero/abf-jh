import { Niveles } from 'app/entities/enumerations/niveles.model';

export interface ITemas {
  id: number;
  titulo?: string | null;
  descripcion?: string | null;
  nivel?: Niveles | null;
}

export type NewTemas = Omit<ITemas, 'id'> & { id: null };
