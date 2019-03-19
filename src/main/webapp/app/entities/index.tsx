import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Fight from './fight';
import Round from './round';
import Score from './score';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/fight`} component={Fight} />
      <ErrorBoundaryRoute path={`${match.url}/round`} component={Round} />
      <ErrorBoundaryRoute path={`${match.url}/score`} component={Score} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
