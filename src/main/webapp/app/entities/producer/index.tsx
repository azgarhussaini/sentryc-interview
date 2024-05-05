import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Producer from './producer';
import ProducerDetail from './producer-detail';
import ProducerUpdate from './producer-update';
import ProducerDeleteDialog from './producer-delete-dialog';

const ProducerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Producer />} />
    <Route path="new" element={<ProducerUpdate />} />
    <Route path=":id">
      <Route index element={<ProducerDetail />} />
      <Route path="edit" element={<ProducerUpdate />} />
      <Route path="delete" element={<ProducerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProducerRoutes;
