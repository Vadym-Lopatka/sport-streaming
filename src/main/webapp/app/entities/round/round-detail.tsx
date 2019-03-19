import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './round.reducer';
import { IRound } from 'app/shared/model/round.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRoundDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RoundDetail extends React.Component<IRoundDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { roundEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Round [<b>{roundEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="redTotalScore">Red Total Score</span>
            </dt>
            <dd>{roundEntity.redTotalScore}</dd>
            <dt>
              <span id="blueTotalScore">Blue Total Score</span>
            </dt>
            <dd>{roundEntity.blueTotalScore}</dd>
            <dt>Fight</dt>
            <dd>{roundEntity.fightId ? roundEntity.fightId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/round" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/round/${roundEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ round }: IRootState) => ({
  roundEntity: round.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RoundDetail);
