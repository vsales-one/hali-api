import { Moment } from 'moment';
import { IPostFavorite } from 'app/shared/model/post-favorite.model';

export interface IPostingItem {
  id?: number;
  title?: string;
  imageUrl?: string;
  last_modified_date?: Moment;
  last_modified_by?: string;
  description?: string;
  pickUpTime?: string;
  startDate?: Moment;
  endDate?: Moment;
  pickupAddress?: string;
  latitude?: number;
  longitude?: number;
  city?: string;
  district?: string;
  categoryCategoryName?: string;
  categoryId?: number;
  postFavorites?: IPostFavorite[];
}

export const defaultValue: Readonly<IPostingItem> = {};
