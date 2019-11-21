import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PostingItem from './posting-item';
import PostingItemDetail from './posting-item-detail';
import PostingItemUpdate from './posting-item-update';
import PostingItemDeleteDialog from './posting-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PostingItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PostingItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PostingItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={PostingItem} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PostingItemDeleteDialog} />
  </>
);

export default Routes;
