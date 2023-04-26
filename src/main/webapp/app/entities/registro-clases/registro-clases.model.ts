import dayjs from 'dayjs/esm';
import { ICursos } from 'app/entities/cursos/cursos.model';
import { ITemas } from 'app/entities/temas/temas.model';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';

export interface IRegistroClases {
  id: number;
  fecha?: dayjs.Dayjs | null;
  cantidadHoras?: number | null;
  asistenciaAlumno?: boolean | null;
  cursos?: Pick<ICursos, 'id' | 'nombreCurso'> | null;
  temas?: Pick<ITemas, 'id' | 'titulo'> | null;
  funcionarios?: Pick<IFuncionarios, 'id' | 'nombreCompleto'> | null;
  alumnos?: Pick<IAlumnos, 'id' | 'nombreCompleto'> | null;
}

export type NewRegistroClases = Omit<IRegistroClases, 'id'> & { id: null };
