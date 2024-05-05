import marketplace from 'app/entities/marketplace/marketplace.reducer';
import sellerInfo from 'app/entities/seller-info/seller-info.reducer';
import seller from 'app/entities/seller/seller.reducer';
import producer from 'app/entities/producer/producer.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  marketplace,
  sellerInfo,
  seller,
  producer,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
