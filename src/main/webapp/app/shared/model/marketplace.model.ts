export interface IMarketplace {
  id?: string;
  description?: string;
  marketplaceLogoContentType?: string | null;
  marketplaceLogo?: string | null;
}

export const defaultValue: Readonly<IMarketplace> = {};
