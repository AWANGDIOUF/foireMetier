export interface ISalonMetier {
  id?: number;
  salonMetier?: string | null;
}

export class SalonMetier implements ISalonMetier {
  constructor(public id?: number, public salonMetier?: string | null) {}
}

export function getSalonMetierIdentifier(salonMetier: ISalonMetier): number | undefined {
  return salonMetier.id;
}
