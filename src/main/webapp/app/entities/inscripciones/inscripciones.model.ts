import dayjs from 'dayjs/esm';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { ICursos } from 'app/entities/cursos/cursos.model';

export interface IInscripciones {
  id: number;
  fechaInscripcion?: dayjs.Dayjs | null;
  alumnos?: Pick<IAlumnos, 'id' | 'nombreCompleto'> | null;
  cursos?: Pick<ICursos, 'id' | 'nombreCurso'> | null;
}

export type NewInscripciones = Omit<IInscripciones, 'id'> & { id: null };
