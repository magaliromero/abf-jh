export interface IMateriales {
  id: number;
  descripcion?: string | null;
  cantidad?: number | null;
  cantidadEnPrestamo?: number | null;
}

export type NewMateriales = Omit<IMateriales, 'id'> & { id: null };
