export interface ITemas {
  id: number;
  titulo?: string | null;
  descripcion?: string | null;
}

export type NewTemas = Omit<ITemas, 'id'> & { id: null };
