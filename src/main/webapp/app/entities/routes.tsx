import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Marketplace from './marketplace';
import SellerInfo from './seller-info';
import Seller from './seller';
import Producer from './producer';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="marketplace/*" element={<Marketplace />} />
        <Route path="seller-info/*" element={<SellerInfo />} />
        <Route path="seller/*" element={<Seller />} />
        <Route path="producer/*" element={<Producer />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
