import { IPostFavorite } from 'app/shared/model/post-favorite.model';

export interface IUserProfile {
  id?: number;
  userId?: string;
  imageUrl?: string;
  city?: string;
  address?: string;
  district?: string;
  phoneNumber?: string;
  postFavorites?: IPostFavorite[];
}

export const defaultValue: Readonly<IUserProfile> = {};
