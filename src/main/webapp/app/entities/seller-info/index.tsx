import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SellerInfo from './seller-info';
import SellerInfoDetail from './seller-info-detail';
import SellerInfoUpdate from './seller-info-update';
import SellerInfoDeleteDialog from './seller-info-delete-dialog';

const SellerInfoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SellerInfo />} />
    <Route path="new" element={<SellerInfoUpdate />} />
    <Route path=":id">
      <Route index element={<SellerInfoDetail />} />
      <Route path="edit" element={<SellerInfoUpdate />} />
      <Route path="delete" element={<SellerInfoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SellerInfoRoutes;
