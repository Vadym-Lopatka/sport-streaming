import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Fight from './fight';
import FightDetail from './fight-detail';
import FightUpdate from './fight-update';
import FightDeleteDialog from './fight-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FightUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FightUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FightDetail} />
      <ErrorBoundaryRoute path={match.url} component={Fight} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FightDeleteDialog} />
  </>
);

export default Routes;
