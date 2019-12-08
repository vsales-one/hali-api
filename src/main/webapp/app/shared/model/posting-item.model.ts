import { Moment } from 'moment';

export interface IPostingItem {
  id?: number;
  title?: string;
  imageUrl?: string;
  description?: string;
  pickUpTime?: string;
  startDate?: Moment;
  endDate?: Moment;
  pickupAddress?: string;
  latitude?: number;
  longitude?: number;
  city?: string;
  district?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  status?: string;
  categoryCategoryName?: string;
  categoryId?: number;
}

export const defaultValue: Readonly<IPostingItem> = {};
