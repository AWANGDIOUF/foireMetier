export interface IObjectif {
  id?: number;
  objectif?: string | null;
}

export class Objectif implements IObjectif {
  constructor(public id?: number, public objectif?: string | null) {}
}

export function getObjectifIdentifier(objectif: IObjectif): number | undefined {
  return objectif.id;
}
