import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PostFavorite from './post-favorite';
import PostFavoriteDetail from './post-favorite-detail';
import PostFavoriteUpdate from './post-favorite-update';
import PostFavoriteDeleteDialog from './post-favorite-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PostFavoriteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PostFavoriteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PostFavoriteDetail} />
      <ErrorBoundaryRoute path={match.url} component={PostFavorite} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PostFavoriteDeleteDialog} />
  </>
);

export default Routes;
