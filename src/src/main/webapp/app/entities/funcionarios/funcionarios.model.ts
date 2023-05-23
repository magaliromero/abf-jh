import dayjs from 'dayjs/esm';
import { ITiposDocumentos } from 'app/entities/tipos-documentos/tipos-documentos.model';
import { EstadosPersona } from 'app/entities/enumerations/estados-persona.model';
import { TipoFuncionarios } from 'app/entities/enumerations/tipo-funcionarios.model';

export interface IFuncionarios {
  id: number;
  elo?: number | null;
  fideId?: number | null;
  nombres?: string | null;
  apellidos?: string | null;
  nombreCompleto?: string | null;
  email?: string | null;
  telefono?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  documento?: string | null;
  estado?: EstadosPersona | null;
  tipoFuncionario?: TipoFuncionarios | null;
  tipoDocumentos?: Pick<ITiposDocumentos, 'id' | 'descripcion'> | null;
}

export type NewFuncionarios = Omit<IFuncionarios, 'id'> & { id: null };
