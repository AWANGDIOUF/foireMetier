export interface IApropos {
  id?: number;
  texte?: string | null;
}

export class Apropos implements IApropos {
  constructor(public id?: number, public texte?: string | null) {}
}

export function getAproposIdentifier(apropos: IApropos): number | undefined {
  return apropos.id;
}
