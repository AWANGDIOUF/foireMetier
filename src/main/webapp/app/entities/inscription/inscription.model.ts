import { DemandeStand } from 'app/entities/enumerations/demande-stand.model';

export interface IInscription {
  id?: number;
  partenaire?: string | null;
  non?: string | null;
  prenon?: string | null;
  adresseProfessionnelle?: string | null;
  telephoneProfessionnelle?: string | null;
  emailProfessionnelle?: string | null;
  demandeStand?: DemandeStand | null;
  tailleStand?: string | null;
  autres?: string | null;
}

export class Inscription implements IInscription {
  constructor(
    public id?: number,
    public partenaire?: string | null,
    public non?: string | null,
    public prenon?: string | null,
    public adresseProfessionnelle?: string | null,
    public telephoneProfessionnelle?: string | null,
    public emailProfessionnelle?: string | null,
    public demandeStand?: DemandeStand | null,
    public tailleStand?: string | null,
    public autres?: string | null
  ) {}
}

export function getInscriptionIdentifier(inscription: IInscription): number | undefined {
  return inscription.id;
}
