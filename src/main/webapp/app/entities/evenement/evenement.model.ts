export interface IEvenement {
  id?: number;
  evenement?: string | null;
}

export class Evenement implements IEvenement {
  constructor(public id?: number, public evenement?: string | null) {}
}

export function getEvenementIdentifier(evenement: IEvenement): number | undefined {
  return evenement.id;
}
