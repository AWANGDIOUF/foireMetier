export interface IIntroduction {
  id?: number;
  introduction?: string | null;
}

export class Introduction implements IIntroduction {
  constructor(public id?: number, public introduction?: string | null) {}
}

export function getIntroductionIdentifier(introduction: IIntroduction): number | undefined {
  return introduction.id;
}
