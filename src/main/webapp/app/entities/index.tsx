import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Category from './category';
import City from './city';
import District from './district';
import PostingItem from './posting-item';
import UserProfile from './user-profile';
import PostFavorite from './post-favorite';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/category`} component={Category} />
      <ErrorBoundaryRoute path={`${match.url}/city`} component={City} />
      <ErrorBoundaryRoute path={`${match.url}/district`} component={District} />
      <ErrorBoundaryRoute path={`${match.url}/posting-item`} component={PostingItem} />
      <ErrorBoundaryRoute path={`${match.url}/user-profile`} component={UserProfile} />
      <ErrorBoundaryRoute path={`${match.url}/post-favorite`} component={PostFavorite} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
