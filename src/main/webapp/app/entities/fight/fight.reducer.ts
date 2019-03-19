import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFight, defaultValue } from 'app/shared/model/fight.model';

export const ACTION_TYPES = {
  FETCH_FIGHT_LIST: 'fight/FETCH_FIGHT_LIST',
  FETCH_FIGHT: 'fight/FETCH_FIGHT',
  CREATE_FIGHT: 'fight/CREATE_FIGHT',
  UPDATE_FIGHT: 'fight/UPDATE_FIGHT',
  DELETE_FIGHT: 'fight/DELETE_FIGHT',
  RESET: 'fight/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFight>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type FightState = Readonly<typeof initialState>;

// Reducer

export default (state: FightState = initialState, action): FightState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FIGHT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FIGHT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FIGHT):
    case REQUEST(ACTION_TYPES.UPDATE_FIGHT):
    case REQUEST(ACTION_TYPES.DELETE_FIGHT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_FIGHT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FIGHT):
    case FAILURE(ACTION_TYPES.CREATE_FIGHT):
    case FAILURE(ACTION_TYPES.UPDATE_FIGHT):
    case FAILURE(ACTION_TYPES.DELETE_FIGHT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_FIGHT_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_FIGHT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FIGHT):
    case SUCCESS(ACTION_TYPES.UPDATE_FIGHT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FIGHT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/fights';

// Actions

export const getEntities: ICrudGetAllAction<IFight> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FIGHT_LIST,
    payload: axios.get<IFight>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IFight> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FIGHT,
    payload: axios.get<IFight>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFight> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FIGHT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IFight> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FIGHT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFight> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FIGHT,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
