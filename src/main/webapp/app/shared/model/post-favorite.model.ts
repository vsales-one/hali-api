import { Moment } from 'moment';

export interface IPostFavorite {
  id?: number;
  dateFavorited?: Moment;
  postingItemId?: number;
  userProfileId?: number;
}

export const defaultValue: Readonly<IPostFavorite> = {};
