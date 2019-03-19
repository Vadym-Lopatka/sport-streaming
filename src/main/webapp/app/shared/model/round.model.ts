export interface IRound {
  id?: number;
  redTotalScore?: number;
  blueTotalScore?: number;
  fightId?: number;
}

export const defaultValue: Readonly<IRound> = {};
