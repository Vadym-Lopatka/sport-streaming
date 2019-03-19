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

import { IRound, defaultValue } from 'app/shared/model/round.model';

export const ACTION_TYPES = {
  FETCH_ROUND_LIST: 'round/FETCH_ROUND_LIST',
  FETCH_ROUND: 'round/FETCH_ROUND',
  CREATE_ROUND: 'round/CREATE_ROUND',
  UPDATE_ROUND: 'round/UPDATE_ROUND',
  DELETE_ROUND: 'round/DELETE_ROUND',
  RESET: 'round/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRound>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RoundState = Readonly<typeof initialState>;

// Reducer

export default (state: RoundState = initialState, action): RoundState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ROUND_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ROUND):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ROUND):
    case REQUEST(ACTION_TYPES.UPDATE_ROUND):
    case REQUEST(ACTION_TYPES.DELETE_ROUND):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ROUND_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ROUND):
    case FAILURE(ACTION_TYPES.CREATE_ROUND):
    case FAILURE(ACTION_TYPES.UPDATE_ROUND):
    case FAILURE(ACTION_TYPES.DELETE_ROUND):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROUND_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROUND):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ROUND):
    case SUCCESS(ACTION_TYPES.UPDATE_ROUND):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ROUND):
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

const apiUrl = 'api/rounds';

// Actions

export const getEntities: ICrudGetAllAction<IRound> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ROUND_LIST,
    payload: axios.get<IRound>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRound> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ROUND,
    payload: axios.get<IRound>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRound> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ROUND,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IRound> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ROUND,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRound> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ROUND,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
