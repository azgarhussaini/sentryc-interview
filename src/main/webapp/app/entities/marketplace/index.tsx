import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Marketplace from './marketplace';
import MarketplaceDetail from './marketplace-detail';
import MarketplaceUpdate from './marketplace-update';
import MarketplaceDeleteDialog from './marketplace-delete-dialog';

const MarketplaceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Marketplace />} />
    <Route path="new" element={<MarketplaceUpdate />} />
    <Route path=":id">
      <Route index element={<MarketplaceDetail />} />
      <Route path="edit" element={<MarketplaceUpdate />} />
      <Route path="delete" element={<MarketplaceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MarketplaceRoutes;
