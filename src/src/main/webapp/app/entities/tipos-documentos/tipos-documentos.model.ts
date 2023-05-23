export interface ITiposDocumentos {
  id: number;
  codigo?: string | null;
  descripcion?: string | null;
}

export type NewTiposDocumentos = Omit<ITiposDocumentos, 'id'> & { id: null };
