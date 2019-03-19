export interface IScore {
  id?: number;
  fightId?: number;
  roundId?: number;
  judgeId?: number;
  value?: number;
}

export const defaultValue: Readonly<IScore> = {};
