import { Moment } from 'moment';

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
}

export const defaultValue: Readonly<IPostingItem> = {};
