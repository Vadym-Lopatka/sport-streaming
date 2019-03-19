import { IRound } from 'app/shared/model/round.model';

export const enum ResultEnum {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  FINISHED = 'FINISHED',
  NOT_HAPPENED = 'NOT_HAPPENED',
  OTHER = 'OTHER'
}

export const enum TypeEnum {
  RED_WIN = 'RED_WIN',
  BLUE_WIN = 'BLUE_WIN',
  NOT_DEFINED = 'NOT_DEFINED',
  OTHER = 'OTHER'
}

export interface IFight {
  id?: number;
  redCorner?: string;
  blueCorner?: string;
  result?: ResultEnum;
  resultType?: TypeEnum;
  comment?: string;
  rounds?: IRound[];
}

export const defaultValue: Readonly<IFight> = {};
