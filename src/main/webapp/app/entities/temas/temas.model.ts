import { ICursos } from 'app/entities/cursos/cursos.model';

export interface ITemas {
  id: number;
  titulo?: string | null;
  descripcion?: string | null;
  cursos?: Pick<ICursos, 'id' | 'nombreCurso'> | null;
}

export type NewTemas = Omit<ITemas, 'id'> & { id: null };
