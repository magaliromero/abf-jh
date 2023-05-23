import dayjs from 'dayjs/esm';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';

export interface IEvaluaciones {
  id: number;
  nroEvaluacion?: number | null;
  fecha?: dayjs.Dayjs | null;
  alumnos?: Pick<IAlumnos, 'id' | 'nombreCompleto'> | null;
  funcionarios?: Pick<IFuncionarios, 'id' | 'nombreCompleto'> | null;
}

export type NewEvaluaciones = Omit<IEvaluaciones, 'id'> & { id: null };
