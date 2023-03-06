export interface IEntreprise {
  id?: number;
  entreprise?: string | null;
}

export class Entreprise implements IEntreprise {
  constructor(public id?: number, public entreprise?: string | null) {}
}

export function getEntrepriseIdentifier(entreprise: IEntreprise): number | undefined {
  return entreprise.id;
}
