import { EstadosMateriales } from 'app/entities/enumerations/estados-materiales.model';

export interface IMateriales {
  id: number;
  descripcion?: string | null;
  estado?: EstadosMateriales | null;
  cantidadStock?: number | null;
  cantidadPrestamo?: number | null;
}

export type NewMateriales = Omit<IMateriales, 'id'> & { id: null };
