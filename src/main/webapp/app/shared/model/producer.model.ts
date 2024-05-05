import dayjs from 'dayjs';

export interface IProducer {
  id?: string;
  producerName?: string;
  createdAt?: dayjs.Dayjs;
  producerLogoContentType?: string | null;
  producerLogo?: string | null;
}

export const defaultValue: Readonly<IProducer> = {};
