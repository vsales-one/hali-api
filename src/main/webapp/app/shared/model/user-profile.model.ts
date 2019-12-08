export interface IUserProfile {
  id?: number;
  userId?: string;
  imageUrl?: string;
  city?: string;
  address?: string;
  district?: string;
  phoneNumber?: string;
  latitude?: number;
  longitude?: number;
  displayName?: string;
}

export const defaultValue: Readonly<IUserProfile> = {};
