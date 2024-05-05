import { IProducer } from 'app/shared/model/producer.model';
import { ISellerInfo } from 'app/shared/model/seller-info.model';
import { SellerState } from 'app/shared/model/enumerations/seller-state.model';

export interface ISeller {
  id?: string;
  sellerName?: string;
  state?: keyof typeof SellerState;
  producerId?: IProducer;
  sellerInfoId?: ISellerInfo;
}

export const defaultValue: Readonly<ISeller> = {};
