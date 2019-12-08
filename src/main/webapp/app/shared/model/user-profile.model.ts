export interface IUserProfile {
  id?: number;
  imageUrl?: string;
  city?: string;
  address?: string;
  district?: string;
  phoneNumber?: string;
  fullName?: string;
  userLogin?: string;
  userId?: number;
}

export const defaultValue: Readonly<IUserProfile> = {};
