import { IMarketplace } from 'app/shared/model/marketplace.model';

export interface ISellerInfo {
  id?: string;
  marketplaceName?: string;
  url?: string | null;
  country?: string;
  externalId?: string;
  marketplaceId?: IMarketplace;
}

export const defaultValue: Readonly<ISellerInfo> = {};
