import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPostingItem, defaultValue } from 'app/shared/model/posting-item.model';

export const ACTION_TYPES = {
  FETCH_POSTINGITEM_LIST: 'postingItem/FETCH_POSTINGITEM_LIST',
  FETCH_POSTINGITEM: 'postingItem/FETCH_POSTINGITEM',
  CREATE_POSTINGITEM: 'postingItem/CREATE_POSTINGITEM',
  UPDATE_POSTINGITEM: 'postingItem/UPDATE_POSTINGITEM',
  DELETE_POSTINGITEM: 'postingItem/DELETE_POSTINGITEM',
  RESET: 'postingItem/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPostingItem>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PostingItemState = Readonly<typeof initialState>;

// Reducer

export default (state: PostingItemState = initialState, action): PostingItemState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_POSTINGITEM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POSTINGITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_POSTINGITEM):
    case REQUEST(ACTION_TYPES.UPDATE_POSTINGITEM):
    case REQUEST(ACTION_TYPES.DELETE_POSTINGITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_POSTINGITEM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POSTINGITEM):
    case FAILURE(ACTION_TYPES.CREATE_POSTINGITEM):
    case FAILURE(ACTION_TYPES.UPDATE_POSTINGITEM):
    case FAILURE(ACTION_TYPES.DELETE_POSTINGITEM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_POSTINGITEM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_POSTINGITEM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_POSTINGITEM):
    case SUCCESS(ACTION_TYPES.UPDATE_POSTINGITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_POSTINGITEM):
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

const apiUrl = 'api/posting-items';

// Actions

export const getEntities: ICrudGetAllAction<IPostingItem> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_POSTINGITEM_LIST,
    payload: axios.get<IPostingItem>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPostingItem> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POSTINGITEM,
    payload: axios.get<IPostingItem>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPostingItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POSTINGITEM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPostingItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POSTINGITEM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPostingItem> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POSTINGITEM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
