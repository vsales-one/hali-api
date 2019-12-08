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

import { IPostFavorite, defaultValue } from 'app/shared/model/post-favorite.model';

export const ACTION_TYPES = {
  FETCH_POSTFAVORITE_LIST: 'postFavorite/FETCH_POSTFAVORITE_LIST',
  FETCH_POSTFAVORITE: 'postFavorite/FETCH_POSTFAVORITE',
  CREATE_POSTFAVORITE: 'postFavorite/CREATE_POSTFAVORITE',
  UPDATE_POSTFAVORITE: 'postFavorite/UPDATE_POSTFAVORITE',
  DELETE_POSTFAVORITE: 'postFavorite/DELETE_POSTFAVORITE',
  RESET: 'postFavorite/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPostFavorite>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PostFavoriteState = Readonly<typeof initialState>;

// Reducer

export default (state: PostFavoriteState = initialState, action): PostFavoriteState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_POSTFAVORITE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POSTFAVORITE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_POSTFAVORITE):
    case REQUEST(ACTION_TYPES.UPDATE_POSTFAVORITE):
    case REQUEST(ACTION_TYPES.DELETE_POSTFAVORITE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_POSTFAVORITE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POSTFAVORITE):
    case FAILURE(ACTION_TYPES.CREATE_POSTFAVORITE):
    case FAILURE(ACTION_TYPES.UPDATE_POSTFAVORITE):
    case FAILURE(ACTION_TYPES.DELETE_POSTFAVORITE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_POSTFAVORITE_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_POSTFAVORITE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_POSTFAVORITE):
    case SUCCESS(ACTION_TYPES.UPDATE_POSTFAVORITE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_POSTFAVORITE):
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

const apiUrl = 'api/post-favorites';

// Actions

export const getEntities: ICrudGetAllAction<IPostFavorite> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_POSTFAVORITE_LIST,
    payload: axios.get<IPostFavorite>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPostFavorite> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POSTFAVORITE,
    payload: axios.get<IPostFavorite>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPostFavorite> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POSTFAVORITE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IPostFavorite> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POSTFAVORITE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPostFavorite> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POSTFAVORITE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
